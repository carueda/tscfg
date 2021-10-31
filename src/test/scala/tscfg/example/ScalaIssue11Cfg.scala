package tscfg.example

final case class ScalaIssue11Cfg(
    foo: ScalaIssue11Cfg.Foo
)
object ScalaIssue11Cfg {
  final case class Foo(
      clone_ : java.lang.String,
      finalize_ : java.lang.String,
      getClass_ : java.lang.String,
      notify_ : java.lang.String,
      notifyAll_ : java.lang.String,
      toString_ : java.lang.String,
      wait_ : java.lang.String
  )
  object Foo {
    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue11Cfg.Foo = {
      ScalaIssue11Cfg.Foo(
        clone_ = if (c.hasPathOrNull("clone")) c.getString("clone") else "..",
        finalize_ = if (c.hasPathOrNull("finalize")) c.getString("finalize") else "..",
        getClass_ = if (c.hasPathOrNull("getClass")) c.getString("getClass") else "..",
        notify_ = if (c.hasPathOrNull("notify")) c.getString("notify") else "..",
        notifyAll_ = if (c.hasPathOrNull("notifyAll")) c.getString("notifyAll") else "..",
        toString_ = if (c.hasPathOrNull("toString")) c.getString("toString") else "..",
        wait_ = if (c.hasPathOrNull("wait")) c.getString("wait") else ".."
      )
    }
  }

  def apply(c: com.typesafe.config.Config): ScalaIssue11Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue11Cfg(
      foo = ScalaIssue11Cfg.Foo(
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
