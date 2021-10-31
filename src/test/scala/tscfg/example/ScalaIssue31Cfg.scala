package tscfg.example

final case class ScalaIssue31Cfg(
    a: scala.Int,
    b: ScalaIssue31Cfg.B
)
object ScalaIssue31Cfg {
  final case class B(
      c: java.lang.String,
      d: ScalaIssue31Cfg.B.D
  )
  object B {
    final case class D(
        e: scala.Boolean
    )
    object D {
      def apply(
          c: com.typesafe.config.Config,
          parentPath: java.lang.String,
          $tsCfgValidator: $TsCfgValidator
      ): ScalaIssue31Cfg.B.D = {
        ScalaIssue31Cfg.B.D(
          e = c.hasPathOrNull("e") && c.getBoolean("e")
        )
      }
    }

    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue31Cfg.B = {
      ScalaIssue31Cfg.B(
        c = $_reqStr(parentPath, c, "c", $tsCfgValidator),
        d = ScalaIssue31Cfg.B.D(
          if (c.hasPathOrNull("d")) c.getConfig("d")
          else com.typesafe.config.ConfigFactory.parseString("d{}"),
          parentPath + "d.",
          $tsCfgValidator
        )
      )
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

  def apply(c: com.typesafe.config.Config): ScalaIssue31Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue31Cfg(
      a = $_reqInt(parentPath, c, "a", $tsCfgValidator),
      b = ScalaIssue31Cfg.B(
        if (c.hasPathOrNull("b")) c.getConfig("b")
        else com.typesafe.config.ConfigFactory.parseString("b{}"),
        parentPath + "b.",
        $tsCfgValidator
      )
    )
    $tsCfgValidator.validate()
    $result
  }
  private def $_reqInt(
      parentPath: java.lang.String,
      c: com.typesafe.config.Config,
      path: java.lang.String,
      $tsCfgValidator: $TsCfgValidator
  ): scala.Int = {
    if (c == null) 0
    else
      try c.getInt(path)
      catch {
        case e: com.typesafe.config.ConfigException =>
          $tsCfgValidator.addBadPath(parentPath + path, e)
          0
      }
  }

  final class $TsCfgValidator {
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
