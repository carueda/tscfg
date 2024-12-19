package tscfg

import com.typesafe.config._
import tscfg.generators.tsConfigUtil
import tscfg.model._
import tscfg.model.durations.ms
import tscfg.ns.{Namespace, NamespaceMan}

import scala.jdk.CollectionConverters._

class ModelBuilder(
    rootNamespace: NamespaceMan,
    assumeAllRequired: Boolean = false
) {

  import buildWarnings._

  import collection._

  def build(conf: Config): ModelBuildResult = {
    warns.clear()
    ModelBuildResult(
      objectType = fromConfig(rootNamespace.root, conf),
      warnings = warns.toList.sortBy(_.line)
    )
  }

  private val warns = collection.mutable.ArrayBuffer[Warning]()

  /** Gets the [[model.ObjectType]] corresponding to the given TS Config object.
    */
  private def fromConfig(
      namespace: Namespace,
      conf: Config,
  ): model.ObjectType = {
    val struct = Struct.getStruct(conf)

    // A single pass may not be sufficient to populate the namespace and get complete
    // name resolutions, in particular, when a name (referring to a `@define`) is
    // within some container type (like `[SomeDef]`). The effect is that `SomeDef`
    // would be considered a string instead of resolving to the definition.
    // An overall redesign (and much more elegant implementation) is needed,
    // but, for now, let's do two passes as a quick trick:
    fromStruct(namespace, struct, firstPass = true)
    // avoid spurious duplicate warnings for the next pass:
    val definedNames = namespace.getAllDefines.keySet
    scribe.debug(s"definedNames = ${pprint.apply(definedNames)}")
    namespace.setOkDuplicates(definedNames)
    fromStruct(namespace, struct, firstPass = false)
  }

  /** Gets the [[model.ObjectType]] corresponding to the given Struct. */
  private def fromStruct(
      namespace: Namespace,
      struct: Struct,
      firstPass: Boolean,
  ): model.ObjectType = {
    val memberStructs = struct.members.values.toList
    // Note: the returned order of this list is assumed to have taken into account any dependencies between
    // the structs, in terms both of inheritance and member types.
    // TODO a future revision may lessen this requirement by making the `namespace.resolveDefine` call below
    //  and associated handling more flexible.

    val members: immutable.Map[String, model.AnnType] = memberStructs.map {
      childStruct =>
        val name = childStruct.name

        val (childType, optional, default) = {
          if (childStruct.isLeaf) {
            fromLeafStruct(namespace, childStruct)
          }
          else {
            (
              fromStruct(namespace.extend(name), childStruct, firstPass),
              false,
              None
            )
          }
        }

        val comments        = childStruct.comments
        val optFromComments = comments.exists(_.trim.startsWith("@optional"))

        // per Lightbend Config restrictions involving $, leave the key alone if
        // contains $, otherwise unquote the key in case is quoted.
        val adjName =
          if (name.contains("$")) name else name.replaceAll("^\"|\"$", "")

        // effective optional and default:
        val (effOptional, effDefault) =
          if (assumeAllRequired)
            (false, None) // that is, all strictly required.
          // A possible variation: allow the `@optional` annotation to still take effect:
          // (optFromComments, if (optFromComments) default else None)
          else
            (optional || optFromComments, default)

        /* Get a comprehensive view of members from _all_ ancestors */
        val parentClassMembers =
          Struct.ancestorClassMembers(
            childStruct,
            memberStructs,
            namespace,
            firstPass,
          )

        /* build the annType  */
        val annType = buildAnnType(
          childType,
          effOptional,
          effDefault,
          childStruct.defineCaseOpt,
          docComments = childStruct.docComments,
          comments = comments,
          parentClassMembers,
        )

        annType.defineCase foreach { namespace.addDefine(name, annType.t, _) }

        adjName -> annType

    }.toMap

    /* filter abstract members from root object as they don't require an instantiation */
    model.ObjectType(
      members.filterNot(fullPathWithObj =>
        fullPathWithObj._2.default.exists(namespace.isAbstractClassDefine)
      )
    )
  }

  private def fromLeafStruct(
      namespace: Namespace,
      struct: Struct,
  ): (Type, Boolean, Option[String]) = {
    val cv          = struct.cv
    val valueString = tscfg.util.escapeValue(cv.unwrapped().toString)
    val isEnum      = struct.isEnum

    getTypeFromConfigValue(namespace, cv, isEnum) match {
      case typ: model.STRING.type =>
        namespace.resolveDefine(valueString) match {
          case Some(ort) =>
            (ort, false, None)

          case None =>
            inferAnnBasicTypeFromString(valueString) match {
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

  private def buildAnnType(
      childType: model.Type,
      effOptional: Boolean,
      effDefault: Option[String],
      defineCase: Option[DefineCase],
      docComments: List[String],
      comments: List[String],
      parentClassMembers: Option[Map[String, model.AnnType]]
  ): AnnType = {

    // if this class is a parent class (abstract class or interface) this is indicated by the childType object
    // that is passed into the AnnType instance that is returned

    // TODO review the following
    val updatedChildType = childType match {
      case objType: ObjectType =>
        if (comments.exists(AnnType.isAbstract))
          AbstractObjectType(objType.members)
        else objType

      case listType: ListType =>
        listType

      case other => other
    }

    model.AnnType(
      updatedChildType,
      optional = effOptional,
      default = effDefault,
      defineCase = defineCase,
      docComments = docComments,
      comments = comments,
      parentClassMembers = parentClassMembers.map(_.toMap),
    )
  }

  private def getTypeFromConfigValue(
      namespace: Namespace,
      cv: ConfigValue,
      isEnum: Boolean
  ): model.Type = {
    import ConfigValueType._
    cv.valueType() match {
      case STRING => model.STRING

      case BOOLEAN => model.BOOLEAN

      case NUMBER => numberType(cv.unwrapped().toString)

      case LIST if isEnum => enumType(cv.asInstanceOf[ConfigList])

      case LIST => listType(namespace, cv.asInstanceOf[ConfigList])

      case OBJECT => objType(namespace, cv.asInstanceOf[ConfigObject])

      case NULL => throw new AssertionError("null unexpected")
    }
  }

  private def inferAnnBasicTypeFromString(
      valueString: String
  ): Option[(model.BasicType, Boolean, Option[String])] = {

    if (tsConfigUtil.isDurationValue(valueString))
      return Some((DURATION(ms), true, Some(valueString)))

    val tokens       = valueString.split("""\s*\|\s*""")
    val typePart     = tokens(0).toLowerCase
    val hasDefault   = tokens.size == 2
    val defaultValue = if (hasDefault) Some(tokens(1)) else None

    val (baseString, isOpt) =
      if (typePart.endsWith("?"))
        (typePart.substring(0, typePart.length - 1), true)
      else
        (typePart, hasDefault)

    val (base, qualification) = {
      val parts = baseString.split("""\s*:\s*""", 2)
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
    if (cv.isEmpty)
      throw new IllegalArgumentException("list with one element expected")

    if (cv.size() > 1) {
      val line = cv.origin().lineNumber()
      val options: ConfigRenderOptions = ConfigRenderOptions.defaults
        .setFormatted(false)
        .setComments(false)
        .setOriginComments(false)
      warns += MultElemListWarning(line, cv.render(options))
    }

    val cv0: ConfigValue = cv.get(0)
    val valueString      = tscfg.util.escapeValue(cv0.unwrapped().toString)
    val typ = getTypeFromConfigValue(namespace, cv0, isEnum = false)

    val elemType = {
      if (typ == model.STRING) {

        namespace.resolveDefine(valueString) match {
          case Some(ort) =>
            ort

          case None =>
            // see possible type from the string literal:
            inferAnnBasicTypeFromString(valueString) match {
              case Some((basicType, isOpt, defaultValue)) =>
                if (isOpt)
                  warns += OptListElemWarning(
                    cv0.origin().lineNumber(),
                    valueString
                  )

                if (defaultValue.isDefined)
                  warns += DefaultListElemWarning(
                    cv0.origin().lineNumber(),
                    defaultValue.get,
                    valueString
                  )

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

  private def enumType(cv: ConfigList): model.EnumObjectType = {
    if (cv.isEmpty)
      throw new IllegalArgumentException(
        "enumeration with at least one element expected"
      )

    model.EnumObjectType(
      cv.iterator().asScala.map(_.unwrapped().toString).toList
    )
  }

  private def objType(
      namespace: Namespace,
      cv: ConfigObject
  ): model.ObjectType =
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

  import com.typesafe.config.ConfigFactory

  import java.io.File

  /** build model from string */
  def apply(
      rootNamespace: NamespaceMan,
      source: String,
      assumeAllRequired: Boolean = false
  ): ModelBuildResult = {
    val config = ConfigFactory.parseString(source).resolve()
    fromConfig(rootNamespace, config, assumeAllRequired)
  }

  /** build model from TS Config object */
  private def fromConfig(
      rootNamespace: NamespaceMan,
      config: Config,
      assumeAllRequired: Boolean = false
  ): ModelBuildResult = {
    new ModelBuilder(rootNamespace, assumeAllRequired).build(config)
  }

  // $COVERAGE-OFF$
  def main(args: Array[String]): Unit = {
    tscfg.util.setLogMinLevel()

    val filename =
      args.headOption.getOrElse("src/main/tscfg/example/example.conf")
    val showTsConfig = args.length > 1 && "-ts" == args(1)
    val file         = new File(filename)
    val bufSource    = io.Source.fromFile(file)
    val source       = bufSource.mkString.trim
    bufSource.close()
    // println("source:\n  |" + source.replaceAll("\n", "\n  |"))
    val config = ConfigFactory.parseString(source).resolve()
    if (showTsConfig) {
      val options = ConfigRenderOptions.defaults
        .setFormatted(true)
        .setComments(true)
        .setOriginComments(false)
      println(config.root.render(options))
    }
    val rootNamespace = new NamespaceMan
    val result        = fromConfig(rootNamespace, config)
    println(
      s"""ModelBuilderResult:
         |${model.util.format(result.objectType)}
         |""".stripMargin
    )
  }

  // $COVERAGE-ON$
}
