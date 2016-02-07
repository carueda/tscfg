package tscfg

import com.typesafe.config.{ConfigValueType, ConfigValue}
import tscfg.generator.GenOpts

object JavaAccessor {

  def apply(value: ConfigValue)(implicit genOpts: GenOpts): Accessor = {
    val valueString = value.unwrapped().toString
    val tokens = valueString.split("""\s*\|\s*""")
    val isOr = tokens.size == 2

    tokens(0).toLowerCase match {
      case "string"    => if (isOr) GetStringOr(tokens(1)) else GetString()
      case "string?"   => if (isOr) GetStringOr(tokens(1)) else GetStringOrNull()

      case "int"       => if (isOr) GetIntOr(tokens(1))    else GetInt()
      case "int?"      => if (isOr) GetIntOr(tokens(1))    else GetIntOrNull()

      case "long"      => if (isOr) GetLongOr(tokens(1))    else GetLong()
      case "long?"     => if (isOr) GetLongOr(tokens(1))    else GetLongOrNull()

      case "double"    => if (isOr) GetDoubleOr(tokens(1)) else GetDouble()
      case "double?"   => if (isOr) GetDoubleOr(tokens(1)) else GetDoubleOrNull()

      case "boolean"   => if (isOr) GetBooleanOr(tokens(1)) else GetBoolean()
      case "boolean?"  => if (isOr) GetBooleanOr(tokens(1)) else GetBooleanOrNull()

      case _           => inferType(value, valueString)
    }
  }

  private def inferType(value: ConfigValue, valueString : String)
                       (implicit genOpts: GenOpts): Accessor = {

    def numberAccessor: Accessor = {
      try {
        valueString.toInt
        GetInt()
      }
      catch {
        case e:NumberFormatException =>
          try {
            valueString.toLong
            GetLong()
          }
          catch {
            case e:NumberFormatException =>
              try {
                valueString.toDouble
                GetDouble()
              }
              catch {
                case e:NumberFormatException => throw new AssertionError()
              }
          }
      }
    }

    value.valueType() match {
      case ConfigValueType.STRING  => GetString()
      case ConfigValueType.BOOLEAN => GetBoolean()
      case ConfigValueType.NUMBER  => numberAccessor
      case ConfigValueType.LIST    => throw new IllegalArgumentException("list not implemented yet")
      case ConfigValueType.OBJECT  => throw new AssertionError("object unexpected")
      case ConfigValueType.NULL    => throw new AssertionError("null unexpected")
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
    def instance(path: String) = s"""c.$hasPath("$path") ? c.getString("$path") : "$value""""
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
}
