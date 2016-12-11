package tscfg.generators

import tscfg.model.ObjectType

/**
  * Base generation class.
  *
  * @param genOpts    Generation options
  */
abstract class Generator(genOpts: GenOpts) {

  def generate(objectType: ObjectType): GenResult

  protected val className: String = genOpts.className
  protected val hasPath: String = if (genOpts.j7) "hasPath" else "hasPathOrNull"
  protected var genResults = GenResult()
}

/**
  * Generation options
  *
  * @param packageName  package name
  * @param className    class name
  * @param j7           true to generate code for Typesafe Config v &lt;= 1.2.1
  */
case class GenOpts(packageName: String  = defaults.packageName,
                   className: String    = defaults.className,
                   j7: Boolean          = false
                  )

case class GenResult(code: String = "?",
                     classNames: Set[String] = Set(),
                     fieldNames: Set[String] = Set())


object defaults {
  val packageName = "tscfg.example"
  val className = "ExampleCfg"
}
