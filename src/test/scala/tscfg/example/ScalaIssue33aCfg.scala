package tscfg.example

final case class ScalaIssue33aCfg(
  endpoint : ScalaIssue33aCfg.Endpoint
)
object ScalaIssue33aCfg {
  final case class Endpoint(
    more : ScalaIssue33aCfg.Endpoint.More
  )
  object Endpoint {
    final case class More(
      url : java.lang.String
    )
    object More {
      def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue33aCfg.Endpoint.More = {
        ScalaIssue33aCfg.Endpoint.More(
          url = if(c.hasPathOrNull("url")) c.getString("url") else "http://example.net"
        )
      }
    }
          
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue33aCfg.Endpoint = {
      ScalaIssue33aCfg.Endpoint(
        more = ScalaIssue33aCfg.Endpoint.More(if(c.hasPathOrNull("more")) c.getConfig("more") else com.typesafe.config.ConfigFactory.parseString("more{}"), parentPath + "more.", $tsCfgValidator)
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue33aCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue33aCfg(
      endpoint = ScalaIssue33aCfg.Endpoint(if(c.hasPathOrNull("endpoint")) c.getConfig("endpoint") else com.typesafe.config.ConfigFactory.parseString("endpoint{}"), parentPath + "endpoint.", $tsCfgValidator)
    )
    $tsCfgValidator.validate()
    $result
  }
  private final class $TsCfgValidator {
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
