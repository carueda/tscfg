package tscfg.example

final case class ScalaMultilinesCfg(
    a: java.lang.String,
    b: java.lang.String,
    c: java.lang.String,
    d: java.lang.String
)
object ScalaMultilinesCfg {
  def apply(c: com.typesafe.config.Config): ScalaMultilinesCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaMultilinesCfg(
      a = if (c.hasPathOrNull("a")) c.getString("a") else "some\nlines",
      b = if (c.hasPathOrNull("b")) c.getString("b") else "other\n\"quoted\"\nlines",
      c = if (c.hasPathOrNull("c")) c.getString("c") else "'simply quoted' string",
      d = if (c.hasPathOrNull("d")) c.getString("d") else "some \b control \t \\ chars \r\f"
    )
    $tsCfgValidator.validate()
    $result
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
