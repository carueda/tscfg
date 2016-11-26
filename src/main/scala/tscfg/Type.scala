package tscfg

import com.typesafe.config.{ConfigList, ConfigObject, ConfigValue, ConfigValueType}

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
  val description = s"Required ${baseType.base}."
}

case class OptionalType(cv: ConfigValue, baseType: BaseType, value: Option[String] = None) extends Type {
  val required = false
  val description =
    s"Optional ${baseType.base}." + (if (value.isDefined) s" Default value ${value.get}." else "")
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
    "string", "int", "integer", "long", "double", "boolean", "duration"
  )

  private def recognizedBaseType(baseType: BaseType): Boolean = recognizedBaseTypes contains baseType.base

  private def inferType(cv: ConfigValue, valueString : String): Type = {
    val defaultValue = Some(valueString)

    def createType(base: String): Type = {
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

    def listType: Type = {
      val configList = cv.asInstanceOf[ConfigList]

      if (configList.isEmpty)
        throw new IllegalArgumentException("list with one element expected")

      if (configList.size() > 1)
        println(s"WARNING: only first element in given list will be considered")

      val cv0: ConfigValue = configList.get(0)

      val elemType = Type(cv0)

      println(s"elemType: $elemType")
      throw new IllegalArgumentException("list not implemented yet")
    }

    def objType: Type = {
      val configObj = cv.asInstanceOf[ConfigObject]
      val conf = configObj.toConfig
      val objNode = nodes.createAllNodes(conf)
      println(s"objNode: $objNode")
      throw new AssertionError("object unexpected")
    }

    cv.valueType() match {
      case ConfigValueType.STRING  => createType("string")
      case ConfigValueType.BOOLEAN => createType("boolean")
      case ConfigValueType.NUMBER  => numberType
      case ConfigValueType.LIST    => listType
      case ConfigValueType.OBJECT  => objType
      case ConfigValueType.NULL    => throw new AssertionError("null unexpected")
    }
  }
}
