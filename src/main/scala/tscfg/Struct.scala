package tscfg

import com.typesafe.config._
import tscfg.DefineCase._
import tscfg.exceptions.ObjectDefinitionException
import tscfg.ns.Namespace

import java.util.Map.Entry
import scala.collection.SeqMap
import scala.collection.{Map, mutable}
import scala.jdk.CollectionConverters._

/** Supports a convenient next representation based on given TS Config object.
  * It supports nested member definitions utilizing the 'members' field
  *
  * @param name
  *   Name of the config member
  * @param cv
  *   Associated ConfigValue
  * @param members
  *   Nested config definitions
  */
case class Struct(
    name: String,
    cv: ConfigValue,
    members: SeqMap[String, Struct] = SeqMap.empty,
) {
  /* Captures string value to support determining dependencies in terms of RHS
   * names (that is, when such a string may be referring to a @define)
   */
  private val tsStringValue: Option[String] =
    cv.valueType() match {
      case ConfigValueType.STRING => Some(cv.unwrapped().toString)
      case _                      => None
    }

  val comments: List[String] =
    cv.origin().comments().asScala.toList

  // Non-None when this is a `@define`
  val defineCaseOpt: Option[DefineCase] = {
    val defineLines = comments.map(_.trim).filter(_.startsWith("@define"))
    defineLines.length match {
      case 0 => None
      case 1 => DefineCase.getDefineCase(defineLines.head)
      case _ =>
        throw new IllegalArgumentException(s"multiple @define lines for $name.")
    }
  }

  def isEnum: Boolean = defineCaseOpt.exists(_.isEnum)

  def isLeaf: Boolean = members.isEmpty

  private def dependencies: Set[String] =
    tsStringValue.toSet ++ members.values.flatMap(_.dependencies)

  private def isDefine: Boolean = defineCaseOpt.isDefined

  private def isExtends: Boolean = defineCaseOpt match {
    case Some(_: ExtendsDefineCase)    => true
    case Some(_: ImplementsDefineCase) => true
    case _                             => false
  }
}

object Struct {

  /** Gets struct corresponding to given TS Config object, with members sorted
    * appropriately for subsequent processing in ModelBuilder. Any circular
    * reference will throw a [[ObjectDefinitionException]].
    */
  def getStruct(conf: Config): Struct = getStruct("", "", conf.root())

  private def sortStructs(structs: List[Struct]): List[Struct] = {
    val (defineStructs, nonDefineStructs) = structs.partition(_.isDefine)
    val sortedDefineStructs               = sortDefineStructs(defineStructs)
    // but also sort the "defines" by any name (member type) dependencies:
    val definesSortedByNameDependencies =
      sortByNameDependencies(sortedDefineStructs)
    definesSortedByNameDependencies ++ nonDefineStructs
  }

  private def sortDefineStructs(defineStructs: List[Struct]): List[Struct] = {
    val sorted = mutable.LinkedHashMap.empty[String, Struct]

    // / `othersBeingAdded` allows to check for circularity
    def addExtendStruct(
        s: Struct,
        othersBeingAdded: List[Struct] = List.empty,
    ): Unit = {
      def addExtendsOrImplements(name: String, isExternal: Boolean): Unit = {
        sorted.get(name) match {
          case Some(_) =>
            // ancestor already added. Add this descendent struct:
            sorted.put(s.name, s)

          case None =>
            // ancestor not added yet. Find it in base list:
            defineStructs.find(_.name == name) match {
              case Some(ancestor) =>
                // check for any circularity:
                if (othersBeingAdded.exists(_.name == ancestor.name)) {
                  val path = s :: othersBeingAdded
                  val via =
                    path.reverseIterator
                      .map("'" + _.name + "'")
                      .mkString(" -> ")
                  throw ObjectDefinitionException(
                    s"extension of struct '${s.name}' involves circular reference via $via",
                  )
                }

                // ok, add ancestor:
                addExtendStruct(ancestor, s :: othersBeingAdded)
                // and then add this struct:
                sorted.put(s.name, s)

              case None if isExternal =>
                sorted.put(s.name, s)

              case None =>
                throw ObjectDefinitionException(
                  s"struct '${s.name}' with undefined extend '$name'",
                )
            }
        }
      }

      s.defineCaseOpt.get match {
        case SimpleDefineCase | AbstractDefineCase | EnumDefineCase =>
          sorted.put(s.name, s)

        case c: ExtendsDefineCase =>
          addExtendsOrImplements(c.name, c.isExternal)

        case c: ImplementsDefineCase =>
          addExtendsOrImplements(c.name, c.isExternal)
      }
    }

    defineStructs.foreach(addExtendStruct(_))

    assert(
      defineStructs.size == sorted.size,
      s"defineStructs.size=${defineStructs.size} != sorted.size=${sorted.size}",
    )

    sorted.toList.map(_._2)
  }

