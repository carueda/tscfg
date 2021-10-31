package tscfg.example

final case class ScalaIssue5Cfg(
    foo: ScalaIssue5Cfg.Foo
)
object ScalaIssue5Cfg {
  final case class Foo(
      config: ScalaIssue5Cfg.Foo.Config
  )
  object Foo {
    final case class Config(
        bar: java.lang.String
    )
    object Config {
      def apply(
          c: com.typesafe.config.Config,
          parentPath: java.lang.String,
          $tsCfgValidator: $TsCfgValidator
      ): ScalaIssue5Cfg.Foo.Config = {
        ScalaIssue5Cfg.Foo.Config(
          bar = if (c.hasPathOrNull("bar")) c.getString("bar") else "baz"
        )
      }
    }

    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue5Cfg.Foo = {
      ScalaIssue5Cfg.Foo(
        config = ScalaIssue5Cfg.Foo.Config(
          if (c.hasPathOrNull("config")) c.getConfig("config")
          else com.typesafe.config.ConfigFactory.parseString("config{}"),
          parentPath + "config.",
          $tsCfgValidator
        )
      )
    }
  }

  def apply(c: com.typesafe.config.Config): ScalaIssue5Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue5Cfg(
      foo = ScalaIssue5Cfg.Foo(
        if (c.hasPathOrNull("foo")) c.getConfig("foo")
        else com.typesafe.config.ConfigFactory.parseString("foo{}"),
        parentPath + "foo.",
        $tsCfgValidator
      )
    )
    $tsCfgValidator.validate()
    $result
  }
  final class $TsCfgValidator {
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
