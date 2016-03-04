package tscfg

import java.io.{PrintWriter, Writer}

import com.typesafe.config.ConfigFactory
import tscfg.nodes._


object generator {
  val version = ConfigFactory.load().getString("tscfg.version")

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

  case class GenResult(classNames: Set[String] = Set(),
                       fieldNames: Set[String] = Set())

  /**
    * Generates code for the given configuration tree.
    *
    * @param node         Root of the tree to process
    * @param out          code is written here
    * @param genOpts      generation options
    */
  def generate(node: Node, out: Writer)
              (implicit genOpts: GenOpts): GenResult = {

    val pw = out match {
      case w: PrintWriter => w
      case w => new PrintWriter(w)
    }

    genOpts.language match {
      case "java"  => javaGenerator.generate(node, pw)
      case "scala" => scalaGenerator.generate(node, pw)
    }
  }

}
