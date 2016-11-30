package tscfg.example

case class ScalaIssue5Cfg(
  foo : ScalaIssue5Cfg.Foo
)

object ScalaIssue5Cfg {
  case class Foo(
    config : Foo.Config
  )

  object Foo {
    case class Config(
      bar : java.lang.String
    )

    object Config {
      def apply(c: com.typesafe.config.Config): Config = {
        Config(
          c.getString("bar")
        )
      }
    }
    def apply(c: com.typesafe.config.Config): Foo = {
      Foo(
        Config(c.getConfig("config"))
      )
    }
  }
  def apply(c: com.typesafe.config.Config): ScalaIssue5Cfg = {
    ScalaIssue5Cfg(
      Foo(c.getConfig("foo"))
    )
  }
}
