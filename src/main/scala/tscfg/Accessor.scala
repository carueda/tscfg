package tscfg


object Accessor {

  def main(args: Array[String]): Unit = {

    List(
      "string|hello world",
      "string?",
      "int",
      "int?",
      "int | 8080",
      "boolean?",
      "boolean | true"
    ) foreach { spec =>
      val accessor = parseValueSpec(spec)

      println()
      println(s"$accessor accessor for spec: '$spec'")
      val instance = accessor.instance("some.path")
      println(s"instance:\n  |${instance.replace("\n", "\n  |")}")
    }
  }

  def parseValueSpec(spec: String): Accessor = {
    val tokens = spec.split("""\s*\|\s*""")
    val type_ = tokens(0).toLowerCase
    val isOr = tokens.size == 2

    type_ match {
      case "string"    => if (isOr) GetStringOr(tokens(1)) else GetString
      case "string?"   => if (isOr) GetStringOr(tokens(1)) else GetStringOrNull

      case "int"       => if (isOr) GetIntOr(tokens(1))    else GetInt
      case "int?"      => if (isOr) GetIntOr(tokens(1))    else GetIntOrNull

      case "double"    => if (isOr) GetDoubleOr(tokens(1)) else GetDouble
      case "double?"   => if (isOr) GetDoubleOr(tokens(1)) else GetDoubleOrNull

      case "boolean"   => if (isOr) GetBooleanOr(tokens(1)) else GetBoolean
      case "boolean?"  => if (isOr) GetBooleanOr(tokens(1)) else GetBooleanOrNull

      case _           => GetStringOrNull  // TODO: examine the given value to determine more concrete accessor
    }
  }

  abstract class Accessor {
    def javaType: String
    def instance(path: String): String
  }

  abstract class StringAccessor extends Accessor {
    def javaType = "String"
  }

  case object GetString extends StringAccessor {
    def instance(path: String) = s"""c.getString("$path")"""
  }
  case object GetStringOrNull extends StringAccessor {
    def instance(path: String) = s"""c.hasPath("$path") ? c.getString("$path") : null"""
  }
  case class GetStringOr(value: String) extends StringAccessor {
    def instance(path: String) = s"""c.hasPath("$path") ? c.getString("$path") : "$value""""
  }

  case object GetInt extends Accessor {
    def javaType = "int"
    def instance(path: String) = s"""c.getInt("$path")"""
  }
  case object GetIntOrNull extends Accessor {
    def javaType = "Integer"
    def instance(path: String) = s"""c.hasPath("$path") ? Integer.valueOf(c.getInt("$path")) : null"""
  }
  case class GetIntOr(value: String) extends Accessor {
    def javaType = "int"
    def instance(path: String) = s"""c.hasPath("$path") ? c.getInt("$path") : $value"""
  }

  case object GetDouble extends Accessor {
    def javaType = "double"
    def instance(path: String) = s"""c.getDouble("$path")"""
  }
  case object GetDoubleOrNull extends Accessor {
    def javaType = "Double"
    def instance(path: String) = s"""c.hasPath("$path") ? Double.valueOf(c.getDouble("$path")) : null"""
  }
  case class GetDoubleOr(value: String) extends Accessor {
    def javaType = "double"
    def instance(path: String) = s"""c.hasPath("$path") ? c.getDouble("$path") : $value"""
  }

  case object GetBoolean extends Accessor {
    def javaType = "boolean"
    def instance(path: String) = s"""c.getBoolean("$path")"""
  }
  case object GetBooleanOrNull extends Accessor {
    def javaType = "Boolean"
    def instance(path: String) = s"""c.hasPath("$path") ? Boolean.valueOf(c.getBoolean("$path")) : null"""
  }
  case class GetBooleanOr(value: String) extends Accessor {
    def javaType = "boolean"
    def instance(path: String) = s"""c.hasPath("$path") ? c.getBoolean("$path") : $value"""
  }

}
