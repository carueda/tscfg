package tscfg

import com.typesafe.config._
import tscfg.specs._
import tscfg.specs.types.AtomicType

import scala.annotation.tailrec
import scala.collection.JavaConversions._

class SpecBuilder(rootKey: Key) {
  import collection._

  def fromConfig(conf: Config): ObjSpec = {
    fromConfig(conf, rootKey.simple)
  }

  private def fromConfig(conf: Config, pushedKey: String): ObjSpec = {
    val memberStructs = getMemberStructs(conf)
    getObjSpec(pushedKey, conf, memberStructs)
  }

  private def getObjSpec(key: String, conf: Config, memberStructs: List[Struct]): ObjSpec = {
    val children: immutable.Map[String, Spec] = memberStructs.map { childStruct ⇒
      val childSpec = getSpec(conf, childStruct)
      childStruct.key.simple -> childSpec
    }.toMap
    ObjSpec(Key(key), children, asScalaBuffer(conf.origin().comments()).toList)
  }

  private def getSpec(conf: Config, struct: Struct): Spec = {
    val cv = conf.getValue(struct.key.simple)
    val members = struct.members
    if (members.isEmpty) {
      fromConfigValue(cv, struct.key.simple)
    }
    else {
      val c = cv.asInstanceOf[ConfigObject].toConfig
      getObjSpec(struct.key.simple, c, struct.members.values.toList)
    }
  }

  private case class Struct(key: Key, members: mutable.HashMap[String, Struct] = mutable.HashMap.empty) {
    // $COVERAGE-OFF$
    def format(indent: String = ""): String = {
      if (members.isEmpty) s"$key"
      else {
        s"$key:\n" +
          members.map(e ⇒ indent + e._1 + ": " + e._2.format(indent + "    ")).mkString("\n")
      }
    }
    // $COVERAGE-ON$
  }

  private def getMemberStructs(conf: Config): List[Struct] = {
    val structs = mutable.HashMap[Key, Struct](Key.root → Struct(Key.root))
    def resolve(key: Key): Struct = {
      if (!structs.contains(key)) structs.put(key, Struct(key))
      structs(key)
    }

    conf.entrySet() foreach { e ⇒
      val path = e.getKey
      //println(s" === path=$path")
      val key = Key(path)
      val leaf = Struct(key)
      doAncestorsOf(key, leaf)

      def doAncestorsOf(childKey: Key, childStruct: Struct): Unit = {
        //println(s"doAncestorsOf: childKey=$childKey")
        createParent(childKey.parent, childKey.simple, childStruct)

        @tailrec
        def createParent(parentKey: Key, simple: String, child: Struct): Unit = {
          //println(s"createParent: parentKey=$parentKey")

          val parentGroup = resolve(parentKey)
          parentGroup.members.put(simple, child)

          if (parentKey != Key.root) {
            createParent(parentKey.parent, parentKey.simple, parentGroup)
          }
        }
      }
    }
    structs(Key.root).members.values.toList
  }

  private def fromConfigValue(cv: ConfigValue, pushedKey: String): Spec = {
    val comments = asScalaBuffer(cv.origin().comments()).toList
    import ConfigValueType._
    cv.valueType() match {
      case STRING  => atomicSpec(cv)
      case BOOLEAN => AtomicSpec(types.BOOLEAN, comments)
      case NUMBER  => AtomicSpec(numberType(cv), comments)
      case LIST    => listSpec(cv.asInstanceOf[ConfigList], pushedKey)
      case OBJECT  => objSpec(cv.asInstanceOf[ConfigObject], pushedKey)
      case NULL    => throw new AssertionError("null unexpected")
    }
  }

  private def atomicSpec(cv: ConfigValue): AtomicSpec = {
    val comments = asScalaBuffer(cv.origin().comments()).toList
    val valueString = cv.unwrapped().toString.toLowerCase

    val tokens = valueString.split("""\s*\|\s*""")
    val typePart = tokens(0).toLowerCase
    val hasDefault = tokens.size == 2
    val defaultValue = if (hasDefault) Some(tokens(1)) else None

    val (baseString, isOpt) = if (typePart.endsWith("?"))
      (typePart.substring(0, typePart.length - 1), true)
    else
      (typePart, false)

    val (baseForAtomicType, qualification) = {
      val parts = baseString.split("""\s*\:\s*""", 2)
      if (parts.length == 1)
        (parts(0), None)
      else
        (parts(0), Some(parts(1)))
    }

    val atomicType = types.recognizedAtomic.getOrElse(baseForAtomicType, {
      import ConfigValueType._
      cv.valueType() match {
        case STRING  => types.STRING
        case BOOLEAN => types.BOOLEAN
        case NUMBER  => numberType(cv)
        case _  => throw new AssertionError()
      }
    })

    AtomicSpec(atomicType, comments, isOpt, defaultValue, qualification)
  }

  private def listSpec(cv: ConfigList, pushedKey: String): ListSpec = {
    if (cv.isEmpty) throw new IllegalArgumentException("list with one element expected")

    if (cv.size() > 1) {
      val line = cv.origin().lineNumber()
      val options: ConfigRenderOptions = ConfigRenderOptions.defaults
        .setFormatted(false).setComments(false).setOriginComments(false)
      println(s"$line: ${cv.render(options)}: WARN: only first element will be considered")
    }

    val comments = asScalaBuffer(cv.origin().comments()).toList
    val elemPushedName = listElementClassName(pushedKey)

    ListSpec(fromConfigValue(cv.get(0), elemPushedName), comments)
  }

  // if needed, creates a new name to distinguish multiple list element classes
  private def listElementClassName(pushedKey: String): String = {
    if (pushedKey.endsWith("$Elm")) pushedKey else {
      val name = if (nextElementCounter > 0) "$" + nextElementCounter + "$Elm" else "$Elm"
      nextElementCounter += 1
      pushedKey + name
    }
  }

  private var nextElementCounter: Int = 0

  private def objSpec(cv: ConfigObject, pushedKey: String): Spec =
    fromConfig(cv.toConfig, pushedKey)

  private def numberType(cv: ConfigValue): AtomicType = {
    val valueString = cv.unwrapped().toString
    try {
      valueString.toInt
      types.INTEGER
    }
    catch {
      case e:NumberFormatException =>
        try {
          valueString.toLong
          types.LONG
        }
        catch {
          case e:NumberFormatException =>
            try {
              valueString.toDouble
              types.DOUBLE
            }
            catch {
              case e:NumberFormatException => throw new AssertionError()
            }
        }
    }
  }
}

object SpecBuilder {
  import java.io.File
  import com.typesafe.config.ConfigFactory

  // $COVERAGE-OFF$
  def main(args: Array[String]): Unit = {
    val filename = args(0)
    val file = new File(filename)
    val src = io.Source.fromFile(file).mkString.trim
    println("src:\n  |" + src.replaceAll("\n", "\n  |"))
    val config = ConfigFactory.parseString(src).resolve()

    val className = "Scala" + {
      val noPath = filename.substring(filename.lastIndexOf('/') + 1)
      val noDef = noPath.replaceAll("""^def\.""", "")
      val symbol = noDef.substring(0, noDef.indexOf('.'))
      symbol.charAt(0).toUpper + symbol.substring(1) + "Cfg"
    }

    val objSpec = new SpecBuilder(Key(className)).fromConfig(config, className)
    println(s"objSpec: ${objSpec.format()}")
  }
  // $COVERAGE-ON$
}
