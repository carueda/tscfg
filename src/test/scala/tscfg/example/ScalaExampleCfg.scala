package tscfg.example

final case class ScalaExampleCfg(
  endpoint : ScalaExampleCfg.Endpoint
)
object ScalaExampleCfg {
  
  /** 
    * @param serial
    *   an optional Integer with default value null
    * @param path
    *   a required String
    * @param url
    *   a String with default value "http://example.net"
    * @param intReq
    *   a required int
    */
  final case class Endpoint(
    intReq    : scala.Int,
    interface : ScalaExampleCfg.Endpoint.Interface,
    path      : java.lang.String,
    serial    : scala.Option[scala.Int],
    url       : java.lang.String
  )
  object Endpoint {
    
    /** 
      * @param port
      *   an int with default value 8080
      */
    final case class Interface(
      port   : scala.Int,
      `type` : scala.Option[java.lang.String]
    )
    object Interface {
      def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaExampleCfg.Endpoint.Interface = {
        ScalaExampleCfg.Endpoint.Interface(
          port   = if(c.hasPathOrNull("port")) c.getInt("port") else 8080,
          `type` = if(c.hasPathOrNull("type")) Some(c.getString("type")) else None
        )
      }
    }
          
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaExampleCfg.Endpoint = {
      ScalaExampleCfg.Endpoint(
        intReq    = $_reqInt(parentPath, c, "intReq", $tsCfgValidator),
        interface = ScalaExampleCfg.Endpoint.Interface(if(c.hasPathOrNull("interface")) c.getConfig("interface") else com.typesafe.config.ConfigFactory.parseString("interface{}"), parentPath + "interface.", $tsCfgValidator),
        path      = $_reqStr(parentPath, c, "path", $tsCfgValidator),
        serial    = if(c.hasPathOrNull("serial")) Some(c.getInt("serial")) else None,
        url       = if(c.hasPathOrNull("url")) c.getString("url") else "http://example.net"
      )
    }
    private def $_reqInt(parentPath: java.lang.String, c: com.typesafe.config.Config, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.Int = {
      if (c == null) 0
      else try c.getInt(path)
      catch {
        case e:com.typesafe.config.ConfigException =>
          $tsCfgValidator.addBadPath(parentPath + path, e)
          0
      }
    }
  
    private def $_reqStr(parentPath: java.lang.String, c: com.typesafe.config.Config, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): java.lang.String = {
      if (c == null) null
      else try c.getString(path)
      catch {
        case e:com.typesafe.config.ConfigException =>
          $tsCfgValidator.addBadPath(parentPath + path, e)
          null
      }
    }
  
  }
        
  def apply(c: com.typesafe.config.Config): ScalaExampleCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaExampleCfg(
      endpoint = ScalaExampleCfg.Endpoint(if(c.hasPathOrNull("endpoint")) c.getConfig("endpoint") else com.typesafe.config.ConfigFactory.parseString("endpoint{}"), parentPath + "endpoint.", $tsCfgValidator)
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
