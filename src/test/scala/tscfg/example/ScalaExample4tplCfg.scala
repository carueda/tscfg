package tscfg.example

/** @param endpoint
  *   Description of the required endpoint section.
  */
final case class ScalaExample4tplCfg(
  endpoint : ScalaExample4tplCfg.Endpoint
)
object ScalaExample4tplCfg {
  
  /** Description of the required endpoint section.
    * 
    * @param path
    *   The path associated with the endpoint.
    *   For example, "/home/foo/bar"
    * @param stuff
    *   Some optional stuff.
    * @param port
    *   Port for the endpoint service.
    * @param notifications
    *   Configuration for notifications
    */
  final case class Endpoint(
    notifications : ScalaExample4tplCfg.Endpoint.Notifications,
    path          : java.lang.String,
    port          : scala.Int,
    stuff         : scala.Option[ScalaExample4tplCfg.Endpoint.Stuff]
  )
  object Endpoint {
    
    /** Configuration for notifications
      * 
      * @param emails
      *   Emails to send notifications to.
      */
    final case class Notifications(
      emails : scala.List[ScalaExample4tplCfg.Endpoint.Notifications.Emails$Elm]
    )
    object Notifications {
      final case class Emails$Elm(
        email : java.lang.String,
        name  : scala.Option[java.lang.String]
      )
      object Emails$Elm {
        def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaExample4tplCfg.Endpoint.Notifications.Emails$Elm = {
          ScalaExample4tplCfg.Endpoint.Notifications.Emails$Elm(
            email = $_reqStr(parentPath, c, "email", $tsCfgValidator),
            name  = if(c.hasPathOrNull("name")) scala.Some(c.getString("name")) else scala.None
          )
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
            
      def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaExample4tplCfg.Endpoint.Notifications = {
        ScalaExample4tplCfg.Endpoint.Notifications(
          emails = $_LScalaExample4tplCfg_Endpoint_Notifications_Emails$Elm(c.getList("emails"), parentPath, $tsCfgValidator)
        )
      }
      private def $_LScalaExample4tplCfg_Endpoint_Notifications_Emails$Elm(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[ScalaExample4tplCfg.Endpoint.Notifications.Emails$Elm] = {
        import scala.jdk.CollectionConverters._
        cl.asScala.map(cv => ScalaExample4tplCfg.Endpoint.Notifications.Emails$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig, parentPath, $tsCfgValidator)).toList
      }
    }
          
    
    /** Some optional stuff.
      * 
      * @param coefs
      *   Coeficient matrix
      */
    final case class Stuff(
      coefs : scala.List[scala.List[scala.Double]],
      port2 : scala.Int
    )
    object Stuff {
      def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaExample4tplCfg.Endpoint.Stuff = {
        ScalaExample4tplCfg.Endpoint.Stuff(
          coefs = $_L$_L$_dbl(c.getList("coefs"), parentPath, $tsCfgValidator),
          port2 = $_reqInt(parentPath, c, "port2", $tsCfgValidator)
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
    
    }
          
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaExample4tplCfg.Endpoint = {
      ScalaExample4tplCfg.Endpoint(
        notifications = ScalaExample4tplCfg.Endpoint.Notifications(if(c.hasPathOrNull("notifications")) c.getConfig("notifications") else com.typesafe.config.ConfigFactory.parseString("notifications{}"), parentPath + "notifications.", $tsCfgValidator),
        path          = $_reqStr(parentPath, c, "path", $tsCfgValidator),
        port          = if(c.hasPathOrNull("port")) c.getInt("port") else 8080,
        stuff         = if(c.hasPathOrNull("stuff")) scala.Some(ScalaExample4tplCfg.Endpoint.Stuff(c.getConfig("stuff"), parentPath + "stuff.", $tsCfgValidator)) else scala.None
      )
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
        
  def apply(c: com.typesafe.config.Config): ScalaExample4tplCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaExample4tplCfg(
      endpoint = ScalaExample4tplCfg.Endpoint(if(c.hasPathOrNull("endpoint")) c.getConfig("endpoint") else com.typesafe.config.ConfigFactory.parseString("endpoint{}"), parentPath + "endpoint.", $tsCfgValidator)
    )
    $tsCfgValidator.validate()
    $result
  }

  private def $_L$_L$_dbl(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[scala.List[scala.Double]] = {
    import scala.jdk.CollectionConverters._
    cl.asScala.map(cv => $_L$_dbl(cv.asInstanceOf[com.typesafe.config.ConfigList], parentPath, $tsCfgValidator)).toList
  }
  private def $_L$_dbl(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[scala.Double] = {
    import scala.jdk.CollectionConverters._
    cl.asScala.map(cv => $_dbl(cv)).toList
  }
  private def $_dbl(cv:com.typesafe.config.ConfigValue): scala.Double = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) ||
      !u.isInstanceOf[java.lang.Number]) throw $_expE(cv, "double")
    u.asInstanceOf[java.lang.Number].doubleValue()
  }

  private def $_expE(cv:com.typesafe.config.ConfigValue, exp:java.lang.String) = {
    val u: Any = cv.unwrapped
    new java.lang.RuntimeException(s"${cv.origin.lineNumber}: " +
      "expecting: " + exp + " got: " +
      (if (u.isInstanceOf[java.lang.String]) "\"" + u + "\"" else u))
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
