package tscfg.example

final case class ScalaIssue15dCfg(
    baz: scala.List[scala.List[ScalaIssue15dCfg.Baz$Elm]]
)
object ScalaIssue15dCfg {
  final case class Baz$Elm(
      aa: scala.Option[scala.Boolean],
      dd: scala.Double
  )
  object Baz$Elm {
    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue15dCfg.Baz$Elm = {
      ScalaIssue15dCfg.Baz$Elm(
        aa = if (c.hasPathOrNull("aa")) Some(c.getBoolean("aa")) else None,
        dd = $_reqDbl(parentPath, c, "dd", $tsCfgValidator)
      )
    }
    private def $_reqDbl(
        parentPath: java.lang.String,
        c: com.typesafe.config.Config,
        path: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): scala.Double = {
      if (c == null) 0
      else
        try c.getDouble(path)
        catch {
          case e: com.typesafe.config.ConfigException =>
            $tsCfgValidator.addBadPath(parentPath + path, e)
            0
        }
    }

  }

  def apply(c: com.typesafe.config.Config): ScalaIssue15dCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue15dCfg(
      baz = $_L$_LScalaIssue15dCfg_Baz$Elm(c.getList("baz"), parentPath, $tsCfgValidator)
    )
    $tsCfgValidator.validate()
    $result
  }
  private def $_L$_LScalaIssue15dCfg_Baz$Elm(
      cl: com.typesafe.config.ConfigList,
      parentPath: java.lang.String,
      $tsCfgValidator: $TsCfgValidator
  ): scala.List[scala.List[ScalaIssue15dCfg.Baz$Elm]] = {
    import scala.jdk.CollectionConverters._
    cl.asScala
      .map(cv =>
        $_LScalaIssue15dCfg_Baz$Elm(
          cv.asInstanceOf[com.typesafe.config.ConfigList],
          parentPath,
          $tsCfgValidator
        )
      )
      .toList
  }
  private def $_LScalaIssue15dCfg_Baz$Elm(
      cl: com.typesafe.config.ConfigList,
      parentPath: java.lang.String,
      $tsCfgValidator: $TsCfgValidator
  ): scala.List[ScalaIssue15dCfg.Baz$Elm] = {
    import scala.jdk.CollectionConverters._
    cl.asScala
      .map(cv =>
        ScalaIssue15dCfg.Baz$Elm(
          cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig,
          parentPath,
          $tsCfgValidator
        )
      )
      .toList
  }
  private final class $TsCfgValidator {
    private val badPaths = scala.collection.mutable.ArrayBuffer[java.lang.String]()

    def addBadPath(path: java.lang.String, e: com.typesafe.config.ConfigException): Unit = {
      badPaths += s"'$path': ${e.getClass.getName}(${e.getMessage})"
    }

    def addInvalidEnumValue(
        path: java.lang.String,
        value: java.lang.String,
        enumName: java.lang.String
    ): Unit = {
      badPaths += s"'$path': invalid value $value for enumeration $enumName"
    }

    def validate(): Unit = {
      if (badPaths.nonEmpty) {
        throw new com.typesafe.config.ConfigException(
          badPaths.mkString("Invalid configuration:\n    ", "\n    ", "")
        ) {}
      }
    }
  }
}
