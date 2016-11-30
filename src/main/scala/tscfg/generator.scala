package tscfg

import java.io.{PrintWriter, Writer}

import com.typesafe.config.ConfigFactory
import tscfg.generators.Generator
import tscfg.generators.java.JavaGenerator
import tscfg.generators.scala.ScalaGenerator
import tscfg.specs.ObjSpec


object generator {
  val version: String = ConfigFactory.load().getString("tscfg.version")

  val defaultPackageName = "tscfg.example"
  val defaultClassName   = "ExampleCfg"

  /**
    * Generation options
    *
    * @param packageName  package name
    * @param className    class name
    * @param j7           true to generate code for Typesafe Config v &lt;= 1.2.1
    * @param preamble     preamble to include in generated code
    * @param language     target language, default "java"
    */
  case class GenOpts(packageName: String = defaultPackageName,
                     className: String   = defaultClassName,
                     j7: Boolean         = false,
                     preamble: Option[String] = None,
                     language: String    = "java"
                    )

  case class GenResult(code: String = "?",
                       classNames: Set[String] = Set(),
                       fieldNames: Set[String] = Set())

  /**
    * Generates code for the given configuration spec.
    *
    * @param objSpec      specification
    * @param out          code is written here
    * @param genOpts      generation options
    */
  def generate(objSpec: ObjSpec, out: Writer)
              (implicit genOpts: GenOpts): GenResult = {

    val pw = out match {
      case w: PrintWriter => w
      case w => new PrintWriter(w)
    }

    val generator: Generator = genOpts.language match {
      case "java"  => new JavaGenerator(genOpts)
      case "scala" => new ScalaGenerator(genOpts)
    }

    val results = generator.generate(objSpec)
    pw.println(results.code)
    results
  }
}
