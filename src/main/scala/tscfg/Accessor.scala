package tscfg

import tscfg.generator.GenOpts


trait HasPath {
  def hasPath()(implicit genOpts: GenOpts) = if (genOpts.j7) "hasPath" else "hasPathOrNull"
}

abstract class Accessor {
  def `type`: String
  def instance(path: String): String
}


object Accessor {
  /**
    * Returns accessor for the spec and target language.
    */
  def apply(spec: String)(implicit genOpts: GenOpts): Accessor = {
    genOpts.language match {
      case "java" => JavaAccessor(spec)
      case "scala" => ScalaAccessor(spec)
    }
  }
}
