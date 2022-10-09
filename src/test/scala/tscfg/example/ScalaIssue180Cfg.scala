package tscfg.example

final case class ScalaIssue180Cfg(
  cfg   : ScalaIssue180Cfg.Cfg
)
object ScalaIssue180Cfg {
  final case class TypeA(
    buzz : java.lang.String,
    fizz : java.lang.String
  )
  object TypeA {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue180Cfg.TypeA = {
      ScalaIssue180Cfg.TypeA(
        buzz = $_reqStr(parentPath, c, "buzz", $tsCfgValidator),
        fizz = $_reqStr(parentPath, c, "fizz", $tsCfgValidator)
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
        
  final case class TypeB(
    bar : ScalaIssue180Cfg.TypeA,
    foo : ScalaIssue180Cfg.TypeA
  )
  object TypeB {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue180Cfg.TypeB = {
      ScalaIssue180Cfg.TypeB(
        bar = ScalaIssue180Cfg.TypeA(if(c.hasPathOrNull("bar")) c.getConfig("bar") else com.typesafe.config.ConfigFactory.parseString("bar{}"), parentPath + "bar.", $tsCfgValidator),
        foo = ScalaIssue180Cfg.TypeA(if(c.hasPathOrNull("foo")) c.getConfig("foo") else com.typesafe.config.ConfigFactory.parseString("foo{}"), parentPath + "foo.", $tsCfgValidator)
      )
    }
  }
        
  final case class Cfg(
    additionalParam : java.lang.String,
    typeB           : ScalaIssue180Cfg.TypeB
  )
  object Cfg {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue180Cfg.Cfg = {
      ScalaIssue180Cfg.Cfg(
        additionalParam = $_reqStr(parentPath, c, "additionalParam", $tsCfgValidator),
        typeB           = ScalaIssue180Cfg.TypeB(if(c.hasPathOrNull("typeB")) c.getConfig("typeB") else com.typesafe.config.ConfigFactory.parseString("typeB{}"), parentPath + "typeB.", $tsCfgValidator)
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
        
  def apply(c: com.typesafe.config.Config): ScalaIssue180Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue180Cfg(
      cfg   = ScalaIssue180Cfg.Cfg(if(c.hasPathOrNull("cfg")) c.getConfig("cfg") else com.typesafe.config.ConfigFactory.parseString("cfg{}"), parentPath + "cfg.", $tsCfgValidator)
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
