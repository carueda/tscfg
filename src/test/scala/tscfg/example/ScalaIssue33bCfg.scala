package tscfg.example

final case class ScalaIssue33bCfg(
  endpoint : ScalaIssue33bCfg.Endpoint
)
object ScalaIssue33bCfg {
  final case class Endpoint(
    baz : ScalaIssue33bCfg.Endpoint.Baz,
    foo : scala.Option[scala.Int],
    url : java.lang.String
  )
  object Endpoint {
    final case class Baz(
      key : java.lang.String
    )
    object Baz {
      def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue33bCfg.Endpoint.Baz = {
        ScalaIssue33bCfg.Endpoint.Baz(
          key = if(c.hasPathOrNull("key")) c.getString("key") else "bar"
        )
      }
    }
          
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue33bCfg.Endpoint = {
      ScalaIssue33bCfg.Endpoint(
        baz = ScalaIssue33bCfg.Endpoint.Baz(if(c.hasPathOrNull("baz")) c.getConfig("baz") else com.typesafe.config.ConfigFactory.parseString("baz{}"), parentPath + "baz.", $tsCfgValidator),
        foo = if(c.hasPathOrNull("foo")) Some(c.getInt("foo")) else None,
        url = if(c.hasPathOrNull("url")) c.getString("url") else "http://example.net"
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue33bCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue33bCfg(
      endpoint = ScalaIssue33bCfg.Endpoint(if(c.hasPathOrNull("endpoint")) c.getConfig("endpoint") else com.typesafe.config.ConfigFactory.parseString("endpoint{}"), parentPath + "endpoint.", $tsCfgValidator)
    )
    $tsCfgValidator.validate()
    $result
  }
  final class $TsCfgValidator {
    private val badPaths = scala.collection.mutable.ArrayBuffer[java.lang.String]()

    def addBadPath(path: java.lang.String, e: com.typesafe.config.ConfigException): Unit = {
      badPaths += s"'$path': ${e.getClass.getName}(${e.getMessage})"
    }

    def addInvalidEnumValue(path: java.lang.String, value: java.lang.String, enumName: java.lang.String): Unit = {
      badPaths += s"'$path': invalid value $value for enumeration $enumName"
    }

    def validate(): Unit = {
      if (badPaths.nonEmpty) {
        throw new com.typesafe.config.ConfigException(
          badPaths.mkString("Invalid configuration:\n    ", "\n    ", "")
        ){}
      }
    }
  }
}
