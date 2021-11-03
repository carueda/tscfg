package tscfg.example

final case class ScalaIssue54exampleDCfg(
  exampleD : ScalaIssue54exampleDCfg.ExampleD
)
object ScalaIssue54exampleDCfg {
  final case class Struct(
    a : scala.Int
  )
  object Struct {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue54exampleDCfg.Struct = {
      ScalaIssue54exampleDCfg.Struct(
        a = $_reqInt(parentPath, c, "a", $tsCfgValidator)
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
  
  }
        
  final case class ExampleD(
    test : ScalaIssue54exampleDCfg.Struct
  )
  object ExampleD {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue54exampleDCfg.ExampleD = {
      ScalaIssue54exampleDCfg.ExampleD(
        test = ScalaIssue54exampleDCfg.Struct(if(c.hasPathOrNull("test")) c.getConfig("test") else com.typesafe.config.ConfigFactory.parseString("test{}"), parentPath + "test.", $tsCfgValidator)
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue54exampleDCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue54exampleDCfg(
      exampleD = ScalaIssue54exampleDCfg.ExampleD(if(c.hasPathOrNull("exampleD")) c.getConfig("exampleD") else com.typesafe.config.ConfigFactory.parseString("exampleD{}"), parentPath + "exampleD.", $tsCfgValidator)
    )
    $tsCfgValidator.validate()
    $result
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
