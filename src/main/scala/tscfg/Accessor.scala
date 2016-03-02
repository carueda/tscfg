package tscfg

import com.typesafe.config.{ConfigFactory, Config}
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

abstract class DurationAccessor extends Accessor {
  def durationGetter(path: String, baseType: BaseType): String = {
    val q = baseType.qualification.getOrElse("millisecond")
    s"""c.getDuration("$path", ${timeUnitParamString(q)})"""
  }

  def durationValue(value: String, baseType: BaseType): Long = {
    val q = baseType.qualification.getOrElse("millisecond")
    val config: Config = ConfigFactory.parseString(s"""k = "$value"""")
    config.getDuration("k", timeUnitParam(q))
  }

  private def timeUnitParamString(q: String): String = "java.util.concurrent.TimeUnit." + (q match {
    case "nanosecond"  => "NANOSECONDS"
    case "microsecond" => "MICROSECONDS"
    case "millisecond" => "MILLISECONDS"
    case "second"      => "SECONDS"
    case "minute"      => "MINUTES"
    case "hour"        => "HOURS"
    case "day"         => "DAYS"
    case _ => throw new AssertionError("unrecognized q='" + q + "'")
  })

  private def timeUnitParam(q: String): java.util.concurrent.TimeUnit = q match {
    case "nanosecond"  => java.util.concurrent.TimeUnit.NANOSECONDS
    case "microsecond" => java.util.concurrent.TimeUnit.MICROSECONDS
    case "millisecond" => java.util.concurrent.TimeUnit.MILLISECONDS
    case "second"      => java.util.concurrent.TimeUnit.SECONDS
    case "minute"      => java.util.concurrent.TimeUnit.MINUTES
    case "hour"        => java.util.concurrent.TimeUnit.HOURS
    case "day"         => java.util.concurrent.TimeUnit.DAYS
    case _ => throw new AssertionError("unrecognized q='" + q + "'")
  }
}
