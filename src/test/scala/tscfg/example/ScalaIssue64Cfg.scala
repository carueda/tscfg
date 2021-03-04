package tscfg.example

final case class ScalaIssue64Cfg(
    test: ScalaIssue64Cfg.Test
)
object ScalaIssue64Cfg {
  sealed abstract class BaseModelConfig(
      val scaling: scala.Double,
      val uuids: scala.List[java.lang.String]
  )

  final case class LoadModelConfig(
      override val scaling: scala.Double,
      override val uuids: scala.List[java.lang.String],
      modelBehaviour: java.lang.String,
      reference: java.lang.String
  ) extends BaseModelConfig(scaling, uuids)
  object LoadModelConfig {
    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue64Cfg.LoadModelConfig = {
      ScalaIssue64Cfg.LoadModelConfig(
        modelBehaviour = $_reqStr(parentPath, c, "modelBehaviour", $tsCfgValidator),
        reference = $_reqStr(parentPath, c, "reference", $tsCfgValidator),
        scaling = $_reqDbl(parentPath, c, "scaling", $tsCfgValidator),
        uuids = $_L$_str(c.getList("uuids"), parentPath, $tsCfgValidator)
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

    private def $_reqStr(
        parentPath: java.lang.String,
        c: com.typesafe.config.Config,
        path: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): java.lang.String = {
      if (c == null) null
      else
        try c.getString(path)
        catch {
          case e: com.typesafe.config.ConfigException =>
            $tsCfgValidator.addBadPath(parentPath + path, e)
            null
        }
    }

  }

  final case class Test(
      loadModelConfig: ScalaIssue64Cfg.LoadModelConfig
  )
  object Test {
    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue64Cfg.Test = {
      ScalaIssue64Cfg.Test(
        loadModelConfig = ScalaIssue64Cfg.LoadModelConfig(
          if (c.hasPathOrNull("loadModelConfig")) c.getConfig("loadModelConfig")
          else com.typesafe.config.ConfigFactory.parseString("loadModelConfig{}"),
          parentPath + "loadModelConfig.",
          $tsCfgValidator
        )
      )
    }
  }

  def apply(c: com.typesafe.config.Config): ScalaIssue64Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue64Cfg(
      test = ScalaIssue64Cfg.Test(
        if (c.hasPathOrNull("test")) c.getConfig("test")
        else com.typesafe.config.ConfigFactory.parseString("test{}"),
        parentPath + "test.",
        $tsCfgValidator
      )
    )
    $tsCfgValidator.validate()
    $result
  }

  private def $_L$_str(
      cl: com.typesafe.config.ConfigList,
      parentPath: java.lang.String,
      $tsCfgValidator: $TsCfgValidator
  ): scala.List[java.lang.String] = {
    import scala.jdk.CollectionConverters._
    cl.asScala.map(cv => $_str(cv)).toList
  }
  private def $_expE(cv: com.typesafe.config.ConfigValue, exp: java.lang.String) = {
    val u: Any = cv.unwrapped
    new java.lang.RuntimeException(
      s"${cv.origin.lineNumber}: " +
        "expecting: " + exp + " got: " +
        (if (u.isInstanceOf[java.lang.String]) "\"" + u + "\"" else u)
    )
  }

  private def $_str(cv: com.typesafe.config.ConfigValue): java.lang.String = {
    java.lang.String.valueOf(cv.unwrapped())
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
