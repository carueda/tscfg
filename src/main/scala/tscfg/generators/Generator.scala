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
case class GenOpts(packageName: String,
                   className: String,
                   j7: Boolean,
                   reportFullPath: Boolean = false,
                   useBackticks: Boolean = false,
                   genGetters: Boolean = false,
                   useOptionals: Boolean = false
                  )

case class GenResult(code: String = "?",
                     classNames: Set[String] = Set(),
                     fields: Map[String, String] = Map.empty,
                     getters: Map[String, String] = Map.empty
                    )
