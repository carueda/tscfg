package tscfg.example

final case class ScalaIssue71aCfg(
  example : ScalaIssue71aCfg.Example
)
object ScalaIssue71aCfg {
  final case class Shared(
    c : java.lang.String,
    d : ScalaIssue71aCfg.Shared.D
  )
  object Shared {
    final case class D(
      e : scala.Int
    )
    object D {
      def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue71aCfg.Shared.D = {
        ScalaIssue71aCfg.Shared.D(
          e = $_reqInt(parentPath, c, "e", $tsCfgValidator)
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
          
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue71aCfg.Shared = {
      ScalaIssue71aCfg.Shared(
        c = $_reqStr(parentPath, c, "c", $tsCfgValidator),
        d = ScalaIssue71aCfg.Shared.D(if(c.hasPathOrNull("d")) c.getConfig("d") else com.typesafe.config.ConfigFactory.parseString("d{}"), parentPath + "d.", $tsCfgValidator)
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
        
  final case class Example(
    a : ScalaIssue71aCfg.Shared,
    b : scala.List[ScalaIssue71aCfg.Shared]
  )
  object Example {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue71aCfg.Example = {
      ScalaIssue71aCfg.Example(
        a = ScalaIssue71aCfg.Shared(if(c.hasPathOrNull("a")) c.getConfig("a") else com.typesafe.config.ConfigFactory.parseString("a{}"), parentPath + "a.", $tsCfgValidator),
        b = $_LScalaIssue71aCfg_Shared(c.getList("b"), parentPath, $tsCfgValidator)
      )
    }
    private def $_LScalaIssue71aCfg_Shared(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[ScalaIssue71aCfg.Shared] = {
      import scala.jdk.CollectionConverters._
      cl.asScala.map(cv => ScalaIssue71aCfg.Shared(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig, parentPath, $tsCfgValidator)).toList
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue71aCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue71aCfg(
      example = ScalaIssue71aCfg.Example(if(c.hasPathOrNull("example")) c.getConfig("example") else com.typesafe.config.ConfigFactory.parseString("example{}"), parentPath + "example.", $tsCfgValidator)
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
