package tscfg.example

final case class ScalaIssue33cCfg(
    endpoint: ScalaIssue33cCfg.Endpoint
)
object ScalaIssue33cCfg {
  final case class Endpoint(
      optObj: ScalaIssue33cCfg.Endpoint.OptObj,
      req: java.lang.String
  )
  object Endpoint {
    final case class OptObj(
        key: java.lang.String
    )
    object OptObj {
      def apply(
          c: com.typesafe.config.Config,
          parentPath: java.lang.String,
          $tsCfgValidator: $TsCfgValidator
      ): ScalaIssue33cCfg.Endpoint.OptObj = {
        ScalaIssue33cCfg.Endpoint.OptObj(
          key = if (c.hasPathOrNull("key")) c.getString("key") else "bar"
        )
      }
    }

    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue33cCfg.Endpoint = {
      ScalaIssue33cCfg.Endpoint(
        optObj = ScalaIssue33cCfg.Endpoint.OptObj(
          if (c.hasPathOrNull("optObj")) c.getConfig("optObj")
          else com.typesafe.config.ConfigFactory.parseString("optObj{}"),
          parentPath + "optObj.",
          $tsCfgValidator
        ),
        req = $_reqStr(parentPath, c, "req", $tsCfgValidator)
      )
    }
    private def $_reqStr(
        parentPath: java.lang.String,
        c: com.typesafe.config.Config,
        path: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): java.lang.String = {
      if (c == null) null
      else
        try c.getString(path)
        catch {
          case e: com.typesafe.config.ConfigException =>
            $tsCfgValidator.addBadPath(parentPath + path, e)
            null
        }
    }

  }

  def apply(c: com.typesafe.config.Config): ScalaIssue33cCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue33cCfg(
      endpoint = ScalaIssue33cCfg.Endpoint(
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
