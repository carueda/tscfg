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
    val typePart = tokens(0).toLowerCase
    val hasDefault = tokens.size == 2
    val defaultValue = if (hasDefault) Some(tokens(1)) else None

    val (base, isOpt) = if (typePart.endsWith("?"))
      (typePart.substring(0, typePart.length - 1), true)
    else
      (typePart, false)

    if (recognizedBaseType(base)) {
      if (hasDefault)
        OptionalType(cv, base, if (typePart == "string") defaultValue.map(s => '"' +s+ '"') else defaultValue)
      else
        if (isOpt) OptionalType(cv, base) else RequiredType(cv, base)
    }
    else inferType(cv, valueString)
  }

  private val recognizedBaseTypes: Set[String] = Set(
    "string", "int", "long", "double", "boolean"
  )

  private def recognizedBaseType(base: String): Boolean = recognizedBaseTypes contains base

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
