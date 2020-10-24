package tscfg.example

final case class ScalaIssue19Cfg(
    _$_foo_ : java.lang.String,
    do_log: scala.Boolean
)
object ScalaIssue19Cfg {
  def apply(c: com.typesafe.config.Config): ScalaIssue19Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue19Cfg(
      _$_foo_ = if (c.hasPathOrNull("\"$_foo\"")) c.getString("\"$_foo\"") else "baz",
      do_log = $_reqBln(parentPath, c, "do log", $tsCfgValidator)
    )
    $tsCfgValidator.validate()
    $result
  }
  private def $_reqBln(
      parentPath: java.lang.String,
      c: com.typesafe.config.Config,
      path: java.lang.String,
      $tsCfgValidator: $TsCfgValidator
  ): scala.Boolean = {
    if (c == null) false
    else
      try c.getBoolean(path)
      catch {
        case e: com.typesafe.config.ConfigException =>
          $tsCfgValidator.addBadPath(parentPath + path, e)
          false
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
