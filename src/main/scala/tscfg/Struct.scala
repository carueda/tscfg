package tscfg

import com.typesafe.config.Config
import tscfg.DefineCase.{
  AbstractDefineCase,
  EnumDefineCase,
  ExtendsDefineCase,
  SimpleDefineCase
}
import tscfg.exceptions.ObjectDefinitionException

import scala.annotation.tailrec
import scala.collection.{Map, mutable}

/** Supports a convenient next representation based on given TS Config object.
  * It supports nested member definitions utilizing the 'members' field
  *
  * @param name
  *   Name of the config member
  * @param members
  *   Nested config definitions
  */
case class Struct(
    name: String,
    members: mutable.HashMap[String, Struct] = mutable.HashMap.empty,
) {

  // Non-None when this is a `@define`
  var defineCaseOpt: Option[DefineCase] = None

  def isDefine: Boolean = defineCaseOpt.isDefined

  def isExtends: Boolean = defineCaseOpt match {
    case Some(_: ExtendsDefineCase) => true
    case _                          => false
  }

  def isEnum: Boolean = defineCaseOpt.exists(_.isEnum)

  def isLeaf: Boolean = members.isEmpty

  // $COVERAGE-OFF$
  def format(indent: String = ""): String = {
    val defineStr = defineCaseOpt.map(dc => s" $dc").getOrElse("")
    val nameStr   = s"${if (name.isEmpty) "(root)" else name}$defineStr"

    if (members.isEmpty) {
      nameStr
    }
    else {
      val indent2 = indent + "    "
      s"$nameStr ->\n" + indent2 + {
        members
          .map(e => s"${e._1}: " + e._2.format(indent2))
          .mkString("\n" + indent2)
      }
    }
  }
  // $COVERAGE-ON$
}

object Struct {
  import scala.jdk.CollectionConverters._

  /** Gets all structs from the given TS Config object, sorted appropriately for
    * subsequent processing in ModelBuilder. Any circular reference will throw a
    * [[ObjectDefinitionException]].
    */
  def getMemberStructs(namespace: Namespace, conf: Config): List[Struct] = {
    val struct: Struct              = getStruct(conf)
    val memberStructs: List[Struct] = struct.members.values.toList

    // set any define to each struct:
    memberStructs.flatMap { setDefineCase(conf, _) }

    val (defineStructs, nonDefineStructs) = memberStructs.partition(_.isDefine)
    val sortedDefineStructs               = sortDefineStructs(defineStructs)

    if (namespace.isRoot) {
      scribe.debug(struct.format())
      scribe.debug(
        s"sortedDefineStructs=\n${sortedDefineStructs.map(_.format()).mkString("\n")}"
      )
    }

    sortedDefineStructs ++ nonDefineStructs
  }

  // ad hoc;  TODO capture external aspect as part of model
  def isExternalParent(name: String): Boolean = {
    name.startsWith("!")
  }

  private def sortDefineStructs(defineStructs: List[Struct]): List[Struct] = {
    val sorted = mutable.LinkedHashMap.empty[String, Struct]

    // / `othersBeingAdded` allows to check for circularity
    def addExtendStruct(
        s: Struct,
        othersBeingAdded: List[Struct] = List.empty
    ): Unit = s.defineCaseOpt.get match {

      case SimpleDefineCase | AbstractDefineCase | EnumDefineCase =>
        sorted.put(s.name, s)

      case ExtendsDefineCase(name, _) =>
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
                    path.reverse.map("'" + _.name + "'").mkString(" -> ")
                  throw ObjectDefinitionException(
                    s"extension of struct '${s.name}' involves circular reference via $via"
                  )
                }

                // ok, add ancestor:
                addExtendStruct(ancestor, s :: othersBeingAdded)
                // and then add this struct:
                sorted.put(s.name, s)

              case None if isExternalParent(name) =>
                sorted.put(s.name.substring(1), s)

              case None =>
                throw ObjectDefinitionException(
                  s"struct '${s.name}' with undefined extend '$name'"
                )
            }
        }
    }
    defineStructs.foreach(addExtendStruct(_))

    assert(
      defineStructs.size == sorted.size,
      s"defineStructs.size=${defineStructs.size} != sorted.size=${sorted.size}"
    )

    sorted.toList.map(_._2)
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
    * @return
    *   Mapping from symbol to type definition if struct is an ExtendsDefineCase
    */
  def ancestorClassMembers(
      struct: Struct,
      memberStructs: List[Struct],
      namespace: Namespace
  ): Option[Map[String, model.AnnType]] = {
    struct.defineCaseOpt.flatMap {
      case ExtendsDefineCase(parentName, _) =>
        val defineStructs = memberStructs.filter(_.isDefine)

        val greatAncestorMembers =
          defineStructs.find(_.name == parentName) match {
            case Some(parentStruct) if parentStruct.isExtends =>
              ancestorClassMembers(parentStruct, memberStructs, namespace)

            case Some(_) => None

            case None if isExternalParent(parentName) => None

            case None =>
              throw new RuntimeException(
                s"struct '${struct.name}' with undefined extend '$parentName'"
              )
          }

        val parentMembers =
          namespace.getRealDefine(parentName).map(_.members) match {
            case s @ Some(parentMembers) => parentMembers

            case None if isExternalParent(parentName) => None

            case None =>
              throw new IllegalArgumentException(
                s"@define '${struct.name}' is invalid because '$parentName' is not @defined"
              )
          }

        // join both member maps
        Some(greatAncestorMembers.getOrElse(Map.empty) ++ parentMembers)

      case _ => None
    }
  }

  private def getStruct(conf: Config): Struct = {
    val structs = mutable.HashMap[String, Struct]("" -> Struct(""))

    def resolve(key: String): Struct = {
      if (!structs.contains(key)) structs.put(key, Struct(getSimple(key)))
      structs(key)
    }

    // Due to TS Config API, we traverse from the leaves to the ancestors:
    conf.entrySet().asScala foreach { e =>
      val path = e.getKey
      val leaf = Struct(path)
      doAncestorsOf(path, leaf)

      def doAncestorsOf(childKey: String, childStruct: Struct): Unit = {
        val (parent, simple) = (getParent(childKey), getSimple(childKey))
        createParent(parent, simple, childStruct)

        @tailrec
        def createParent(
            parentKey: String,
            simple: String,
            child: Struct
        ): Unit = {
          val parentGroup = resolve(parentKey)
          parentGroup.members.put(simple, child)
          if (parentKey != "") {
            createParent(
              getParent(parentKey),
              getSimple(parentKey),
              parentGroup
            )
          }
        }
      }
    }

    def getParent(path: String): String = {
      val idx = path.lastIndexOf('.')
      if (idx >= 0) path.substring(0, idx) else ""
    }

    def getSimple(path: String): String = {
      val idx = path.lastIndexOf('.')
      if (idx >= 0) path.substring(idx + 1) else path
    }

    structs("")
  }

  private def setDefineCase(conf: Config, s: Struct): Option[DefineCase] = {
    val cv          = conf.getValue(s.name)
    val comments    = cv.origin().comments().asScala.toList
    val defineLines = comments.map(_.trim).filter(_.startsWith("@define"))
    s.defineCaseOpt = defineLines.length match {
      case 0 => None
      case 1 => DefineCase.getDefineCase(defineLines.head)
      case _ =>
        throw new IllegalArgumentException(
          s"multiple @define lines for ${s.name}."
        )
    }
    s.defineCaseOpt
  }
}
