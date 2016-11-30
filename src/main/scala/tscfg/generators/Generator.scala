package tscfg.generators

import tscfg.specs.ObjSpec

/**
  * Base generation class.
  *
  * @param genOpts    Generation options
  */
abstract class Generator(genOpts: GenOpts) {

  def generate(objSpec: ObjSpec): GenResult

  protected val hasPath: String = if (genOpts.j7) "hasPath" else "hasPathOrNull"

}

/**
  * Generation options
  *
  * @param packageName  package name
  * @param className    class name
  * @param j7           true to generate code for Typesafe Config v &lt;= 1.2.1
  * @param preamble     preamble to include in generated code
  * @param language     target language, default "java"
  */
case class GenOpts(packageName: String         = defaults.packageName,
                   className: String           = defaults.className,
                   j7: Boolean                 = false,
                   preamble: Option[String]    = None,
                   language: String            = "java"
                  )

case class GenResult(code: String = "?",
                     classNames: Set[String] = Set(),
                     fieldNames: Set[String] = Set())


object defaults {
  val packageName = "tscfg.example"
  val className = "ExampleCfg"
}
