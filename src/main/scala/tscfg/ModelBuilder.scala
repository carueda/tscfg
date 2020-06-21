package tscfg

import com.typesafe.config._
import tscfg.generators.tsConfigUtil
import tscfg.model.durations.ms
import tscfg.model.{AbstractObjectType, AnnType, DURATION, ObjectType, SIZE}

import scala.jdk.CollectionConverters._

case class ModelBuildResult(objectType: model.ObjectType,
                            warnings: List[buildWarnings.Warning])

object buildWarnings {

  sealed abstract class Warning(ln: Int,
                                src: String,
                                msg: String = "") {
    val line: Int = ln
    val source: String = src
    val message: String = msg
  }

  case class MultElemListWarning(ln: Int, src: String) extends
    Warning(ln, src, "only first element will be considered")

  case class OptListElemWarning(ln: Int, src: String) extends
    Warning(ln, src, "ignoring optional mark in list's element type")

  case class DefaultListElemWarning(ln: Int, default: String, elemType: String) extends
    Warning(ln, default, s"ignoring default value='$default' in list's element type: $elemType")

}

class ModelBuilder(assumeAllRequired: Boolean = false) {

  import collection._
  import buildWarnings._

  def build(conf: Config): ModelBuildResult = {
    warns.clear()
    ModelBuildResult(objectType = fromConfig(Namespace.root, conf),
      warnings = warns.toList.sortBy(_.line))
  }

  private val warns = collection.mutable.ArrayBuffer[Warning]()

  private def fromConfig(namespace: Namespace, conf: Config): model.ObjectType = {
    val memberStructs = getMemberStructs(conf)
      // have the `@define`s be traversed first:
      .sortWith { case (childStruct, s2) =>
        if (childStruct.isLeaf) false
        else if (s2.isLeaf) true
        else {
          val cv = conf.getValue(childStruct.name)
          val comments = cv.origin().comments().asScala.toList
          comments.exists(_.trim.startsWith("@define"))
        }
      }

    val members: immutable.Map[String, model.AnnType] = memberStructs.map { childStruct =>
      val name = childStruct.name
      val cv = conf.getValue(name)

      val (childType, optional, default) = {
        if (childStruct.isLeaf) {
          val valueString = util.escapeValue(cv.unwrapped().toString)

          getTypeFromConfigValue(namespace, cv, valueString) match {
            case typ: model.STRING.type =>
              namespace.resolveDefine(valueString) match {
                case Some(ort) =>
                  (ort, false, None)

                case None =>
                  toAnnBasicType(valueString) match {
                    case Some(annBasicType) =>
                      annBasicType

                    case None =>
                      (typ, true, Some(valueString))
                  }
              }

            case typ: model.BasicType =>
              (typ, true, Some(valueString))

            case typ =>
              (typ, false, None)
          }
        }
        else {
          (fromConfig(namespace.extend(name), conf.getConfig(name)), false, None)
        }
      }

      val comments = cv.origin().comments().asScala.toList
      val optFromComments = comments.exists(_.trim.startsWith("@optional"))
      val commentsOpt = if (comments.isEmpty) None else Some(comments.mkString("\n"))

      // per Lightbend Config restrictions involving $, leave the key alone if
      // contains $, otherwise unquote the key in case is quoted.
      val adjName = if (name.contains("$")) name else name.replaceAll("^\"|\"$", "")

      // effective optional and default:
      val (effOptional, effDefault) = if (assumeAllRequired)
        (false, None) // that is, all strictly required.
      // A possible variation: allow the `@optional` annotation to still take effect:
      //(optFromComments, if (optFromComments) default else None)
      else
      (optional || optFromComments, default)

      //println(s"ModelBuilder: effOptional=$effOptional  effDefault=$effDefault " +
      //  s"assumeAllRequired=$assumeAllRequired optFromComments=$optFromComments " +
      //  s"adjName=$adjName")

      /* get the parent class members, if any*/
      val parentClassMembers = this.parentClassMembers(commentsOpt, namespace)

      /* build the annType  */
      val annType = buildAnnType(childType, effOptional, effDefault, commentsOpt, parentClassMembers)

      if (annType.isDefine) {
        namespace.addDefine(name, childType, annType.isParent)
      }

      adjName -> annType

    }.toMap

    model.ObjectType(members)
  }

  private def buildAnnType(childType: model.Type, effOptional:Boolean, effDefault:Option[String],
                           commentsOpt:Option[String],
                           parentClassMembers:Option[Predef.Map[String, model.AnnType]]):AnnType ={

    // if this class is a parent class (abstract class or interface) this is indicated by the childType object
    // that is passed into the AnnType instance that is returned
    val updatedChildType = childType match {
      case objType: ObjectType =>
        if(commentsOpt.exists(AnnType.isParent))
          AbstractObjectType(objType.members) else objType
      case other => other
    }

    model.AnnType(
      updatedChildType,
      optional = effOptional,
      default = effDefault,
      comments = commentsOpt,
      parentClassMembers = parentClassMembers
    )
  }

  private def parentClassMembers(commentsOpt: Option[String], namespace: Namespace):
  Option[Predef.Map[String, model.AnnType]] = {

    AnnType.parentClassName(commentsOpt).flatMap(
      parentName => {
        // sanity check to see if this class is defined as parent class
        if (namespace.isParent(parentName)) {
          // valid parent name
          namespace.getDefine(parentName).map {
            case objType: ObjectType =>
              objType.members
            case _ => Map.empty[String, model.AnnType]
          }
        } else {
          // parent class might be defined, but not as parent -> error // todo handling JH
          None
        }
      })
  }

