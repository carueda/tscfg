package tscfg

import com.typesafe.config.{Config, ConfigFactory}
import tscfg.generator.GenOpts

object JavaAccessor {

  def apply(type_ : Type)(implicit genOpts: GenOpts): Accessor = {
    val baseType = type_.baseType
    val required = type_.required
    val value = type_.value

    baseType.base match {
      case "string"    => if (required) GetString() else if (value.isDefined) GetStringOr(value.get) else GetStringOrNull()
      case "int"       => if (required) GetInt()    else if (value.isDefined) GetIntOr(value.get) else GetIntOrNull()
      case "long"      => if (required) GetLong()   else if (value.isDefined) GetLongOr(value.get) else GetLongOrNull()
      case "double"    => if (required) GetDouble() else if (value.isDefined) GetDoubleOr(value.get) else GetDoubleOrNull()
      case "boolean"   => if (required) GetBoolean()else if (value.isDefined) GetBooleanOr(value.get) else GetBooleanOrNull()
      case "duration"  => if (required) GetDuration(baseType) else if (value.isDefined) GetDurationOr(baseType, value.get) else GetDurationOrNull(baseType)

      case _ => throw new AssertionError()
    }
  }

  abstract class StringAccessor()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "String"
  }

  case class GetString()(implicit genOpts: GenOpts) extends StringAccessor {
    def instance(path: String) = s"""c.getString("$path")"""
  }
  case class GetStringOrNull()(implicit genOpts: GenOpts) extends StringAccessor with HasPath {
    def instance(path: String) = s"""c.$hasPath("$path") ? c.getString("$path") : null"""
  }
  case class GetStringOr(value: String)(implicit genOpts: GenOpts) extends StringAccessor with HasPath {
    def instance(path: String) = s"""c.$hasPath("$path") ? c.getString("$path") : $value"""
  }

  case class GetInt()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "int"
    def instance(path: String) = s"""c.getInt("$path")"""
  }
  case class GetIntOrNull()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Integer"
    def instance(path: String) = s"""c.$hasPath("$path") ? Integer.valueOf(c.getInt("$path")) : null"""
  }
  case class GetIntOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "int"
    def instance(path: String) = s"""c.$hasPath("$path") ? c.getInt("$path") : $value"""
  }

  case class GetLong()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "long"
    def instance(path: String) = s"""c.getLong("$path")"""
  }
  case class GetLongOrNull()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Long"
    def instance(path: String) = s"""c.$hasPath("$path") ? Long.valueOf(c.getLong("$path")) : null"""
  }
  case class GetLongOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "long"
    def instance(path: String) = s"""c.$hasPath("$path") ? c.getLong("$path") : $value"""
  }

  case class GetDouble()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "double"
    def instance(path: String) = s"""c.getDouble("$path")"""
  }
  case class GetDoubleOrNull()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Double"
    def instance(path: String) = s"""c.$hasPath("$path") ? Double.valueOf(c.getDouble("$path")) : null"""
  }
  case class GetDoubleOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "double"
    def instance(path: String) = s"""c.$hasPath("$path") ? c.getDouble("$path") : $value"""
  }

  case class GetBoolean()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "boolean"
    def instance(path: String) = s"""c.getBoolean("$path")"""
  }
  case class GetBooleanOrNull()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Boolean"
    def instance(path: String) = s"""c.$hasPath("$path") ? Boolean.valueOf(c.getBoolean("$path")) : null"""
  }
  case class GetBooleanOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "boolean"
    def instance(path: String) = s"""c.$hasPath("$path") ? c.getBoolean("$path") : $value"""
  }

  abstract class DurationAccessor extends Accessor {
    def timeUnitParamString(q: String): String = "java.util.concurrent.TimeUnit." + (q match {
      case "nanosecond"  => "NANOSECONDS"
      case "microsecond" => "MICROSECONDS"
      case "millisecond" => "MILLISECONDS"
      case "second"      => "SECONDS"
      case "minute"      => "MINUTES"
      case "hour"        => "HOURS"
      case "day"         => "DAYS"
      case _ => throw new AssertionError("unrecognized q='" + q + "'")
    })

    def getter(path: String, baseType: BaseType): String = {
      val q = baseType.qualification.getOrElse("millisecond")
      s"""c.getDuration("$path", ${timeUnitParamString(q)})"""
    }

    def timeUnitParam(q: String): java.util.concurrent.TimeUnit = q match {
      case "nanosecond"  => java.util.concurrent.TimeUnit.NANOSECONDS
      case "microsecond" => java.util.concurrent.TimeUnit.MICROSECONDS
      case "millisecond" => java.util.concurrent.TimeUnit.MILLISECONDS
      case "second"      => java.util.concurrent.TimeUnit.SECONDS
      case "minute"      => java.util.concurrent.TimeUnit.MINUTES
      case "hour"        => java.util.concurrent.TimeUnit.HOURS
      case "day"         => java.util.concurrent.TimeUnit.DAYS
      case _ => throw new AssertionError("unrecognized q='" + q + "'")
    }

    def convertedDefaultValue(value: String, baseType: BaseType): Long = {
      val q = baseType.qualification.getOrElse("millisecond")
      val config: Config = ConfigFactory.parseString(s"""k = "$value"""")
      config.getDuration("k", timeUnitParam(q))
    }
  }

  case class GetDuration(baseType: BaseType)(implicit genOpts: GenOpts) extends DurationAccessor {
    def `type` = "long"
    def instance(path: String) = getter(path, baseType)
  }
  case class GetDurationOrNull(baseType: BaseType)(implicit genOpts: GenOpts) extends DurationAccessor with HasPath {
    def `type` = "Long"
    def instance(path: String) = s"""c.$hasPath("$path") ? ${getter(path, baseType)} : null"""
  }
  case class GetDurationOr(baseType: BaseType, value: String)(implicit genOpts: GenOpts) extends DurationAccessor with HasPath {
    def `type` = "long"
    def instance(path: String) = s"""c.$hasPath("$path") ? ${getter(path, baseType)} : ${convertedDefaultValue(value, baseType)}"""
  }

}
