package tscfg.example

final case class ScalaExample1Cfg(
    bazOptionalWithDefault: java.lang.String,
    bazOptionalWithNoDefault: scala.Option[java.lang.String],
    fooRequired: java.lang.String
)
object ScalaExample1Cfg {
  def apply(c: com.typesafe.config.Config): ScalaExample1Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaExample1Cfg(
      bazOptionalWithDefault =
        if (c.hasPathOrNull("bazOptionalWithDefault")) c.getString("bazOptionalWithDefault")
        else "hello",
      bazOptionalWithNoDefault =
        if (c.hasPathOrNull("bazOptionalWithNoDefault"))
          Some(c.getString("bazOptionalWithNoDefault"))
        else None,
      fooRequired = $_reqStr(parentPath, c, "fooRequired", $tsCfgValidator)
    )
    $tsCfgValidator.validate()
    $result
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
