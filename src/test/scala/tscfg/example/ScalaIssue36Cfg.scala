package tscfg.example

final case class ScalaIssue36Cfg(
    obj: ScalaIssue36Cfg.Obj
)
object ScalaIssue36Cfg {
  final case class Obj(
      baz: ScalaIssue36Cfg.Obj.Baz,
      foo: ScalaIssue36Cfg.Obj.Foo
  )
  object Obj {
    final case class Baz(
        bar: java.lang.String
    )
    object Baz {
      def apply(
          c: com.typesafe.config.Config,
          parentPath: java.lang.String,
          $tsCfgValidator: $TsCfgValidator
      ): ScalaIssue36Cfg.Obj.Baz = {
        ScalaIssue36Cfg.Obj.Baz(
          bar = $_reqStr(parentPath, c, "bar", $tsCfgValidator)
        )
      }
      private def $_reqStr(
          parentPath: java.lang.String,
          c: com.typesafe.config.Config,
          path: java.lang.String,
          $tsCfgValidator: $TsCfgValidator
      ): java.lang.String = {
        if (c == null) null
        else
          try c.getString(path)
          catch {
            case e: com.typesafe.config.ConfigException =>
              $tsCfgValidator.addBadPath(parentPath + path, e)
              null
          }
      }

    }

    final case class Foo(
        bar: scala.Int
    )
    object Foo {
      def apply(
          c: com.typesafe.config.Config,
          parentPath: java.lang.String,
          $tsCfgValidator: $TsCfgValidator
      ): ScalaIssue36Cfg.Obj.Foo = {
        ScalaIssue36Cfg.Obj.Foo(
          bar = $_reqInt(parentPath, c, "bar", $tsCfgValidator)
        )
      }
      private def $_reqInt(
          parentPath: java.lang.String,
          c: com.typesafe.config.Config,
          path: java.lang.String,
          $tsCfgValidator: $TsCfgValidator
      ): scala.Int = {
        if (c == null) 0
        else
          try c.getInt(path)
          catch {
            case e: com.typesafe.config.ConfigException =>
              $tsCfgValidator.addBadPath(parentPath + path, e)
              0
          }
      }

    }

    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue36Cfg.Obj = {
      ScalaIssue36Cfg.Obj(
        baz = ScalaIssue36Cfg.Obj.Baz(
          if (c.hasPathOrNull("baz")) c.getConfig("baz")
          else com.typesafe.config.ConfigFactory.parseString("baz{}"),
          parentPath + "baz.",
          $tsCfgValidator
        ),
        foo = ScalaIssue36Cfg.Obj.Foo(
          if (c.hasPathOrNull("foo")) c.getConfig("foo")
          else com.typesafe.config.ConfigFactory.parseString("foo{}"),
          parentPath + "foo.",
          $tsCfgValidator
        )
      )
    }
  }

  def apply(c: com.typesafe.config.Config): ScalaIssue36Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue36Cfg(
      obj = ScalaIssue36Cfg.Obj(
        if (c.hasPathOrNull("obj")) c.getConfig("obj")
        else com.typesafe.config.ConfigFactory.parseString("obj{}"),
        parentPath + "obj.",
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
