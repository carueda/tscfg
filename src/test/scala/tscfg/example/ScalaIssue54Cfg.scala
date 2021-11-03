package tscfg.example

final case class ScalaIssue54Cfg(
  example : ScalaIssue54Cfg.Example
)
object ScalaIssue54Cfg {
  final case class Shared(
    c : java.lang.String,
    d : scala.Int
  )
  object Shared {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue54Cfg.Shared = {
      ScalaIssue54Cfg.Shared(
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
    a : ScalaIssue54Cfg.Shared,
    b : scala.List[ScalaIssue54Cfg.Shared]
  )
  object Example {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue54Cfg.Example = {
      ScalaIssue54Cfg.Example(
        a = ScalaIssue54Cfg.Shared(if(c.hasPathOrNull("a")) c.getConfig("a") else com.typesafe.config.ConfigFactory.parseString("a{}"), parentPath + "a.", $tsCfgValidator),
        b = $_LScalaIssue54Cfg_Shared(c.getList("b"), parentPath, $tsCfgValidator)
      )
    }
    private def $_LScalaIssue54Cfg_Shared(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[ScalaIssue54Cfg.Shared] = {
      import scala.jdk.CollectionConverters._
      cl.asScala.map(cv => ScalaIssue54Cfg.Shared(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig, parentPath, $tsCfgValidator)).toList
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue54Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue54Cfg(
      example = ScalaIssue54Cfg.Example(if(c.hasPathOrNull("example")) c.getConfig("example") else com.typesafe.config.ConfigFactory.parseString("example{}"), parentPath + "example.", $tsCfgValidator)
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
