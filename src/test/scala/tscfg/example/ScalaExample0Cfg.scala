package tscfg.example

final case class ScalaExample0Cfg(
    service: ScalaExample0Cfg.Service
)
object ScalaExample0Cfg {
  final case class Service(
      debug: scala.Boolean,
      doLog: scala.Boolean,
      factor: scala.Double,
      poolSize: scala.Int,
      url: java.lang.String
  )
  object Service {
    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaExample0Cfg.Service = {
      ScalaExample0Cfg.Service(
        debug = !c.hasPathOrNull("debug") || c.getBoolean("debug"),
        doLog = c.hasPathOrNull("doLog") && c.getBoolean("doLog"),
        factor = if (c.hasPathOrNull("factor")) c.getDouble("factor") else 0.75,
        poolSize = if (c.hasPathOrNull("poolSize")) c.getInt("poolSize") else 32,
        url = if (c.hasPathOrNull("url")) c.getString("url") else "http://example.net/rest"
      )
    }
  }

  def apply(c: com.typesafe.config.Config): ScalaExample0Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaExample0Cfg(
      service = ScalaExample0Cfg.Service(
        if (c.hasPathOrNull("service")) c.getConfig("service")
        else com.typesafe.config.ConfigFactory.parseString("service{}"),
        parentPath + "service.",
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
