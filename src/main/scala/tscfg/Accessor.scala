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
    def scalaType: String
    def scalaInstance(path: String): String
  }

  abstract class StringAccessor()(implicit genOpts: GenOpts) extends Accessor {
    def javaType = "String"
    def scalaType = "String"
  }

  case class GetString()(implicit genOpts: GenOpts) extends StringAccessor {
    def instance(path: String) = s"""c.getString("$path")"""
    def scalaInstance(path: String) = s"""c.getString("$path")"""
  }
  case class GetStringOrNull()(implicit genOpts: GenOpts) extends StringAccessor with HasPath {
    def instance(path: String) = s"""c.$hasPath("$path") ? c.getString("$path") : null"""
    override def scalaType = "Option[String]"
    def scalaInstance(path: String) = s"""if(c.$hasPath("$path")) Some(c.getString("$path")) else None"""
  }
  case class GetStringOr(value: String)(implicit genOpts: GenOpts) extends StringAccessor with HasPath {
    def instance(path: String) = s"""c.$hasPath("$path") ? c.getString("$path") : "$value""""
    def scalaInstance(path: String) = s"""if(c.$hasPath("$path")) c.getString("$path") else "$value""""
  }

  case class GetInt()(implicit genOpts: GenOpts) extends Accessor {
    def javaType = "int"
    def instance(path: String) = s"""c.getInt("$path")"""
    def scalaType = "Int"
    def scalaInstance(path: String) = s"""c.getInt("$path")"""
  }
  case class GetIntOrNull()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def javaType = "Integer"
    def instance(path: String) = s"""c.$hasPath("$path") ? Integer.valueOf(c.getInt("$path")) : null"""
    def scalaType = "Option[Int]"
    def scalaInstance(path: String) = s"""if(c.$hasPath("$path")) Some(c.getInt("$path")) else None"""
  }
  case class GetIntOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def javaType = "int"
    def instance(path: String) = s"""c.$hasPath("$path") ? c.getInt("$path") : $value"""
    def scalaType = "Int"
    def scalaInstance(path: String) = s"""if(c.$hasPath("$path")) c.getInt("$path") else $value"""
  }

  case class GetDouble()(implicit genOpts: GenOpts) extends Accessor {
    def javaType = "double"
    def instance(path: String) = s"""c.getDouble("$path")"""
    def scalaType = "Double"
    def scalaInstance(path: String) = s"""c.getDouble("$path")"""
  }
  case class GetDoubleOrNull()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def javaType = "Double"
    def instance(path: String) = s"""c.$hasPath("$path") ? Double.valueOf(c.getDouble("$path")) : null"""
    def scalaType = "Option[Double]"
    def scalaInstance(path: String) = s"""if(c.$hasPath("$path")) Some(c.getDouble("$path")) else None"""
  }
  case class GetDoubleOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def javaType = "double"
    def instance(path: String) = s"""c.$hasPath("$path") ? c.getDouble("$path") : $value"""
    def scalaType = "Double"
    def scalaInstance(path: String) = s"""if(c.$hasPath("$path")) c.getDouble("$path") else $value"""
  }

  case class GetBoolean()(implicit genOpts: GenOpts) extends Accessor {
    def javaType = "boolean"
    def instance(path: String) = s"""c.getBoolean("$path")"""
    def scalaType = "Boolean"
    def scalaInstance(path: String) = s"""c.getBoolean("$path")"""
  }
  case class GetBooleanOrNull()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def javaType = "Boolean"
    def instance(path: String) = s"""c.$hasPath("$path") ? Boolean.valueOf(c.getBoolean("$path")) : null"""
    def scalaType = "Option[Boolean]"
    def scalaInstance(path: String) = s"""if(c.$hasPath("$path")) Some(c.getBoolean("$path")) else None"""
  }
  case class GetBooleanOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def javaType = "boolean"
    def instance(path: String) = s"""c.$hasPath("$path") ? c.getBoolean("$path") : $value"""
    def scalaType = "Boolean"
    def scalaInstance(path: String) = s"""if(c.$hasPath("$path")) c.getBoolean("$path") else $value"""
  }

}
