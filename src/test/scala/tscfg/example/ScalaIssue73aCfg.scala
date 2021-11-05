package tscfg.example

final case class ScalaIssue73aCfg(
  test      : ScalaIssue73aCfg.Test
)
object ScalaIssue73aCfg {
  sealed abstract class AbstractA (
   val a : java.lang.String
  ) extends java.io.Serializable
  
  final case class ImplA(
    override val a : java.lang.String,
    b : java.lang.String
  ) extends AbstractA(a)
  object ImplA {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue73aCfg.ImplA = {
      ScalaIssue73aCfg.ImplA(
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
    impl : ScalaIssue73aCfg.ImplA
  )
  object Test {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue73aCfg.Test = {
      ScalaIssue73aCfg.Test(
        impl = ScalaIssue73aCfg.ImplA(if(c.hasPathOrNull("impl")) c.getConfig("impl") else com.typesafe.config.ConfigFactory.parseString("impl{}"), parentPath + "impl.", $tsCfgValidator)
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue73aCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue73aCfg(
      test      = ScalaIssue73aCfg.Test(if(c.hasPathOrNull("test")) c.getConfig("test") else com.typesafe.config.ConfigFactory.parseString("test{}"), parentPath + "test.", $tsCfgValidator)
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
