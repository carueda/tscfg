package tscfg

import com.typesafe.config.ConfigValue
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
  def apply(type_ : Type)(implicit genOpts: GenOpts): Accessor = {
    genOpts.language match {
      case "java" => JavaAccessor(type_)
      case "scala" => ScalaAccessor(type_)
      case lang => throw new IllegalArgumentException(s"unrecognized language: '$lang'")
    }
  }
}
