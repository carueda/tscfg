package tscfg.example

/** @param fruit
  *   Use of the enum at first level
  * @param fruits
  *   Use of the enum in a list
  */
final case class ScalaIssue62Cfg(
  fruit     : ScalaIssue62Cfg.FruitType,
  fruits    : scala.List[ScalaIssue62Cfg.FruitType]
)
object ScalaIssue62Cfg {
  
  /** Comment for enum FruitType
    */
  sealed trait FruitType
  
  object FruitType {
      
    /** comment for element apple
      */
    object apple extends FruitType
      
    /** comment for element banana
      */
    object banana extends FruitType
      
    /** comment for element pineapple
      */
    object pineapple extends FruitType
  
    def $resEnum(name: java.lang.String, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): FruitType = name match {
      case "apple" => FruitType.apple
      case "banana" => FruitType.banana
      case "pineapple" => FruitType.pineapple
      case v => $tsCfgValidator.addInvalidEnumValue(path, v, "FruitType")
                null
    }
  }
  def apply(c: com.typesafe.config.Config): ScalaIssue62Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue62Cfg(
      fruit     = ScalaIssue62Cfg.FruitType.$resEnum(c.getString("fruit"), parentPath + "fruit", $tsCfgValidator),
      fruits    = $_LScalaIssue62Cfg_FruitType(c.getList("fruits"), parentPath, $tsCfgValidator)
    )
    $tsCfgValidator.validate()
    $result
  }
  private def $_LScalaIssue62Cfg_FruitType(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[ScalaIssue62Cfg.FruitType] = {
    import scala.jdk.CollectionConverters._
    cl.asScala.map(cv => ScalaIssue62Cfg.FruitType.$resEnum(cv.unwrapped().toString, "?", $tsCfgValidator)).toList
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
