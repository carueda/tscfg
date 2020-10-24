package tscfg.example

final case class ScalaIssue33Cfg(
    endpoint: ScalaIssue33Cfg.Endpoint
)
object ScalaIssue33Cfg {
  final case class Endpoint(
      url: java.lang.String
  )
  object Endpoint {
    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue33Cfg.Endpoint = {
      ScalaIssue33Cfg.Endpoint(
        url = if (c.hasPathOrNull("url")) c.getString("url") else "http://example.net"
      )
    }
  }

  def apply(c: com.typesafe.config.Config): ScalaIssue33Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue33Cfg(
      endpoint = ScalaIssue33Cfg.Endpoint(
        if (c.hasPathOrNull("endpoint")) c.getConfig("endpoint")
        else com.typesafe.config.ConfigFactory.parseString("endpoint{}"),
        parentPath + "endpoint.",
        $tsCfgValidator
      )
    )
    $tsCfgValidator.validate()
    $result
  }
  private final class $TsCfgValidator {
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