  private def sortByNameDependencies(structs: List[Struct]): List[Struct] = {
    structs.sortWith((a, b) => {
      val aDeps = a.dependencies
      val bDeps = b.dependencies

      if (aDeps.contains(b.name)) {
        // a depends on b, so b should come first:
        false
      }
      else if (bDeps.contains(a.name)) {
        // b depends on a, so a should come first:
        true
      }
      else {
        // no dependency, so sort by name:
        a.name < b.name
      }
    })
  }

  /** Determines the joint set of all ancestor's members to allow proper
    * overriding in child structs.
    *
    * Note that not-circularity is verified prior to calling this function.
    *
    * @param struct
    *   Current (child) struct to consider
    * @param memberStructs
    *   List to find referenced structs
    * @param namespace
    *   Current known name space
    * @param firstPass
    *   true for a first pass, which will be less strict in terms of name
    *   resolution
    * @return
    *   Mapping from symbol to type definition if struct is an ExtendsDefineCase
    */
  def ancestorClassMembers(
      struct: Struct,
      memberStructs: List[Struct],
      namespace: Namespace,
      firstPass: Boolean,
  ): Option[Map[String, model.AnnType]] = {

    def handleExtends(
        parentName: String,
        isExternal: Boolean,
    ): Option[Map[String, model.AnnType]] = {
      val defineStructs = memberStructs.filter(_.isDefine)

      val greatAncestorMembers =
        defineStructs.find(_.name == parentName) match {
          case Some(parentStruct) if parentStruct.isExtends =>
            ancestorClassMembers(
              parentStruct,
              memberStructs,
              namespace,
              firstPass,
            )

          case Some(_) => None

          case None if isExternal => None

          case None =>
            throw new RuntimeException(
              s"struct '${struct.name}' with undefined extend '$parentName'",
            )
        }

      val parentMembers =
        namespace.getRealDefine(parentName).map(_.members) match {
          case Some(parentMembers) => parentMembers
          case None if isExternal  => None
          case None if firstPass   => None
          case None =>
            throw new IllegalArgumentException(
              s"@define '${struct.name}' is invalid because '$parentName' is not @defined",
            )
        }

      // join both member maps
      Some(greatAncestorMembers.getOrElse(Map.empty) ++ parentMembers)
    }

    struct.defineCaseOpt.flatMap {
      case c: ExtendsDefineCase =>
        handleExtends(c.name, c.isExternal)

      case c: ImplementsDefineCase =>
        // note: handling it as an extends (todo: revisit this at some point)
        handleExtends(c.name, c.isExternal)

      case _ => None
    }
  }

  private def getStruct(pp: String, key: String, cv: ConfigValue): Struct =
    cv match {
      case co: ConfigObject => getStructForObject(pp, key, co)
      case _                => getStructForLeaf(key, cv)
    }

  private def getStructForObject(
      pp: String,
      key: String,
      co: ConfigObject,
  ): Struct = {
    def doEntry(e: Entry[String, ConfigValue]): Struct = {
      val key   = e.getKey
      val cv    = e.getValue
      val newPp = if (pp.isEmpty) key else s"$pp.$key"
      getStruct(newPp, key, cv)
    }
    val structs       = co.entrySet().asScala.toList.map(doEntry)
    val sortedStructs = sortStructs(structs)
    val pairs         = sortedStructs.map(s => s.name -> s)
    val members       = SeqMap(pairs: _*)
    Struct(key, co, members)
  }

  private def getStructForLeaf(key: String, cv: ConfigValue): Struct = {
    val name = if (key.contains('$')) s"\"$key\"" else key
    Struct(name, cv)
  }

  // $COVERAGE-OFF$
  def main(args: Array[String]): Unit = {
    import java.io.File
    // tscfg.util.setLogMinLevel()
    val filename =
      args.headOption.getOrElse("src/main/tscfg/example/issue309b.spec.conf")
    println(s"filename = $filename")
    val file      = new File(filename)
    val bufSource = io.Source.fromFile(file)
    val source    = bufSource.mkString.trim
    bufSource.close()
    val conf   = ConfigFactory.parseString(source).resolve()
    val struct = getStruct(conf)
    pprint.pprintln(struct)
  }
  // $COVERAGE-ON$
}
