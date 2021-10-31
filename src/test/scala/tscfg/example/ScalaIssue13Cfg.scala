package tscfg.example

final case class ScalaIssue13Cfg(
    issue: ScalaIssue13Cfg.Issue
)
object ScalaIssue13Cfg {
  final case class Issue(
      optionalFoo: scala.Option[java.lang.String]
  )
  object Issue {
    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue13Cfg.Issue = {
      ScalaIssue13Cfg.Issue(
        optionalFoo = if (c.hasPathOrNull("optionalFoo")) Some(c.getString("optionalFoo")) else None
      )
    }
  }

  def apply(c: com.typesafe.config.Config): ScalaIssue13Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue13Cfg(
      issue = ScalaIssue13Cfg.Issue(
        if (c.hasPathOrNull("issue")) c.getConfig("issue")
        else com.typesafe.config.ConfigFactory.parseString("issue{}"),
        parentPath + "issue.",
        $tsCfgValidator
      )
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
