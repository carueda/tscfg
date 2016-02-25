package tscfg

import com.typesafe.config.{ConfigValueType, ConfigValue}

abstract class Type {
  val base: String
  val required: Boolean
  val value: Option[String]
  val description: String
}

case class RequiredType(base: String) extends Type {
  val required = true
  val value = None
  val description = s"Required $base."
}

case class OptionalType(base: String, value: Option[String] = None) extends Type {
  val required = false
  val description =
    s"Optional $base." + (if (value.isDefined) s" Default value ${value.get}." else "")
}

object Type {
  def apply(value: ConfigValue): Type = {
    val valueString = value.unwrapped().toString
    val tokens = valueString.split("""\s*\|\s*""")
    val isOr = tokens.size == 2
    val defaultValue = if (isOr) Some(tokens(1)) else None

    tokens(0).toLowerCase match {
      case "string"    => if (isOr) OptionalType("string", defaultValue.map(s => '"' +s+ '"')) else RequiredType("string")
      case "string?"   => if (isOr) OptionalType("string", defaultValue.map(s => '"' +s+ '"')) else OptionalType("string")

      case "int"       => if (isOr) OptionalType("int", defaultValue) else RequiredType("int")
      case "int?"      => if (isOr) OptionalType("int", defaultValue) else OptionalType("int")

      case "long"      => if (isOr) OptionalType("long", defaultValue) else RequiredType("long")
      case "long?"     => if (isOr) OptionalType("long", defaultValue) else OptionalType("long")

      case "double"    => if (isOr) OptionalType("double", defaultValue) else RequiredType("double")
      case "double?"   => if (isOr) OptionalType("double", defaultValue) else OptionalType("double")

      case "boolean"   => if (isOr) OptionalType("boolean", defaultValue) else RequiredType("boolean")
      case "boolean?"  => if (isOr) OptionalType("boolean", defaultValue) else OptionalType("boolean")

      case _           => inferType(value, valueString)
    }
  }

  private def inferType(value: ConfigValue, valueString : String): Type = {
    val defaultValue = Some(valueString)

    def numberType: Type = {
      try {
        valueString.toInt
        OptionalType("int", defaultValue)
      }
      catch {
        case e:NumberFormatException =>
          try {
            valueString.toLong
            OptionalType("long", defaultValue)
          }
          catch {
            case e:NumberFormatException =>
              try {
                valueString.toDouble
                OptionalType("double", defaultValue)
              }
              catch {
                case e:NumberFormatException => throw new AssertionError()
              }
          }
      }
    }

    value.valueType() match {
      case ConfigValueType.STRING  => OptionalType("string", defaultValue)
      case ConfigValueType.BOOLEAN => OptionalType("boolean", defaultValue)
      case ConfigValueType.NUMBER  => numberType
      case ConfigValueType.LIST    => throw new IllegalArgumentException("list not implemented yet")
      case ConfigValueType.OBJECT  => throw new AssertionError("object unexpected")
      case ConfigValueType.NULL    => throw new AssertionError("null unexpected")
    }
  }
}
