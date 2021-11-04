package tscfg.example

final case class ScalaIssue47Cfg(
  service : ScalaIssue47Cfg.Service
)
object ScalaIssue47Cfg {
  final case class Service(
    debug    : scala.Boolean,
    doLog    : scala.Boolean,
    factor   : scala.Double,
    poolSize : scala.Int,
    url      : java.lang.String
  )
  object Service {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue47Cfg.Service = {
      ScalaIssue47Cfg.Service(
        debug    = $_reqBln(parentPath, c, "debug", $tsCfgValidator),
        doLog    = $_reqBln(parentPath, c, "doLog", $tsCfgValidator),
        factor   = $_reqDbl(parentPath, c, "factor", $tsCfgValidator),
        poolSize = $_reqInt(parentPath, c, "poolSize", $tsCfgValidator),
        url      = $_reqStr(parentPath, c, "url", $tsCfgValidator)
      )
    }
    private def $_reqBln(parentPath: java.lang.String, c: com.typesafe.config.Config, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.Boolean = {
      if (c == null) false
      else try c.getBoolean(path)
      catch {
        case e:com.typesafe.config.ConfigException =>
          $tsCfgValidator.addBadPath(parentPath + path, e)
          false
      }
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
        
  def apply(c: com.typesafe.config.Config): ScalaIssue47Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue47Cfg(
      service = ScalaIssue47Cfg.Service($_reqConfig(parentPath, c, "service", $tsCfgValidator), parentPath + "service.", $tsCfgValidator)
    )
    $tsCfgValidator.validate()
    $result
  }
  private def $_reqConfig(parentPath: java.lang.String, c: com.typesafe.config.Config, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): com.typesafe.config.Config = {
    if (c == null) null
    else try c.getConfig(path)
    catch {
      case e:com.typesafe.config.ConfigException =>
        $tsCfgValidator.addBadPath(parentPath + path, e)
        null
    }
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
