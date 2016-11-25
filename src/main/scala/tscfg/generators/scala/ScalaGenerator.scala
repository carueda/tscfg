package tscfg.generators.scala

import java.io.{FileWriter, PrintWriter}

import tscfg.generator._
import tscfg.generators.Generator
import tscfg.scalaUtil._
import tscfg.specs._
import tscfg.specs.types._
import tscfg.util

import scala.annotation.tailrec

class ScalaGenerator(genOpts: GenOpts) extends Generator {

  val hasPath = if (genOpts.j7) "hasPath" else "hasPathOrNull"

  def generate(objSpec: ObjSpec): GenResult = {

    var results = GenResult()

    def genForObjSpec(objSpec: ObjSpec, indent: String, isRoot: Boolean = false): Code = {
      var comma = ""

      val className = getClassName(objSpec.name)

      val orderedNames = objSpec.orderedNames
      val padScalaIdLength = if (orderedNames.nonEmpty)
        orderedNames.map(scalaIdentifier).maxBy(_.length).length else 0
      def padScalaId(id: String) = id + (" " * (padScalaIdLength - id.length))

      results = results.copy(classNames = results.classNames + className)

      val code = Code(objSpec, className)

      // <object>
      code.println(indent + s"object $className {")

      // <recurse>
      orderedNames foreach { name =>
        objSpec.children(name) match {
          case o: ObjSpec ⇒
            val c = genCode(o, indent + IND)
            code.println(c.definition)
          case _ ⇒
        }
      }
      // </recurse>

      // <apply>
      // #13: add `build` auxiliary method to avoid type erasure issue in case of optional member
      code.println(s"$indent  def apply(c: com.typesafe.config.Config) = build(Some(c))")
      code.println(s"$indent  def apply() = build(None)")
      code.println(s"$indent  def build(c: scala.Option[${util.TypesafeConfigClassName}]): $className = {")
      code.println(s"$indent    $className(")


      comma = indent
      orderedNames foreach { name =>
        code.print(comma)
        objSpec.children(name) match {
          case a:AtomicSpec ⇒
            code.print(s"""      ${instance(code, a, a.name)}""")

          case o:ObjSpec ⇒
            val className = getClassName(o.name)
            code.print(s"""      $className.build(c.map(c => if (c.hasPath("${o.name}")) Some(c.getConfig("${o.name}")) else None).get)""")

          case l:ListSpec ⇒
            val className = getClassName(l.name)
            code.print(s"""      TODO_access_list_$className.build(c.map(c => if (c.hasPath("${l.name}")) Some(c.getConfig("${l.name}")) else None).get)""")
        }
        comma = s",\n$indent"
      }
      code.println("")
      code.println(s"$indent    )")
      code.println(s"$indent  }")
      // </apply>

      code.println(s"$indent}")
      // </object>



      // <class>
      results = results.copy(classNames = results.classNames + className)
      code.println(s"${indent}case class $className(")
      comma = ""
      orderedNames foreach { name =>
        val scalaId = scalaIdentifier(name)
        results = results.copy(fieldNames = results.fieldNames + scalaId)
        code.print(comma)
        code.print(s"$indent  ${padScalaId(scalaId)} : ")  // note, space before : for proper tokenization
        objSpec.children(name) match {
          case a:AtomicSpec ⇒
            code.print(getScalaType(a))

          case o:ObjSpec ⇒
            // TODO use full qualified class name
            val className = genOpts.className + "..." + o.name
            code.print(s"""$className""")

          case l:ListSpec ⇒
            code.print(s"""      ${getScalaType(l)}""")
        }
        comma = ",\n"
      }

      code.print(s"\n$indent)")

      // toString...
      code.println("")
      // </class>

      code
    }

    def genForAtomicSpec(spec: AtomicSpec): Code = {
      Code(spec, getScalaType(spec))
    }

    def genForListSpec(listSpec: ListSpec, indent: String): Code = {
      @tailrec
      def listNesting(ls: ListSpec, levels: Int): (Spec, Int) = ls.elemSpec match {
        case subListSpec: ListSpec ⇒ listNesting(subListSpec, levels + 1)
        case nonListSpec           ⇒ (nonListSpec, levels)
      }

      val (elemSpec, levels) = listNesting(listSpec, 1)

      val elemCode = genCode(elemSpec, indent)
      val elemObjType = getScalaType(elemCode.spec)
      val scalaType = ("scala.collection.immutable.List[" * levels) + elemObjType + ("]" * levels)
      val code = Code(listSpec, scalaType)

      if (elemCode.definition.nonEmpty) code.println(elemCode.definition)

      code
    }

    def genCode(spec: Spec, indent: String = ""): Code = spec match {
      case spec: AtomicSpec    ⇒ genForAtomicSpec(spec)
      case spec: ObjSpec       ⇒ genForObjSpec(spec, indent)
      case spec: ListSpec      ⇒ genForListSpec(spec, indent)
    }

    val header = new StringBuilder()
    //header.append(s"// generated by tscfg $version on ${new Date()}\n")
//    genOpts.preamble foreach { p =>
//      header.append(s"// ${p.replace("\n", "\n// ")}\n\n")
//    }
    header.append(s"package ${genOpts.packageName}\n\n")

    // main class:
    val elemSpec = genForObjSpec(objSpec, "", isRoot = true)

    results = results.copy(code = header.toString() + elemSpec.definition)

    results
  }

