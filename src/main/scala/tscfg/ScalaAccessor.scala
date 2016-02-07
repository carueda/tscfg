package tscfg

import com.typesafe.config.{ConfigValueType, ConfigValue}
import tscfg.generator.GenOpts

object ScalaAccessor {

  def apply(value: ConfigValue)(implicit genOpts: GenOpts): Accessor = {
    val valueString = value.unwrapped().toString
    val tokens = valueString.split("""\s*\|\s*""")
    val type_ = tokens(0).toLowerCase
    val isOr = tokens.size == 2

    tokens(0).toLowerCase match {
      case "string"    => if (isOr) GetStringOr(tokens(1)) else GetString()
      case "string?"   => if (isOr) GetStringOr(tokens(1)) else GetOptString()

      case "int"       => if (isOr) GetIntOr(tokens(1))    else GetInt()
      case "int?"      => if (isOr) GetIntOr(tokens(1))    else GetOptInt()

      case "long"      => if (isOr) GetLongOr(tokens(1))    else GetLong()
      case "long?"     => if (isOr) GetLongOr(tokens(1))    else GetOptLong()

      case "double"    => if (isOr) GetDoubleOr(tokens(1)) else GetDouble()
      case "double?"   => if (isOr) GetDoubleOr(tokens(1)) else GetOptDouble()

      case "boolean"   => if (isOr) GetBooleanOr(tokens(1)) else GetBoolean()
      case "boolean?"  => if (isOr) GetBooleanOr(tokens(1)) else GetOptBoolean()

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
                case e:NumberFormatException => throw new AssertionError("unexpected")
              }
          }
      }
    }

    value.valueType() match {
      case ConfigValueType.STRING  => GetString()
      case ConfigValueType.BOOLEAN => GetBoolean()
      case ConfigValueType.NUMBER  => numberAccessor
      case ConfigValueType.LIST    => throw new IllegalArgumentException("sorry, list not implemented yet")
      case ConfigValueType.OBJECT  => throw new RuntimeException("unexpected")
      case ConfigValueType.NULL    => throw new RuntimeException("unexpected")
    }
  }

  abstract class StringAccessor()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "String"
  }

  case class GetString()(implicit genOpts: GenOpts) extends StringAccessor {
    def instance(path: String) = s"""c.getString("$path")"""
  }
  case class GetOptString()(implicit genOpts: GenOpts) extends StringAccessor with HasPath {
    override def `type` = "Option[String]"
    def instance(path: String) = s"""if(c.$hasPath("$path")) Some(c.getString("$path")) else None"""
  }
  case class GetStringOr(value: String)(implicit genOpts: GenOpts) extends StringAccessor with HasPath {
    def instance(path: String) = s"""if(c.$hasPath("$path")) c.getString("$path") else "$value""""
  }

  case class GetInt()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "Int"
    def instance(path: String) = s"""c.getInt("$path")"""
  }
  case class GetOptInt()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Option[Int]"
    def instance(path: String) = s"""if(c.$hasPath("$path")) Some(c.getInt("$path")) else None"""
  }
  case class GetIntOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Int"
    def instance(path: String) = s"""if(c.$hasPath("$path")) c.getInt("$path") else $value"""
  }

  case class GetLong()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "Long"
    def instance(path: String) = s"""c.getLong("$path")"""
  }
  case class GetOptLong()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Option[Long]"
    def instance(path: String) = s"""if(c.$hasPath("$path")) Some(c.getLong("$path")) else None"""
  }
  case class GetLongOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Long"
    def instance(path: String) = s"""if(c.$hasPath("$path")) c.getLong("$path") else $value"""
  }

  case class GetDouble()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "Double"
    def instance(path: String) = s"""c.getDouble("$path")"""
  }
  case class GetOptDouble()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Option[Double]"
    def instance(path: String) = s"""if(c.$hasPath("$path")) Some(c.getDouble("$path")) else None"""
  }
  case class GetDoubleOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Double"
    def instance(path: String) = s"""if(c.$hasPath("$path")) c.getDouble("$path") else $value"""
  }

  case class GetBoolean()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "Boolean"
    def instance(path: String) = s"""c.getBoolean("$path")"""
  }
  case class GetOptBoolean()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Option[Boolean]"
    def instance(path: String) = s"""if(c.$hasPath("$path")) Some(c.getBoolean("$path")) else None"""
  }
  case class GetBooleanOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Boolean"
    def instance(path: String) = s"""if(c.$hasPath("$path")) c.getBoolean("$path") else $value"""
  }
}
