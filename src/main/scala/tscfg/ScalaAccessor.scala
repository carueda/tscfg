package tscfg

import tscfg.generator.GenOpts

object ScalaAccessor {

  def apply(spec: String)(implicit genOpts: GenOpts): Accessor = {
    val tokens = spec.split("""\s*\|\s*""")
    val type_ = tokens(0).toLowerCase
    val isOr = tokens.size == 2

    type_ match {
      case "string"    => if (isOr) GetStringOr(tokens(1)) else GetString()
      case "string?"   => if (isOr) GetStringOr(tokens(1)) else GetOptString()

      case "int"       => if (isOr) GetIntOr(tokens(1))    else GetInt()
      case "int?"      => if (isOr) GetIntOr(tokens(1))    else GetOptInt()

      case "double"    => if (isOr) GetDoubleOr(tokens(1)) else GetDouble()
      case "double?"   => if (isOr) GetDoubleOr(tokens(1)) else GetOptDouble()

      case "boolean"   => if (isOr) GetBooleanOr(tokens(1)) else GetBoolean()
      case "boolean?"  => if (isOr) GetBooleanOr(tokens(1)) else GetOptBoolean()

      case _           => GetString()  // TODO: examine the given value to determine more concrete accessor
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
