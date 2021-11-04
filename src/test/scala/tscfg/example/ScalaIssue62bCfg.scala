package tscfg.example

final case class ScalaIssue62bCfg(
  foo       : ScalaIssue62bCfg.Foo
)
object ScalaIssue62bCfg {
  sealed trait FruitType
  object FruitType {
    object apple extends FruitType
    object banana extends FruitType
    object pineapple extends FruitType
    def $resEnum(name: java.lang.String, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): FruitType = name match {
      case "apple" => FruitType.apple
      case "banana" => FruitType.banana
      case "pineapple" => FruitType.pineapple
      case v => $tsCfgValidator.addInvalidEnumValue(path, v, "FruitType")
                null
    }
  }
  final case class Foo(
    fruit      : ScalaIssue62bCfg.FruitType,
    other      : ScalaIssue62bCfg.Foo.Other,
    someFruits : scala.List[ScalaIssue62bCfg.FruitType]
  )
  object Foo {
    final case class Other(
      aFruit : ScalaIssue62bCfg.FruitType
    )
    object Other {
      def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue62bCfg.Foo.Other = {
        ScalaIssue62bCfg.Foo.Other(
          aFruit = ScalaIssue62bCfg.FruitType.$resEnum(c.getString("aFruit"), parentPath + "aFruit", $tsCfgValidator)
        )
      }
    }
          
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue62bCfg.Foo = {
      ScalaIssue62bCfg.Foo(
        fruit      = ScalaIssue62bCfg.FruitType.$resEnum(c.getString("fruit"), parentPath + "fruit", $tsCfgValidator),
        other      = ScalaIssue62bCfg.Foo.Other(if(c.hasPathOrNull("other")) c.getConfig("other") else com.typesafe.config.ConfigFactory.parseString("other{}"), parentPath + "other.", $tsCfgValidator),
        someFruits = $_LScalaIssue62bCfg_FruitType(c.getList("someFruits"), parentPath, $tsCfgValidator)
      )
    }
    private def $_LScalaIssue62bCfg_FruitType(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[ScalaIssue62bCfg.FruitType] = {
      import scala.jdk.CollectionConverters._
      cl.asScala.map(cv => ScalaIssue62bCfg.FruitType.$resEnum(cv.unwrapped().toString, "?", $tsCfgValidator)).toList
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue62bCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue62bCfg(
      foo       = ScalaIssue62bCfg.Foo(if(c.hasPathOrNull("foo")) c.getConfig("foo") else com.typesafe.config.ConfigFactory.parseString("foo{}"), parentPath + "foo.", $tsCfgValidator)
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