  /**
    * Captures code associated with a spec.
    */
  private case class Code(spec: Spec, scalaType: String) {

    def println(str: String): Unit = defn.append(str).append('\n')

    def print(str: String): Unit = defn.append(str)

    def definition = defn.toString

    private val defn = new StringBuilder()
  }

  private val IND = "  "

  private def getScalaType(spec: Spec): String = {
    spec match {
      case a: AtomicSpec ⇒
        a.typ match {
            case STRING   ⇒ if (spec.isOptional) "scala.Option[java.lang.String]" else "java.lang.String"
            case INTEGER  ⇒ if (spec.isOptional) "scala.Option[scala.Int]"     else "scala.Int"
            case LONG     ⇒ if (spec.isOptional) "scala.Option[scala.Long]"    else "scala.Long"
            case DOUBLE   ⇒ if (spec.isOptional) "scala.Option[scala.Double]"  else "scala.Double"
            case BOOLEAN  ⇒ if (spec.isOptional) "scala.Option[scala.Boolean]" else "scala.Boolean"
            case DURATION ⇒ if (spec.isOptional) "scala.Option[scala.Long]"    else "scala.Long"
        }

      case o: ObjSpec  ⇒ getClassName(o.name)

      case l: ListSpec  ⇒
        s"scala.collection.immutable.List[${getScalaType(l.elemSpec)}]"
      }
  }

  private def instance(objCode: Code, spec: Spec, path: String): String = {
    spec match {
      case a: AtomicSpec ⇒
        a.typ match {
          case STRING ⇒
            if (spec.defaultValue.isDefined) {
              val value = spec.defaultValue.get
              s"""c.map(c => if(c.$hasPath("$path")) c.getString("$path") else $value).get"""
            }
            else if (spec.isOptional) {
              s"""c.map(c => if(c.$hasPath("$path")) Some(c.getString("$path")) else None).get"""
            }
            else
              s"""c.get.getString("$path")"""

          case INTEGER ⇒
            if (spec.defaultValue.isDefined) {
              val value = spec.defaultValue.get
              s"""c.map(c => if(c.$hasPath("$path")) c.getInt("$path") else $value).get"""
            }
            else if (spec.isOptional) {
              s"""c.map(c => if(c.$hasPath("$path")) Some(c.getInt("$path")) else None).get"""
            }
            else
              s"""c.get.getInt("$path")"""

          case LONG ⇒
            if (spec.defaultValue.isDefined) {
              val value = spec.defaultValue.get
              s"""c.map(c => if(c.$hasPath("$path")) c.getLong("$path") else $value).get"""
            }
            else if (spec.isOptional) {
              s"""c.map(c => if(c.$hasPath("$path")) Some(c.getLong("$path")) else None).get"""
            }
            else
              s"""c.get.getLong("$path")"""

          case DOUBLE ⇒
            if (spec.defaultValue.isDefined) {
              val value = spec.defaultValue.get
              s"""c.map(c => if(c.$hasPath("$path")) c.getDouble("$path") else $value).get"""
            }
            else if (spec.isOptional) {
              s"""c.map(c => if(c.$hasPath("$path")) Some(c.getDouble("$path")) else None).get"""
            }
            else
              s"""c.get.getDouble("$path")"""

          case BOOLEAN ⇒
            if (spec.defaultValue.isDefined) {
              val value = spec.defaultValue.get
              s"""c.map(c => if(c.$hasPath("$path")) c.getBoolean("$path") else $value).get"""
            }
            else if (spec.isOptional) {
              s"""c.map(c => if(c.$hasPath("$path")) Some(c.getBoolean("$path")) else None).get"""
            }
            else
              s"""c.get.getBoolean("$path")"""


          case DURATION ⇒ s"""TODO_getDuration("$path")"""
        }

      case o: ObjSpec  ⇒
        val className = getClassName(o.name)
        s"""      $className.build(c.map(c => if (c.hasPath("${o.name}")) Some(c.getConfig("${o.name}")) else None).get)"""

      case l: ListSpec  ⇒
        s"""TODO_instance_list(c.getList("$path"))"""
    }
  }
}

object ScalaGenerator {
  import java.io.File

  import com.typesafe.config.ConfigFactory
  import tscfg.SpecBuilder

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

    val objSpec = SpecBuilder.fromConfig(config, className)
    println("\nobjSpec:\n  |" + objSpec.format().replaceAll("\n", "\n  |"))

    val genOpts = GenOpts("tscfg.example", className,
      preamble = Some(s"source: (a test)")
    )

    val generator: Generator = new ScalaGenerator(genOpts)

    val results = generator.generate(objSpec)

    //println("\n" + results.code)

    val destFilename  = s"src/main/scala/tscfg/example/$className.scala"
    val destFile = new File(destFilename)
    val out = new PrintWriter(new FileWriter(destFile), true)
    out.println(results.code)
  }
}
