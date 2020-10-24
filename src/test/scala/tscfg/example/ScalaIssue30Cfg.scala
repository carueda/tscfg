package tscfg.example

final case class ScalaIssue30Cfg(
    `foo-object`: ScalaIssue30Cfg.`Foo-object`,
    `other stuff`: scala.Int
)
object ScalaIssue30Cfg {
  final case class `Foo-object`(
      `0`: java.lang.String,
      `bar-baz`: java.lang.String
  )
  object `Foo-object` {
    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue30Cfg.`Foo-object` = {
      ScalaIssue30Cfg.`Foo-object`(
        `0` = $_reqStr(parentPath, c, "0", $tsCfgValidator),
        `bar-baz` = $_reqStr(parentPath, c, "bar-baz", $tsCfgValidator)
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

  def apply(c: com.typesafe.config.Config): ScalaIssue30Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue30Cfg(
      `foo-object` = ScalaIssue30Cfg.`Foo-object`(
        if (c.hasPathOrNull("foo-object")) c.getConfig("foo-object")
        else com.typesafe.config.ConfigFactory.parseString("foo-object{}"),
        parentPath + "foo-object.",
        $tsCfgValidator
      ),
      `other stuff` = $_reqInt(parentPath, c, "other stuff", $tsCfgValidator)
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
