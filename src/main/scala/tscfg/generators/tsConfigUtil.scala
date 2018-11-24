package tscfg.generators

import tscfg.model._
import tscfg.util.escapeString
import _root_.scala.util.control.NonFatal

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
    case SIZE      ⇒  s"""getBytes("$path")"""
    case DURATION  ⇒  durationGetter(path)
  }

  def basicValue(t: Type, value: String): String = t match {
    case SIZE     ⇒ sizeValue(value)
    case DURATION ⇒ durationValue(value)
    case STRING   ⇒ '"' + escapeString(value) + '"'
    case _        ⇒ value
  }

  def isDurationValue(value: String): Boolean = {
    val config: Config = ConfigFactory.parseString(s"""k = "$value"""")
    try {
      config.getDuration("k")
      true
    }
    catch {
      case NonFatal(_) ⇒ false
    }
  }

  private def durationValue(value: String): String = {
    val config: Config = ConfigFactory.parseString(s"""k = "$value"""")
    config.getDuration("k").toString
  }

  private def durationGetter(path: String): String = {
    s"""getDuration("$path")"""
  }

  def isSizeValue(value: String): Boolean = {
    val config: Config = ConfigFactory.parseString(s"""s = "$value"""")
    try {
      config.getBytes("s")
      true
    }
    catch {
      case NonFatal(_) ⇒ false
    }
  }

  private def sizeValue(value: String): String = {
    val config: Config = ConfigFactory.parseString(s"""s = "$value"""")
    config.getBytes("s").toString
  }
}
