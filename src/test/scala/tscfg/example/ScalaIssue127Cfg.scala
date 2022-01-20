package tscfg.example

final case class ScalaIssue127Cfg(
  example : ScalaIssue127Cfg.Example
)
object ScalaIssue127Cfg {
  final case class Shared(
    c : java.lang.String,
    d : scala.Int
  )
  object Shared {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue127Cfg.Shared = {
      ScalaIssue127Cfg.Shared(
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
    a : ScalaIssue127Cfg.Shared
  )
  object Example {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue127Cfg.Example = {
      ScalaIssue127Cfg.Example(
        a = ScalaIssue127Cfg.Shared(if(c.hasPathOrNull("a")) c.getConfig("a") else com.typesafe.config.ConfigFactory.parseString("a{}"), parentPath + "a.", $tsCfgValidator)
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue127Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue127Cfg(
      example = ScalaIssue127Cfg.Example(if(c.hasPathOrNull("example")) c.getConfig("example") else com.typesafe.config.ConfigFactory.parseString("example{}"), parentPath + "example.", $tsCfgValidator)
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
