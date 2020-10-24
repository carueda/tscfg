package tscfg.example

final case class ScalaIssue62aCfg(
    foo: ScalaIssue62aCfg.Foo
)
object ScalaIssue62aCfg {
  // NOTE: incomplete #62 implementation
  sealed trait FruitType
  object FruitType {
    object apple     extends FruitType
    object banana    extends FruitType
    object pineapple extends FruitType
    def $resEnum(
        name: java.lang.String,
        path: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): FruitType = name match {
      case "apple"     => FruitType.apple
      case "banana"    => FruitType.banana
      case "pineapple" => FruitType.pineapple
      case v =>
        $tsCfgValidator.addInvalidEnumValue(path, v, "FruitType")
        null
    }
  }
  final case class Foo(
      fruit: FruitType
  )
  object Foo {
    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue62aCfg.Foo = {
      ScalaIssue62aCfg.Foo(
        fruit = FruitType.$resEnum(c.getString("fruit"), parentPath + "fruit", $tsCfgValidator)
      )
    }
  }

  def apply(c: com.typesafe.config.Config): ScalaIssue62aCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue62aCfg(
      foo = ScalaIssue62aCfg.Foo(
        if (c.hasPathOrNull("foo")) c.getConfig("foo")
        else com.typesafe.config.ConfigFactory.parseString("foo{}"),
        parentPath + "foo.",
        $tsCfgValidator
      )
    )
    $tsCfgValidator.validate()
    $result
  }
  private final class $TsCfgValidator {
    private val badPaths = scala.collection.mutable.ArrayBuffer[java.lang.String]()

    def addBadPath(path: java.lang.String, e: com.typesafe.config.ConfigException): Unit = {
      badPaths += s"'$path': ${e.getClass.getName}(${e.getMessage})"
    }

    def addInvalidEnumValue(
        path: java.lang.String,
        value: java.lang.String,
        enumName: java.lang.String
    ): Unit = {
      badPaths += s"'$path': invalid value $value for enumeration $enumName"
    }

    def validate(): Unit = {
      if (badPaths.nonEmpty) {
        throw new com.typesafe.config.ConfigException(
          badPaths.mkString("Invalid configuration:\n    ", "\n    ", "")
        ) {}
      }
    }
  }
}
