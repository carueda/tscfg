package tscfg.example

final case class ScalaIssue10Cfg(
  main : ScalaIssue10Cfg.Main
)
object ScalaIssue10Cfg {
  
  /** @param email
    *   Mail server properties if you want to enable notifications to users
    */
  final case class Main(
    email : scala.Option[ScalaIssue10Cfg.Main.Email],
    reals : scala.Option[scala.List[ScalaIssue10Cfg.Main.Reals$Elm]]
  )
  object Main {
    
    /** Mail server properties if you want to enable notifications to users
      */
    final case class Email(
      password : java.lang.String,
      server   : java.lang.String
    )
    object Email {
      def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue10Cfg.Main.Email = {
        ScalaIssue10Cfg.Main.Email(
          password = $_reqStr(parentPath, c, "password", $tsCfgValidator),
          server   = $_reqStr(parentPath, c, "server", $tsCfgValidator)
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
          
    final case class Reals$Elm(
      foo : scala.Double
    )
    object Reals$Elm {
      def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue10Cfg.Main.Reals$Elm = {
        ScalaIssue10Cfg.Main.Reals$Elm(
          foo = $_reqDbl(parentPath, c, "foo", $tsCfgValidator)
        )
      }
      private def $_reqDbl(parentPath: java.lang.String, c: com.typesafe.config.Config, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.Double = {
        if (c == null) 0
        else try c.getDouble(path)
        catch {
          case e:com.typesafe.config.ConfigException =>
            $tsCfgValidator.addBadPath(parentPath + path, e)
            0
        }
      }
    
    }
          
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue10Cfg.Main = {
      ScalaIssue10Cfg.Main(
        email = if(c.hasPathOrNull("email")) scala.Some(ScalaIssue10Cfg.Main.Email(c.getConfig("email"), parentPath + "email.", $tsCfgValidator)) else scala.None,
        reals = if(c.hasPathOrNull("reals")) scala.Some($_LScalaIssue10Cfg_Main_Reals$Elm(c.getList("reals"), parentPath, $tsCfgValidator)) else scala.None
      )
    }
    private def $_LScalaIssue10Cfg_Main_Reals$Elm(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[ScalaIssue10Cfg.Main.Reals$Elm] = {
      import scala.jdk.CollectionConverters._
      cl.asScala.map(cv => ScalaIssue10Cfg.Main.Reals$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig, parentPath, $tsCfgValidator)).toList
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue10Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue10Cfg(
      main = ScalaIssue10Cfg.Main(if(c.hasPathOrNull("main")) c.getConfig("main") else com.typesafe.config.ConfigFactory.parseString("main{}"), parentPath + "main.", $tsCfgValidator)
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
