package tscfg.example

final case class ScalaIssue14Cfg(
    _0: ScalaIssue14Cfg._0,
    _2: java.lang.String
)
object ScalaIssue14Cfg {
  final case class _0(
      _1: java.lang.String
  )
  object _0 {
    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue14Cfg._0 = {
      ScalaIssue14Cfg._0(
        _1 = if (c.hasPathOrNull("1")) c.getString("1") else "bar"
      )
    }
  }

  def apply(c: com.typesafe.config.Config): ScalaIssue14Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue14Cfg(
      _0 = ScalaIssue14Cfg._0(
        if (c.hasPathOrNull("0")) c.getConfig("0")
        else com.typesafe.config.ConfigFactory.parseString("0{}"),
        parentPath + "0.",
        $tsCfgValidator
      ),
      _2 = if (c.hasPathOrNull("2")) c.getString("2") else "foo"
    )
    $tsCfgValidator.validate()
    $result
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
