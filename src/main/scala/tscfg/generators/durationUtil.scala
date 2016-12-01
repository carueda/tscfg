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

  private def timeUnitParamString(q: String): String = "java.util.concurrent.TimeUnit." + unify(q)

  private def timeUnitParam(q: String): TimeUnit = {
    unify(q) match {
      case "NANOSECONDS"   =>  TimeUnit.NANOSECONDS
      case "MICROSECONDS"  =>  TimeUnit.MICROSECONDS
      case "MILLISECONDS"  =>  TimeUnit.MILLISECONDS
      case "SECONDS"       =>  TimeUnit.SECONDS
      case "MINUTES"       =>  TimeUnit.MINUTES
      case "HOURS"         =>  TimeUnit.HOURS
      case "DAYS"          =>  TimeUnit.DAYS

      case _ => throw new AssertionError("unrecognized q='" + q + "'")
    }
  }

  // https://github.com/typesafehub/config/blob/master/HOCON.md#duration-format
  private def unify(q: String): String = q match {
    case "ns" | "nano"   | "nanos"  | "nanosecond"  | "nanoseconds"   => "NANOSECONDS"
    case "us" | "micro"  | "micros" | "microsecond" | "microseconds"  => "MICROSECONDS"
    case "ms" | "milli"  | "millis" | "millisecond" | "milliseconds"  => "MILLISECONDS"
    case "s"  | "second" | "seconds"                                  => "SECONDS"
    case "m"  | "minute" | "minutes"                                  => "MINUTES"
    case "h"  | "hour"   | "hours"                                    => "HOURS"
    case "d"  | "day"    | "days"                                     => "DAYS"

    case _ => throw new AssertionError("unrecognized q='" + q + "'")
  }

}
