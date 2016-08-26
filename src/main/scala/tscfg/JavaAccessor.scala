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
      case "duration"  =>
        if (required) GetDuration(baseType)
        else if (value.isDefined) GetDurationOr(baseType, value.get)
        else GetDurationOrNull(baseType)

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
    def instance(path: String) = s"""c != null && c.$hasPath("$path") ? c.getString("$path") : null"""
  }
  case class GetStringOr(value: String)(implicit genOpts: GenOpts) extends StringAccessor with HasPath {
    def instance(path: String) = s"""c != null && c.$hasPath("$path") ? c.getString("$path") : $value"""
  }

  case class GetInt()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "int"
    def instance(path: String) = s"""c.getInt("$path")"""
  }
  case class GetIntOrNull()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Integer"
    def instance(path: String) = s"""c != null && c.$hasPath("$path") ? Integer.valueOf(c.getInt("$path")) : null"""
  }
  case class GetIntOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "int"
    def instance(path: String) = s"""c != null && c.$hasPath("$path") ? c.getInt("$path") : $value"""
  }

  case class GetLong()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "long"
    def instance(path: String) = s"""c.getLong("$path")"""
  }
  case class GetLongOrNull()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Long"
    def instance(path: String) = s"""c != null && c.$hasPath("$path") ? Long.valueOf(c.getLong("$path")) : null"""
  }
  case class GetLongOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "long"
    def instance(path: String) = s"""c != null && c.$hasPath("$path") ? c.getLong("$path") : $value"""
  }

  case class GetDouble()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "double"
    def instance(path: String) = s"""c.getDouble("$path")"""
  }
  case class GetDoubleOrNull()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Double"
    def instance(path: String) = s"""c != null && c.$hasPath("$path") ? Double.valueOf(c.getDouble("$path")) : null"""
  }
  case class GetDoubleOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "double"
    def instance(path: String) = s"""c != null && c.$hasPath("$path") ? c.getDouble("$path") : $value"""
  }

  case class GetBoolean()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "boolean"
    def instance(path: String) = s"""c.getBoolean("$path")"""
  }
  case class GetBooleanOrNull()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Boolean"
    def instance(path: String) = s"""c != null && c.$hasPath("$path") ? Boolean.valueOf(c.getBoolean("$path")) : null"""
  }
  case class GetBooleanOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "boolean"
    def instance(path: String) = s"""c != null && c.$hasPath("$path") ? c.getBoolean("$path") : $value"""
  }

  case class GetDuration(baseType: BaseType)(implicit genOpts: GenOpts) extends DurationAccessor {
    def `type` = "long"
    def instance(path: String) = durationGetter(path, baseType)
  }
  case class GetDurationOrNull(baseType: BaseType)(implicit genOpts: GenOpts) extends DurationAccessor with HasPath {
    def `type` = "Long"
    def instance(path: String) = s"""c != null && c.$hasPath("$path") ? ${durationGetter(path, baseType)} : null"""
  }
  case class GetDurationOr(baseType: BaseType, value: String)(implicit genOpts: GenOpts) extends DurationAccessor with HasPath {
    def `type` = "long"
    def instance(path: String) = s"""c != null && c.$hasPath("$path") ? ${durationGetter(path, baseType)} : ${durationValue(value, baseType)}"""
  }

}
