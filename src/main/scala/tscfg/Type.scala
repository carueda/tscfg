package tscfg

import com.typesafe.config.{ConfigValueType, ConfigValue}

abstract class Type {
  val cv: ConfigValue
  val base: String
  val required: Boolean
  val value: Option[String]
  val description: String

  def spec = cv.unwrapped().toString
}

case class RequiredType(cv: ConfigValue, base: String) extends Type {
  val required = true
  val value = None
  val description = s"Required $base."
}

case class OptionalType(cv: ConfigValue, base: String, value: Option[String] = None) extends Type {
  val required = false
  val description =
    s"Optional $base." + (if (value.isDefined) s" Default value ${value.get}." else "")
}

object Type {
  def apply(cv: ConfigValue): Type = {
    val valueString = cv.unwrapped().toString
    val tokens = valueString.split("""\s*\|\s*""")
    val isOr = tokens.size == 2
    val defaultValue = if (isOr) Some(tokens(1)) else None

    // TODO simplify this!

    tokens(0).toLowerCase match {
      case "string"    => if (isOr) OptionalType(cv, "string", defaultValue.map(s => '"' +s+ '"')) else RequiredType(cv, "string")
      case "string?"   => if (isOr) OptionalType(cv, "string", defaultValue.map(s => '"' +s+ '"')) else OptionalType(cv, "string")

      case "int"       => if (isOr) OptionalType(cv, "int", defaultValue) else RequiredType(cv, "int")
      case "int?"      => if (isOr) OptionalType(cv, "int", defaultValue) else OptionalType(cv, "int")

      case "long"      => if (isOr) OptionalType(cv, "long", defaultValue) else RequiredType(cv, "long")
      case "long?"     => if (isOr) OptionalType(cv, "long", defaultValue) else OptionalType(cv, "long")

      case "double"    => if (isOr) OptionalType(cv, "double", defaultValue) else RequiredType(cv, "double")
      case "double?"   => if (isOr) OptionalType(cv, "double", defaultValue) else OptionalType(cv, "double")

      case "boolean"   => if (isOr) OptionalType(cv, "boolean", defaultValue) else RequiredType(cv, "boolean")
      case "boolean?"  => if (isOr) OptionalType(cv, "boolean", defaultValue) else OptionalType(cv, "boolean")

      case _           => inferType(cv, valueString)
    }
  }

  private def inferType(cv: ConfigValue, valueString : String): Type = {
    val defaultValue = Some(valueString)

    // convenience in case later on we want to allow to indicate whether to make it required or optional via some parameter
    val required = false  // optional is what makes more sense though

    def createType(base: String): Type = {
      if (required)
        RequiredType(cv, base)
      else
        OptionalType(cv, base, if (base == "string") defaultValue.map(s => '"' +s+ '"') else defaultValue)
    }

    def numberType: Type = {
      try {
        valueString.toInt
        createType("int")
      }
      catch {
        case e:NumberFormatException =>
          try {
            valueString.toLong
            createType("long")
          }
          catch {
            case e:NumberFormatException =>
              try {
                valueString.toDouble
                createType("double")
              }
              catch {
                case e:NumberFormatException => throw new AssertionError()
              }
          }
      }
    }

    cv.valueType() match {
      case ConfigValueType.STRING  => createType("string")
      case ConfigValueType.BOOLEAN => createType("boolean")
      case ConfigValueType.NUMBER  => numberType
      case ConfigValueType.LIST    => throw new IllegalArgumentException("list not implemented yet")
      case ConfigValueType.OBJECT  => throw new AssertionError("object unexpected")
      case ConfigValueType.NULL    => throw new AssertionError("null unexpected")
    }
  }
}
