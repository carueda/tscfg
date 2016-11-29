package tscfg.generators

import _root_.java.util.concurrent.TimeUnit

import com.typesafe.config.{Config, ConfigFactory}
import tscfg.specs.AtomicSpec


object durationUtil {

  def getter(path: String, spec: AtomicSpec): String = {
    val q = spec.qualification.getOrElse("millisecond")
    s"""getDuration("$path", ${timeUnitParamString(q)})"""
  }

  def durationValue(value: String, spec: AtomicSpec): Long = {
    val q = spec.qualification.getOrElse("millisecond")
    val config: Config = ConfigFactory.parseString(s"""k = "$value"""")
    config.getDuration("k", timeUnitParam(q))
  }

  private def timeUnitParamString(q: String): String = "java.util.concurrent.TimeUnit." + (q match {
    case "nanosecond"  => "NANOSECONDS"
    case "microsecond" => "MICROSECONDS"
    case "millisecond" | "ms" => "MILLISECONDS"
    case "second"      => "SECONDS"
    case "minute"      => "MINUTES"
    case "hour"        => "HOURS"
    case "day"         => "DAYS"
    case _ => throw new AssertionError("unrecognized q='" + q + "'")
  })

  private def timeUnitParam(q: String): TimeUnit = {
    q match {
      case "nanosecond"  => TimeUnit.NANOSECONDS
      case "microsecond" => TimeUnit.MICROSECONDS
      case "millisecond" | "ms" => TimeUnit.MILLISECONDS
      case "second"      => TimeUnit.SECONDS
      case "minute"      => TimeUnit.MINUTES
      case "hour"        => TimeUnit.HOURS
      case "day"         => TimeUnit.DAYS
      case _ => throw new AssertionError("unrecognized q='" + q + "'")
    }
  }
}
