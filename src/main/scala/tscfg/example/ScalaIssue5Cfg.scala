package tscfg.example

case class ScalaIssue5Cfg(
  foo : ScalaIssue5Cfg.Foo
)
object ScalaIssue5Cfg {
  case class Foo(
    config : ScalaIssue5Cfg.Foo.Config
  )
  object Foo {
    case class Config(
      bar : java.lang.String
    )
    object Config {
      def apply(c: com.typesafe.config.Config): ScalaIssue5Cfg.Foo.Config = {
        ScalaIssue5Cfg.Foo.Config(
          bar = if(c.hasPathOrNull("bar")) c.getString("bar") else "baz"
        )
      }
    }
          
    def apply(c: com.typesafe.config.Config): ScalaIssue5Cfg.Foo = {
      ScalaIssue5Cfg.Foo(
        config = ScalaIssue5Cfg.Foo.Config(c.getConfig("config"))
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue5Cfg = {
    ScalaIssue5Cfg(
      foo = ScalaIssue5Cfg.Foo(c.getConfig("foo"))
    )
  }
}
