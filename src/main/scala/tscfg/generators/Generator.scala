package tscfg.generators

import tscfg.model.ObjectType

/** Base generation class.
  *
  * @param genOpts
  *   Generation options
  */
abstract class Generator(genOpts: GenOpts) {

  def generate(objectType: ObjectType): GenResult

  protected val className: String     = genOpts.className
  protected val hasPath: String       = "hasPathOrNull"
  protected var genResults: GenResult = GenResult()

  // allows to insert special marks in generated code to facilitate debugging.
  // Keep this as the empty string constant in version control.
  protected def dbg(s: String, in: Boolean = true): String =
    ""
  // if (in) s" /*$s*/ " else s" // $s"
}

/** Generation options
  */
case class GenOpts(
    packageName: String,
    className: String,
    genDoc: Boolean = true,
    assumeAllRequired: Boolean = false,
    useBackticks: Boolean = false,
    genGetters: Boolean = false,
    genRecords: Boolean = false,
    useOptionals: Boolean = false,
    useDurations: Boolean = false
)

case class GenResult(
    code: String = "?",
    classNames: Set[String] = Set(),
    fields: Map[String, String] = Map.empty,
    getters: Map[String, String] = Map.empty
)
