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

  protected def dbg(s: String, in: Boolean = true): String =
    ""
    //if (in) s" /*$s*/ " else s" // $s"
}

/**
  * Generation options
  */
case class GenOpts(packageName: String,
                   className: String,
                   assumeAllRequired: Boolean = false,
                   j7: Boolean = false,
                   useBackticks: Boolean = false,
                   genGetters: Boolean = false,
                   useOptionals: Boolean = false,
                   useDurations: Boolean = false
                  )

case class GenResult(code: String = "?",
                     classNames: Set[String] = Set(),
                     fields: Map[String, String] = Map.empty,
                     getters: Map[String, String] = Map.empty
                    )
