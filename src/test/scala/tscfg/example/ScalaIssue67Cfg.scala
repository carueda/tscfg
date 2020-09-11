package tscfg.example

final case class ScalaIssue67Cfg(
  test      : ScalaIssue67Cfg.Test
)
object ScalaIssue67Cfg {
  sealed abstract class AbstractA (
   val a : java.lang.String
  )
  
  final case class ImplA(
    override val a : java.lang.String,
    b : java.lang.String
  ) extends AbstractA(a)
  object ImplA {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue67Cfg.ImplA = {
      ScalaIssue67Cfg.ImplA(
        b = $_reqStr(parentPath, c, "b", $tsCfgValidator),
        a = $_reqStr(parentPath, c, "a", $tsCfgValidator)
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
        
  final case class Test(
    impl : ImplA
  )
  object Test {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue67Cfg.Test = {
      ScalaIssue67Cfg.Test(
        impl = ImplA(if(c.hasPathOrNull("impl")) c.getConfig("impl") else com.typesafe.config.ConfigFactory.parseString("impl{}"), parentPath + "impl.", $tsCfgValidator)
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue67Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue67Cfg(
      test      = ScalaIssue67Cfg.Test(if(c.hasPathOrNull("test")) c.getConfig("test") else com.typesafe.config.ConfigFactory.parseString("test{}"), parentPath + "test.", $tsCfgValidator)
    )
    $tsCfgValidator.validate()
    $result
  }
  private final class $TsCfgValidator {
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
