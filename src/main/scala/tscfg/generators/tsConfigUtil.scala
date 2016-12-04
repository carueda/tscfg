package tscfg.generators

import tscfg.model._
import _root_.java.util.concurrent.TimeUnit

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Some typesafeConfig-related utilities for both java and scala generation.
  */
object tsConfigUtil {

  def basicGetter(bt: BasicType, qualification: Option[String], path: String): String = bt match {
    case STRING    ⇒  s"""getString("$path")"""
    case INTEGER   ⇒  s"""getInt("$path")"""
    case LONG      ⇒  s"""getLong("$path")"""
    case DOUBLE    ⇒  s"""getDouble("$path")"""
    case BOOLEAN   ⇒  s"""getBoolean("$path")"""
    case DURATION  ⇒  durationGetter(path, qualification)
  }

  def basicValue(t: Type, value: String, qualification: Option[String]): String = t match {
    case DURATION ⇒ durationValue(value, qualification)
    case STRING   ⇒ '"' + value.replaceAll("\\\"", "\\\\\"") + '"'
    case _        ⇒ value
  }

  private def durationValue(value: String, qualification: Option[String]): String = {
    val q = qualification.getOrElse("millisecond")
    val config: Config = ConfigFactory.parseString(s"""k = "$value"""")
    config.getDuration("k", timeUnitParam(q)).toString
  }

  private def durationGetter(path: String, qualification: Option[String]): String = {
    val q = qualification.getOrElse("millisecond")
    s"""getDuration("$path", ${timeUnitParamString(q)})"""
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
