package tscfg.example

final case class ScalaIssue124Cfg(
  example : ScalaIssue124Cfg.Example
)
object ScalaIssue124Cfg {
  final case class Shared(
    c : java.lang.String,
    d : scala.Int
  )
  object Shared {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue124Cfg.Shared = {
      ScalaIssue124Cfg.Shared(
        c = $_reqStr(parentPath, c, "c", $tsCfgValidator),
        d = $_reqInt(parentPath, c, "d", $tsCfgValidator)
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
        
  final case class Example(
    a : scala.Option[ScalaIssue124Cfg.Shared],
    b : scala.Option[scala.List[ScalaIssue124Cfg.Shared]]
  )
  object Example {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue124Cfg.Example = {
      ScalaIssue124Cfg.Example(
        a = if(c.hasPathOrNull("a")) scala.Some(ScalaIssue124Cfg.Shared(c.getConfig("a"), parentPath + "a.", $tsCfgValidator)) else None,
        b = if(c.hasPathOrNull("b")) scala.Some($_LScalaIssue124Cfg_Shared(c.getList("b"), parentPath, $tsCfgValidator)) else None
      )
    }
    private def $_LScalaIssue124Cfg_Shared(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[ScalaIssue124Cfg.Shared] = {
      import scala.jdk.CollectionConverters._
      cl.asScala.map(cv => ScalaIssue124Cfg.Shared(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig, parentPath, $tsCfgValidator)).toList
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue124Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue124Cfg(
      example = ScalaIssue124Cfg.Example(if(c.hasPathOrNull("example")) c.getConfig("example") else com.typesafe.config.ConfigFactory.parseString("example{}"), parentPath + "example.", $tsCfgValidator)
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
