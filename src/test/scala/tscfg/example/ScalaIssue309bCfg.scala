package tscfg.example

final case class ScalaIssue309bCfg(
  foo           : ScalaIssue309bCfg.SomeExtension
)
object ScalaIssue309bCfg {
  sealed abstract class SomeAbstract (
   val something : java.lang.String
  )
  
  final case class SomeExtension(
    override val something : java.lang.String
  ) extends SomeAbstract(something)
  object SomeExtension {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue309bCfg.SomeExtension = {
      ScalaIssue309bCfg.SomeExtension(
        something = $_reqStr(parentPath, c, "something", $tsCfgValidator)
      )
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
        
  def apply(c: com.typesafe.config.Config): ScalaIssue309bCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue309bCfg(
      foo           = ScalaIssue309bCfg.SomeExtension(if(c.hasPathOrNull("foo")) c.getConfig("foo") else com.typesafe.config.ConfigFactory.parseString("foo{}"), parentPath + "foo.", $tsCfgValidator)
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
