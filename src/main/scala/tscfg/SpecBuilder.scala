package tscfg

import java.io.File

import com.typesafe.config._
import tscfg.generator.GenOpts
import tscfg.generators.{Generator, JavaGenerator}
import tscfg.specs._

import scala.annotation.tailrec
import scala.collection.JavaConversions._

object SpecBuilder {
  import collection._

  def fromConfig(conf: Config): ObjSpec = {

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
        val leaf = Leaf(key, fromConfigValue(value))
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
    val root = getRootGroup
    //println("root group:"); pprint.log(root)
    def getSpec(struct: Struct): Spec = struct match {
      case Leaf(_, spec) ⇒ spec

      case Group(members) ⇒
        val children: immutable.Map[String, Spec] = members.map { case (childSimple, childStruct) ⇒
            childSimple -> getSpec(childStruct)
        }.toMap
        ObjSpec(children)
    }

    getSpec(root).asInstanceOf[ObjSpec]
  }

  private def fromConfigValue(cv: ConfigValue): Spec = {
    import ConfigValueType._
    cv.valueType() match {
      case STRING | BOOLEAN => atomicSpec(cv)
      case NUMBER  => numberSpec(cv)
      case LIST    => listSpec(cv.asInstanceOf[ConfigList])
      case OBJECT  => objSpec(cv.asInstanceOf[ConfigObject])
      case NULL    => throw new AssertionError("null unexpected")
    }
  }

  private def atomicSpec(cv: ConfigValue): Spec = {
    val valueString = cv.unwrapped().toString.toLowerCase
    //println(s"atomicSpec: valueString=$valueString")
    atomics.recognized.getOrElse(valueString, {
      import ConfigValueType._
      cv.valueType() match {
        case STRING  => atomics.stringSpec
        case BOOLEAN => atomics.booleanSpec
        case NUMBER  => numberSpec(cv)
        case _  => throw new AssertionError()
      }
    })
  }

  private def listSpec(cv: ConfigList): ListSpec = {
    if (cv.isEmpty) throw new IllegalArgumentException("list with one element expected")

    if (cv.size() > 1) {
      val line = cv.origin().lineNumber()
      val options: ConfigRenderOptions = ConfigRenderOptions.defaults
        .setFormatted(false).setComments(false).setOriginComments(false)
      println(s"$line: ${cv.render(options)}: WARN: only first element will be considered")
    }

    ListSpec(fromConfigValue(cv.get(0)))
  }

  private def objSpec(cv: ConfigObject): Spec = fromConfig(cv.toConfig)

  private def numberSpec(cv: ConfigValue): Spec = {
    val valueString = cv.unwrapped().toString
    try {
      valueString.toInt
      atomics.integerSpec
    }
    catch {
      case e:NumberFormatException =>
        try {
          valueString.toLong
          atomics.longSpec
        }
        catch {
          case e:NumberFormatException =>
            try {
              valueString.toDouble
              atomics.doubleSpec
            }
            catch {
              case e:NumberFormatException => throw new AssertionError()
            }
        }
    }
  }

  def main(args: Array[String]): Unit = {
    val filename = if (args.isEmpty) "example/issue15.conf" else args(0)
    val file = new File(filename)
    val src = io.Source.fromFile(file).mkString.trim
    println("src:\n  |" + src.replaceAll("\n", "\n  |"))
    val config = ConfigFactory.parseString(src).resolve()

    val objSpec = fromConfig(config)
    println("\nobjSpec:\n  |" + objSpec.format().replaceAll("\n", "\n  |"))

    implicit val genOpts = GenOpts("pkg", "issue15",
      preamble = Some(s"source: (a test)")
    )

    val generator: Generator = new JavaGenerator

    val results = generator.generate(objSpec)

    println("\n" + results.code)
  }
}