  private case class Struct(name: String, members: mutable.HashMap[String, Struct] = mutable.HashMap.empty) {
    def isLeaf: Boolean = members.isEmpty

    // $COVERAGE-OFF$
    def format(indent: String = ""): String = {
      if (members.isEmpty)
        name
      else
        s"$name:\n" +
          members.map(e => indent + e._1 + ": " + e._2.format(indent + "    ")).mkString("\n")
    }

    // $COVERAGE-ON$
  }

  private def getMemberStructs(conf: Config): List[Struct] = {
    val structs = mutable.HashMap[String, Struct]("" -> Struct(""))

    def resolve(key: String): Struct = {
      if (!structs.contains(key)) structs.put(key, Struct(getSimple(key)))
      structs(key)
    }

    conf.entrySet().asScala foreach { e =>
      val path = e.getKey
      val leaf = Struct(path)
      doAncestorsOf(path, leaf)

      def doAncestorsOf(childKey: String, childStruct: Struct): Unit = {
        val (parent, simple) = (getParent(childKey), getSimple(childKey))
        createParent(parent, simple, childStruct)

        @annotation.tailrec
        def createParent(parentKey: String, simple: String, child: Struct): Unit = {
          val parentGroup = resolve(parentKey)
          parentGroup.members.put(simple, child)
          if (parentKey != "") {
            createParent(getParent(parentKey), getSimple(parentKey), parentGroup)
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

    structs("").members.values.toList
  }

  private def getTypeFromConfigValue(namespace: Namespace, cv: ConfigValue, valueString: String): model.Type = {
    import ConfigValueType._
    cv.valueType() match {
      case STRING => model.STRING
      case BOOLEAN => model.BOOLEAN
      case NUMBER => numberType(cv.unwrapped().toString)
      case LIST => listType(namespace, cv.asInstanceOf[ConfigList])
      case OBJECT =>
        objType(namespace, cv.asInstanceOf[ConfigObject])
      case NULL => throw new AssertionError("null unexpected")
    }
  }

  private def toAnnBasicType(valueString: String):
  Option[(model.BasicType, Boolean, Option[String])] = {

    if (tsConfigUtil.isDurationValue(valueString))
      return Some((DURATION(ms), true, Some(valueString)))

    if (tsConfigUtil.isSizeValue(valueString))
      return Some((SIZE, true, Some(valueString)))

    val tokens = valueString.split("""\s*\|\s*""")
    val typePart = tokens(0).toLowerCase
    val hasDefault = tokens.size == 2
    val defaultValue = if (hasDefault) Some(tokens(1)) else None

    val (baseString, isOpt) = if (typePart.endsWith("?"))
      (typePart.substring(0, typePart.length - 1), true)
    else
      (typePart, hasDefault)

    val (base, qualification) = {
      val parts = baseString.split("""\s*\:\s*""", 2)
      if (parts.length == 1)
        (parts(0), None)
      else
        (parts(0), Some(parts(1)))
    }

    model.recognizedAtomic.get(base) map { bt =>
      val basicType = bt match {
        case DURATION(_) if qualification.isDefined =>
          DURATION(tsConfigUtil.unifyDuration(qualification.get))
        case _ => bt
      }
      (basicType, isOpt, defaultValue)
    }
  }

  private def listType(namespace: Namespace, cv: ConfigList): model.ListType = {
    if (cv.isEmpty) throw new IllegalArgumentException("list with one element expected")

    if (cv.size() > 1) {
      val line = cv.origin().lineNumber()
      val options: ConfigRenderOptions = ConfigRenderOptions.defaults
        .setFormatted(false).setComments(false).setOriginComments(false)
      warns += MultElemListWarning(line, cv.render(options))
    }

    val cv0: ConfigValue = cv.get(0)
    val valueString = util.escapeValue(cv0.unwrapped().toString)
    val typ = getTypeFromConfigValue(namespace, cv0, valueString)

    val elemType = {
      if (typ == model.STRING) {

        namespace.resolveDefine(valueString) match {
          case Some(ort) =>
            ort

          case None =>
            // see possible type from the string literal:
            toAnnBasicType(valueString) match {
              case Some((basicType, isOpt, defaultValue)) =>
                if (isOpt)
                  warns += OptListElemWarning(cv0.origin().lineNumber(), valueString)

                if (defaultValue.isDefined)
                  warns += DefaultListElemWarning(cv0.origin().lineNumber(), defaultValue.get, valueString)

                basicType

              case None =>
                typ
            }
        }
      }
      else typ
    }

    model.ListType(elemType)
  }

  private def objType(namespace: Namespace, cv: ConfigObject): model.ObjectType =
    fromConfig(namespace, cv.toConfig)

  private def numberType(valueString: String): model.BasicType = {
    try {
      valueString.toInt
      model.INTEGER
    }
    catch {
      case _: NumberFormatException =>
        try {
          valueString.toLong
          model.LONG
        }
        catch {
          case _: NumberFormatException =>
            try {
              valueString.toDouble
              model.DOUBLE
            }
            catch {
              case _: NumberFormatException => throw new AssertionError()
            }
        }
    }
  }
}

object ModelBuilder {

  import java.io.File

  import com.typesafe.config.ConfigFactory

  def apply(source: String, assumeAllRequired: Boolean = false): ModelBuildResult = {
    val config = ConfigFactory.parseString(source).resolve()
    new ModelBuilder(assumeAllRequired).build(config)
  }

  // $COVERAGE-OFF$
  def main(args: Array[String]): Unit = {
    val filename = args(0)
    val file = new File(filename)
    val source = io.Source.fromFile(file).mkString.trim
    //println("source:\n  |" + source.replaceAll("\n", "\n  |"))
    val result = ModelBuilder(source)
    println("objectType:")
    println(model.util.format(result.objectType))
  }

  // $COVERAGE-ON$
}
