package tscfg.example

final case class ScalaIssue61Cfg(
    intParams: scala.Option[scala.List[scala.Int]]
)
object ScalaIssue61Cfg {
  def apply(c: com.typesafe.config.Config): ScalaIssue61Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue61Cfg(
      intParams =
        if (c.hasPathOrNull("intParams"))
          scala.Some($_L$_int(c.getList("intParams"), parentPath, $tsCfgValidator))
        else None
    )
    $tsCfgValidator.validate()
    $result
  }

  private def $_L$_int(
      cl: com.typesafe.config.ConfigList,
      parentPath: java.lang.String,
      $tsCfgValidator: $TsCfgValidator
  ): scala.List[scala.Int] = {
    import scala.jdk.CollectionConverters._
    cl.asScala.map(cv => $_int(cv)).toList
  }
  private def $_expE(cv: com.typesafe.config.ConfigValue, exp: java.lang.String) = {
    val u: Any = cv.unwrapped
    new java.lang.RuntimeException(
      s"${cv.origin.lineNumber}: " +
        "expecting: " + exp + " got: " +
        (if (u.isInstanceOf[java.lang.String]) "\"" + u + "\"" else u)
    )
  }

  private def $_int(cv: com.typesafe.config.ConfigValue): scala.Int = {
    val u: Any = cv.unwrapped
    if (
      (cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) ||
      !u.isInstanceOf[Integer]
    ) throw $_expE(cv, "integer")
    u.asInstanceOf[Integer]
  }

  final class $TsCfgValidator {
    private val badPaths = scala.collection.mutable.ArrayBuffer[java.lang.String]()

    def addBadPath(path: java.lang.String, e: com.typesafe.config.ConfigException): Unit = {
      badPaths += s"'$path': ${e.getClass.getName}(${e.getMessage})"
    }

    def addInvalidEnumValue(
        path: java.lang.String,
        value: java.lang.String,
        enumName: java.lang.String
    ): Unit = {
      badPaths += s"'$path': invalid value $value for enumeration $enumName"
    }

    def validate(): Unit = {
      if (badPaths.nonEmpty) {
        throw new com.typesafe.config.ConfigException(
          badPaths.mkString("Invalid configuration:\n    ", "\n    ", "")
        ) {}
      }
    }
  }
}
