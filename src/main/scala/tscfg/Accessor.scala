package tscfg

import tscfg.generator.GenOpts


object Accessor {

  def parseValueSpec(spec: String)(implicit genOpts: GenOpts): Accessor = {
    val tokens = spec.split("""\s*\|\s*""")
    val type_ = tokens(0).toLowerCase
    val isOr = tokens.size == 2

    type_ match {
      case "string"    => if (isOr) GetStringOr(tokens(1)) else GetString()
      case "string?"   => if (isOr) GetStringOr(tokens(1)) else GetStringOrNull()

      case "int"       => if (isOr) GetIntOr(tokens(1))    else GetInt()
      case "int?"      => if (isOr) GetIntOr(tokens(1))    else GetIntOrNull()

      case "double"    => if (isOr) GetDoubleOr(tokens(1)) else GetDouble()
      case "double?"   => if (isOr) GetDoubleOr(tokens(1)) else GetDoubleOrNull()

      case "boolean"   => if (isOr) GetBooleanOr(tokens(1)) else GetBoolean()
      case "boolean?"  => if (isOr) GetBooleanOr(tokens(1)) else GetBooleanOrNull()

      case _           => GetString()  // TODO: examine the given value to determine more concrete accessor
    }
  }

  trait HasPath {
    def hasPath()(implicit genOpts: GenOpts) = if (genOpts.j7) "hasPath" else "hasPathOrNull"
  }

  abstract class Accessor {
    def javaType: String
    def instance(path: String): String
  }

  abstract class StringAccessor()(implicit genOpts: GenOpts) extends Accessor {
    def javaType = "String"
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
    def javaType = "int"
    def instance(path: String) = s"""c.getInt("$path")"""
  }
  case class GetIntOrNull()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def javaType = "Integer"
    def instance(path: String) = s"""c.$hasPath("$path") ? Integer.valueOf(c.getInt("$path")) : null"""
  }
  case class GetIntOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def javaType = "int"
    def instance(path: String) = s"""c.$hasPath("$path") ? c.getInt("$path") : $value"""
  }

  case class GetDouble()(implicit genOpts: GenOpts) extends Accessor {
    def javaType = "double"
    def instance(path: String) = s"""c.getDouble("$path")"""
  }
  case class GetDoubleOrNull()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def javaType = "Double"
    def instance(path: String) = s"""c.$hasPath("$path") ? Double.valueOf(c.getDouble("$path")) : null"""
  }
  case class GetDoubleOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def javaType = "double"
    def instance(path: String) = s"""c.$hasPath("$path") ? c.getDouble("$path") : $value"""
  }

  case class GetBoolean()(implicit genOpts: GenOpts) extends Accessor {
    def javaType = "boolean"
    def instance(path: String) = s"""c.getBoolean("$path")"""
  }
  case class GetBooleanOrNull()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def javaType = "Boolean"
    def instance(path: String) = s"""c.$hasPath("$path") ? Boolean.valueOf(c.getBoolean("$path")) : null"""
  }
  case class GetBooleanOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def javaType = "boolean"
    def instance(path: String) = s"""c.$hasPath("$path") ? c.getBoolean("$path") : $value"""
  }

}
