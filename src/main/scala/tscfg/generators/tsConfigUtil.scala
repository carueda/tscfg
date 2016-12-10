package tscfg.generators

import tscfg.model._
import tscfg.model.durations._

import _root_.java.util.concurrent.TimeUnit

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Some typesafeConfig-related utilities for both java and scala generation.
  */
object tsConfigUtil {

  def basicGetter(bt: BasicType, path: String): String = bt match {
    case STRING    ⇒  s"""getString("$path")"""
    case INTEGER   ⇒  s"""getInt("$path")"""
    case LONG      ⇒  s"""getLong("$path")"""
    case DOUBLE    ⇒  s"""getDouble("$path")"""
    case BOOLEAN   ⇒  s"""getBoolean("$path")"""
    case DURATION(q)  ⇒  durationGetter(path, q)
  }

  def basicValue(t: Type, value: String): String = t match {
    case DURATION(q) ⇒ durationValue(value, q)
    case STRING   ⇒ '"' + value.replaceAll("\\\"", "\\\\\"") + '"'
    case _        ⇒ value
  }

  // https://github.com/typesafehub/config/blob/master/HOCON.md#duration-format
  def unify(q: String): Qualification = q match {
    case "ns" | "nano"   | "nanos"  | "nanosecond"  | "nanoseconds"   => NANOSECONDS
    case "us" | "micro"  | "micros" | "microsecond" | "microseconds"  => MICROSECONDS
    case "ms" | "milli"  | "millis" | "millisecond" | "milliseconds"  => MILLISECONDS
    case "s"  | "second" | "seconds"                                  => SECONDS
    case "m"  | "minute" | "minutes"                                  => MINUTES
    case "h"  | "hour"   | "hours"                                    => HOURS
    case "d"  | "day"    | "days"                                     => DAYS

    case _ => throw new AssertionError("unrecognized q='" + q + "'")
  }
  private def durationValue(value: String, q: Qualification): String = {
    val config: Config = ConfigFactory.parseString(s"""k = "$value"""")
    config.getDuration("k", timeUnitParam(q)).toString
  }

  private def durationGetter(path: String, q: Qualification): String = {
    s"""getDuration("$path", ${timeUnitParamString(q)})"""
  }

  private def timeUnitParamString(q: Qualification): String =
    "java.util.concurrent.TimeUnit." + q.toString

  private def timeUnitParam(q: Qualification): TimeUnit = {
    q match {
      case NANOSECONDS   =>  TimeUnit.NANOSECONDS
      case MICROSECONDS  =>  TimeUnit.MICROSECONDS
      case MILLISECONDS  =>  TimeUnit.MILLISECONDS
      case SECONDS       =>  TimeUnit.SECONDS
      case MINUTES       =>  TimeUnit.MINUTES
      case HOURS         =>  TimeUnit.HOURS
      case DAYS          =>  TimeUnit.DAYS
    }
  }
}
