package tscfg.example

final case class ScalaIssue54exampleECfg(
    exampleE: ScalaIssue54exampleECfg.ExampleE
)
object ScalaIssue54exampleECfg {
  final case class Struct(
      a: scala.Int
  )
  object Struct {
    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue54exampleECfg.Struct = {
      ScalaIssue54exampleECfg.Struct(
        a = $_reqInt(parentPath, c, "a", $tsCfgValidator)
      )
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

  }

  final case class ExampleE(
      test: ScalaIssue54exampleECfg.Struct
  )
  object ExampleE {
    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue54exampleECfg.ExampleE = {
      ScalaIssue54exampleECfg.ExampleE(
        test = ScalaIssue54exampleECfg.Struct(
          if (c.hasPathOrNull("test")) c.getConfig("test")
          else com.typesafe.config.ConfigFactory.parseString("test{}"),
          parentPath + "test.",
          $tsCfgValidator
        )
      )
    }
  }

  def apply(c: com.typesafe.config.Config): ScalaIssue54exampleECfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue54exampleECfg(
      exampleE = ScalaIssue54exampleECfg.ExampleE(
        if (c.hasPathOrNull("exampleE")) c.getConfig("exampleE")
        else com.typesafe.config.ConfigFactory.parseString("exampleE{}"),
        parentPath + "exampleE.",
        $tsCfgValidator
      )
    )
    $tsCfgValidator.validate()
    $result
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
