package tscfg.example

final case class ScalaIssue15Cfg(
  positions : scala.List[ScalaIssue15Cfg.Positions$Elm]
)
object ScalaIssue15Cfg {
  final case class Positions$Elm(
    numbers   : scala.List[scala.Int],
    positions : scala.List[scala.List[ScalaIssue15Cfg.Positions$Elm.Positions$Elm]]
  )
  object Positions$Elm {
    final case class Positions$Elm(
      other : scala.Int,
      stuff : java.lang.String
    )
    object Positions$Elm {
      def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue15Cfg.Positions$Elm.Positions$Elm = {
        ScalaIssue15Cfg.Positions$Elm.Positions$Elm(
          other = $_reqInt(parentPath, c, "other", $tsCfgValidator),
          stuff = $_reqStr(parentPath, c, "stuff", $tsCfgValidator)
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
          
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue15Cfg.Positions$Elm = {
      ScalaIssue15Cfg.Positions$Elm(
        numbers   = $_L$_int(c.getList("numbers"), parentPath, $tsCfgValidator),
        positions = $_L$_LScalaIssue15Cfg_Positions$Elm_Positions$Elm(c.getList("positions"), parentPath, $tsCfgValidator)
      )
    }
    private def $_L$_LScalaIssue15Cfg_Positions$Elm_Positions$Elm(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[scala.List[ScalaIssue15Cfg.Positions$Elm.Positions$Elm]] = {
      import scala.jdk.CollectionConverters._
      cl.asScala.map(cv => $_LScalaIssue15Cfg_Positions$Elm_Positions$Elm(cv.asInstanceOf[com.typesafe.config.ConfigList], parentPath, $tsCfgValidator)).toList
    }
    private def $_LScalaIssue15Cfg_Positions$Elm_Positions$Elm(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[ScalaIssue15Cfg.Positions$Elm.Positions$Elm] = {
      import scala.jdk.CollectionConverters._
      cl.asScala.map(cv => ScalaIssue15Cfg.Positions$Elm.Positions$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig, parentPath, $tsCfgValidator)).toList
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue15Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue15Cfg(
      positions = $_LScalaIssue15Cfg_Positions$Elm(c.getList("positions"), parentPath, $tsCfgValidator)
    )
    $tsCfgValidator.validate()
    $result
  }
  private def $_LScalaIssue15Cfg_Positions$Elm(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[ScalaIssue15Cfg.Positions$Elm] = {
    import scala.jdk.CollectionConverters._
    cl.asScala.map(cv => ScalaIssue15Cfg.Positions$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig, parentPath, $tsCfgValidator)).toList
  }

  private def $_L$_int(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[scala.Int] = {
    import scala.jdk.CollectionConverters._
    cl.asScala.map(cv => $_int(cv)).toList
  }
  private def $_expE(cv:com.typesafe.config.ConfigValue, exp:java.lang.String) = {
    val u: Any = cv.unwrapped
    new java.lang.RuntimeException(s"${cv.origin.lineNumber}: " +
      "expecting: " + exp + " got: " +
      (if (u.isInstanceOf[java.lang.String]) "\"" + u + "\"" else u))
  }

  private def $_int(cv:com.typesafe.config.ConfigValue): scala.Int = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) ||
      !u.isInstanceOf[Integer]) throw $_expE(cv, "integer")
    u.asInstanceOf[Integer]
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
