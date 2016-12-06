package tscfg.generators

import tscfg.model.ObjectType

/**
  * Base generation class.
  *
  * @param genOpts    Generation options
  */
abstract class Gen(genOpts: GenOpts) {

  def generate(objectType: ObjectType): GenResult

  protected val className: String = genOpts.className
  protected val hasPath: String = if (genOpts.j7) "hasPath" else "hasPathOrNull"
  protected var genResults = GenResult()
}
