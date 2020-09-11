package tscfg.example

final case class ScalaIssue67aCfg(
  test      : ScalaIssue67aCfg.Test
)
object ScalaIssue67aCfg {
  sealed abstract class AbstractA (
   val a : java.lang.String
  )
  
  sealed abstract class AbstractB (
   override val a : java.lang.String,
    val b : java.lang.String
  ) extends AbstractA(a)
  
  final case class ImplB(
    override val a : java.lang.String,
    override val b : java.lang.String,
    c : java.lang.String
  ) extends AbstractB(a,b)
  object ImplB {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue67aCfg.ImplB = {
      ScalaIssue67aCfg.ImplB(
        c = $_reqStr(parentPath, c, "c", $tsCfgValidator),
        a = $_reqStr(parentPath, c, "a", $tsCfgValidator),
        b = $_reqStr(parentPath, c, "b", $tsCfgValidator)
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
    impl : ImplB
  )
  object Test {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue67aCfg.Test = {
      ScalaIssue67aCfg.Test(
        impl = ImplB(if(c.hasPathOrNull("impl")) c.getConfig("impl") else com.typesafe.config.ConfigFactory.parseString("impl{}"), parentPath + "impl.", $tsCfgValidator)
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue67aCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue67aCfg(
      test      = ScalaIssue67aCfg.Test(if(c.hasPathOrNull("test")) c.getConfig("test") else com.typesafe.config.ConfigFactory.parseString("test{}"), parentPath + "test.", $tsCfgValidator)
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
