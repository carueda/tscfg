package tscfg

import com.typesafe.config.{ConfigValueType, ConfigValue}

abstract sealed class BaseType {
  val base: String
  val qualification: Option[String] = None
}
case class UnqualifiedBaseType(base: String) extends BaseType
case class QualifiedBaseType(base: String, q: String) extends BaseType {
  override val qualification: Option[String] = Some(q)
}

object BaseType {
  def apply(base: String, qualification: String) = {
    QualifiedBaseType(base,
      if (base == "duration")
        canonicalDurationUnit(qualification)
      else qualification
    )
  }

  def apply(base: String) = UnqualifiedBaseType(base)

  // https://github.com/typesafehub/config/blob/master/HOCON.md#duration-format
  private def canonicalDurationUnit(s: String): String = s match {
    case "ns" | "nano" | "nanos" | "nanosecond" | "nanoseconds"     => "nanosecond"
    case "us" | "micro" | "micros" | "microsecond" | "microseconds" => "microsecond"
    case "ms" | "milli" | "millis" | "millisecond" | "milliseconds" => "millisecond"
    case "s" | "second" | "seconds" => "second"
    case "m" | "minute" | "minutes" => "minute"
    case "h" | "hour" | "hours"     => "hour"
    case "d" | "day" | "days"       => "day"
    case _ => throw new AssertionError()
  }
}

abstract class Type {
  val cv: ConfigValue
  val baseType: BaseType
  val required: Boolean
  val value: Option[String]
  val description: String

  def spec = cv.unwrapped().toString
}

case class RequiredType(cv: ConfigValue, baseType: BaseType) extends Type {
  val required = true
  val value = None
  val description = s"Required $baseType."
}

case class OptionalType(cv: ConfigValue, baseType: BaseType, value: Option[String] = None) extends Type {
  val required = false
  val description =
    s"Optional $baseType." + (if (value.isDefined) s" Default value ${value.get}." else "")
}

object Type {

  def apply(cv: ConfigValue): Type = {
    val valueString = cv.unwrapped().toString
    val tokens = valueString.split("""\s*\|\s*""")
    val typePart = tokens(0).toLowerCase
    val hasDefault = tokens.size == 2
    val defaultValue = if (hasDefault) Some(tokens(1)) else None

    val (baseString, isOpt) = if (typePart.endsWith("?"))
      (typePart.substring(0, typePart.length - 1), true)
    else
      (typePart, false)

    val baseType: BaseType = {
      val parts = baseString.split("""\s*\:\s*""", 2)
      if (parts.size == 1)
        BaseType(parts(0))
      else
        BaseType(parts(0), parts(1))
    }

    if (recognizedBaseType(baseType)) {
      if (hasDefault)
        OptionalType(cv, baseType, if (typePart == "string") defaultValue.map(s => '"' +s+ '"') else defaultValue)
      else
        if (isOpt) OptionalType(cv, baseType) else RequiredType(cv, baseType)
    }
    else inferType(cv, valueString)
  }

  private val recognizedBaseTypes: Set[String] = Set(
    "string", "int", "long", "double", "boolean", "duration"
  )

  private def recognizedBaseType(baseType: BaseType): Boolean = recognizedBaseTypes contains baseType.base

  private def inferType(cv: ConfigValue, valueString : String): Type = {
    val defaultValue = Some(valueString)

    // convenience in case later on we want to allow to indicate whether to make it required or optional via some parameter
    val required = false  // optional is what makes more sense though

    def createType(base: String): Type = {
      if (required)
        RequiredType(cv, BaseType(base))
      else
        OptionalType(cv, BaseType(base), if (base == "string") defaultValue.map(s => '"' +s+ '"') else defaultValue)
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
