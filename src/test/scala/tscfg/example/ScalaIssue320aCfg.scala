package tscfg.example

final case class ScalaIssue320aCfg(
  someAB : ScalaIssue320aCfg.SomeAB
)
object ScalaIssue320aCfg {
  final case class SomeAB(
    a : java.lang.String,
    b : scala.Boolean
  )
  object SomeAB {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue320aCfg.SomeAB = {
      ScalaIssue320aCfg.SomeAB(
        a = $_reqStr(parentPath, c, "a", $tsCfgValidator),
        b = $_reqBln(parentPath, c, "b", $tsCfgValidator)
      )
    }
    private def $_reqBln(parentPath: java.lang.String, c: com.typesafe.config.Config, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.Boolean = {
      if (c == null) false
      else try c.getBoolean(path)
      catch {
        case e:com.typesafe.config.ConfigException =>
          $tsCfgValidator.addBadPath(parentPath + path, e)
          false
      }
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
        
  def apply(c: com.typesafe.config.Config): ScalaIssue320aCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue320aCfg(
      someAB = ScalaIssue320aCfg.SomeAB(if(c.hasPathOrNull("someAB")) c.getConfig("someAB") else com.typesafe.config.ConfigFactory.parseString("someAB{}"), parentPath + "someAB.", $tsCfgValidator)
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
