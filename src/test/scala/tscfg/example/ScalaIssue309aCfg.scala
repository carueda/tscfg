package tscfg.example

final case class ScalaIssue309aCfg(
  emptyObj : ScalaIssue309aCfg.EmptyObj,
  other    : scala.Int
)
object ScalaIssue309aCfg {
  final case class EmptyObj(
    
  )
  object EmptyObj {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue309aCfg.EmptyObj = {
      ScalaIssue309aCfg.EmptyObj(
        
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue309aCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue309aCfg(
      emptyObj = ScalaIssue309aCfg.EmptyObj(if(c.hasPathOrNull("emptyObj")) c.getConfig("emptyObj") else com.typesafe.config.ConfigFactory.parseString("emptyObj{}"), parentPath + "emptyObj.", $tsCfgValidator),
      other    = $_reqInt(parentPath, c, "other", $tsCfgValidator)
    )
    $tsCfgValidator.validate()
    $result
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
