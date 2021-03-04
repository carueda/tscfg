package tscfg.generators

import tscfg.model._
import tscfg.util.escapeString
import tscfg.model.durations._
import _root_.scala.util.control.NonFatal
import _root_.java.util.concurrent.TimeUnit

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Some typesafeConfig-related utilities for both java and scala generation.
  */
object tsConfigUtil {

  def basicGetter(bt: BasicType, path: String, useDurations:Boolean): String = bt match {
    case STRING    =>  s"""getString("$path")"""
    case INTEGER   =>  s"""getInt("$path")"""
    case LONG      =>  s"""getLong("$path")"""
    case DOUBLE    =>  s"""getDouble("$path")"""
    case BOOLEAN   =>  s"""getBoolean("$path")"""
    case SIZE      =>  s"""getBytes("$path")"""
    case DURATION(q)  =>  durationGetter(path, q, useDurations)
  }

  def basicRequiredGetter(bt: BasicType, path: String, useDurations:Boolean): (String,String) = {
    val methodName = bt match {
      case STRING    =>   "$_reqStr"
      case INTEGER   =>   "$_reqInt"
      case LONG      =>   "$_reqLng"
      case DOUBLE    =>   "$_reqDbl"
      case BOOLEAN   =>   "$_reqBln"
      case SIZE      =>   "$_reqSiz"
      // $COVERAGE-OFF$
      case _ => throw new AssertionError("should not happen")
      // $COVERAGE-ON$
    }
    (methodName, s"""$methodName(parentPath, c, "$path", $$tsCfgValidator)""")
  }

  def basicValue(t: Type, value: String, useDurations: Boolean): String = t match {
    case SIZE        => sizeValue(value)
    case DURATION(q) => durationValue(value, q, useDurations)
    case STRING      => s""""${escapeString(value)}""""
    case _           => value
  }

  // https://github.com/typesafehub/config/blob/master/HOCON.md#duration-format
  def unifyDuration(q: String): DurationQualification = q match {
    case "ns" | "nano"   | "nanos"  | "nanosecond"  | "nanoseconds"   => ns
    case "us" | "micro"  | "micros" | "microsecond" | "microseconds"  => us
    case "ms" | "milli"  | "millis" | "millisecond" | "milliseconds"  => ms
    case "s"  | "second" | "seconds"                                  => second
    case "m"  | "minute" | "minutes"                                  => minute
    case "h"  | "hour"   | "hours"                                    => hour
    case "d"  | "day"    | "days"                                     => day

    case _ => throw new AssertionError("unrecognized q='" + q + "'")
  }

  def isDurationValue(value: String): Boolean = {
    try {
      val config: Config = ConfigFactory.parseString(s"""k = "$value"""")
      config.getDuration("k")
      true
    }
    catch {
      case NonFatal(_) => false
    }
  }

  private def durationValue(value: String, q: DurationQualification, useDurations: Boolean): String = {
    val config: Config = ConfigFactory.parseString(s"""k = "$value"""")
    if(useDurations)
      config.getDuration("k").toString
    else
      config.getDuration("k", timeUnitParam(q)).toString
  }

  private def durationGetter(path: String, q: DurationQualification, useDurations: Boolean): String = {
    if(useDurations)
      s"""getDuration("$path")"""
    else
      s"""getDuration("$path", ${timeUnitParamString(q)})"""
  }

  private def timeUnitParamString(q: DurationQualification): String =
    "java.util.concurrent.TimeUnit." + (q match {
      case `ns`     =>  "NANOSECONDS"
      case `us`     =>  "MICROSECONDS"
      case `ms`     =>  "MILLISECONDS"
      case `second` =>  "SECONDS"
      case `minute` =>  "MINUTES"
      case `hour`   =>  "HOURS"
      case `day`    =>  "DAYS"
    })

  private def timeUnitParam(q: DurationQualification): TimeUnit = q match {
    case `ns`     =>  TimeUnit.NANOSECONDS
    case `us`     =>  TimeUnit.MICROSECONDS
    case `ms`     =>  TimeUnit.MILLISECONDS
    case `second` =>  TimeUnit.SECONDS
    case `minute` =>  TimeUnit.MINUTES
    case `hour`   =>  TimeUnit.HOURS
    case `day`    =>  TimeUnit.DAYS
  }

  private def sizeValue(value: String): String = {
    val config: Config = ConfigFactory.parseString(s"""s = "$value"""")
    s"""${config.getBytes("s")}L"""
  }
}
