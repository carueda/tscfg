package tscfg.example

final case class ScalaIssue312aCfg(
  endpoint : ScalaIssue312aCfg.Endpoint
)
object ScalaIssue312aCfg {
  /** Description of the required endpoint.
    */
  final case class Endpoint(
    notification : ScalaIssue312aCfg.Endpoint.Notification,
    path         : java.lang.String,
    port         : scala.Int
  )
  object Endpoint {
    /** Configuration for notifications.
      */
    final case class Notification(
      emails : scala.List[ScalaIssue312aCfg.Endpoint.Notification.Emails$Elm]
    )
    object Notification {
      final case class Emails$Elm(
        email : java.lang.String
      )
      object Emails$Elm {
        def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue312aCfg.Endpoint.Notification.Emails$Elm = {
          ScalaIssue312aCfg.Endpoint.Notification.Emails$Elm(
            email = $_reqStr(parentPath, c, "email", $tsCfgValidator)
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
            
      def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue312aCfg.Endpoint.Notification = {
        ScalaIssue312aCfg.Endpoint.Notification(
          emails = $_LScalaIssue312aCfg_Endpoint_Notification_Emails$Elm(c.getList("emails"), parentPath, $tsCfgValidator)
        )
      }
      private def $_LScalaIssue312aCfg_Endpoint_Notification_Emails$Elm(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[ScalaIssue312aCfg.Endpoint.Notification.Emails$Elm] = {
        import scala.jdk.CollectionConverters._
        cl.asScala.map(cv => ScalaIssue312aCfg.Endpoint.Notification.Emails$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig, parentPath, $tsCfgValidator)).toList
      }
    }
          
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue312aCfg.Endpoint = {
      ScalaIssue312aCfg.Endpoint(
        notification = ScalaIssue312aCfg.Endpoint.Notification(if(c.hasPathOrNull("notification")) c.getConfig("notification") else com.typesafe.config.ConfigFactory.parseString("notification{}"), parentPath + "notification.", $tsCfgValidator),
        path         = $_reqStr(parentPath, c, "path", $tsCfgValidator),
        port         = if(c.hasPathOrNull("port")) c.getInt("port") else 8080
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
        
  def apply(c: com.typesafe.config.Config): ScalaIssue312aCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue312aCfg(
      endpoint = ScalaIssue312aCfg.Endpoint(if(c.hasPathOrNull("endpoint")) c.getConfig("endpoint") else com.typesafe.config.ConfigFactory.parseString("endpoint{}"), parentPath + "endpoint.", $tsCfgValidator)
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
