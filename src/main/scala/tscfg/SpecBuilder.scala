package tscfg

import com.typesafe.config._
import tscfg.specs._
import tscfg.specs.types.AtomicType

import scala.annotation.tailrec
import scala.collection.JavaConversions._

object SpecBuilder {
  import collection._

  def fromConfig(conf: Config, pushedName: String): ObjSpec = {

    // 1- get a struct representation from conf.entrySet()
    abstract class Struct
    case class Leaf(key: Key, spec: Spec) extends Struct
    case class Group(members: mutable.HashMap[String, Struct] = mutable.HashMap.empty) extends Struct

    def getRootGroup: Group = {

      val groups = mutable.HashMap[Key, Group](Key.root → Group())

      def getGroup(key: Key): Group = {
        if (!groups.contains(key)) groups.put(key, Group())
        groups(key)
      }

      conf.entrySet() foreach { e ⇒
        val (path, value) = (e.getKey, e.getValue)
        val key = Key(path)
        val leaf = Leaf(key, fromConfigValue(value, key.simple))
        doAncestorsOf(key, leaf)

        def doAncestorsOf(childKey: Key, childStruct: Struct): Unit = {
          createParent(childKey.parent, childKey.simple, childStruct)

          @tailrec
          def createParent(parentKey: Key, simple: String, child: Struct): Unit = {
            val parentGroup = getGroup(parentKey)
            parentGroup.members.put(simple, child)

            if (parentKey != Key.root) {
              createParent(parentKey.parent, parentKey.simple, parentGroup)
            }
          }
        }
      }
      groups(Key.root)
    }

    // 2- now build and return the corresponding root Spec:
    def getSpec(name: String, struct: Struct): Spec = struct match {
      case Leaf(_, spec) ⇒ spec

      case Group(members) ⇒
        val children: immutable.Map[String, Spec] = members.map { case (childSimple, childStruct) ⇒
            childSimple -> getSpec(childSimple, childStruct)
        }.toMap
        ObjSpec(name, children)
    }

    val root = getRootGroup
    //println("root group:"); pprint.log(root)

    getSpec(pushedName, root).asInstanceOf[ObjSpec]
  }

  private def fromConfigValue(cv: ConfigValue, pushedName: String): Spec = {
    import ConfigValueType._
    cv.valueType() match {
      case STRING  => atomicSpec(cv, pushedName)
      case BOOLEAN => AtomicSpec(pushedName, types.BOOLEAN)
      case NUMBER  => AtomicSpec(pushedName, numberType(cv))
      case LIST    => listSpec(cv.asInstanceOf[ConfigList], pushedName)
      case OBJECT  => objSpec(cv.asInstanceOf[ConfigObject], pushedName)
      case NULL    => throw new AssertionError("null unexpected")
    }
  }

  private def atomicSpec(cv: ConfigValue, pushedName: String): AtomicSpec = {
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

    AtomicSpec(pushedName, atomicType, isOpt, defaultValue, qualification)
  }

  private def listSpec(cv: ConfigList, pushedName: String): ListSpec = {
    if (cv.isEmpty) throw new IllegalArgumentException("list with one element expected")

    if (cv.size() > 1) {
      val line = cv.origin().lineNumber()
      val options: ConfigRenderOptions = ConfigRenderOptions.defaults
        .setFormatted(false).setComments(false).setOriginComments(false)
      println(s"$line: ${cv.render(options)}: WARN: only first element will be considered")
    }

    // push list element class name (a new one unless one is already being pushed):
    val elemPushedName = if (pushedName.endsWith("$Elm_")) pushedName else newListElementClassName

    ListSpec(pushedName,
      fromConfigValue(cv.get(0), elemPushedName))
  }

  /**
    * New name makes easier to distinguish multiple list element classes
    */
  private def newListElementClassName: String = {
    val name = "_$" + nextElementCounter + "$Elm_"
    nextElementCounter += 1
    name
  }
  var nextElementCounter: Int = 0

  private def objSpec(cv: ConfigObject, pushedName: String): Spec = fromConfig(cv.toConfig, pushedName)

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
