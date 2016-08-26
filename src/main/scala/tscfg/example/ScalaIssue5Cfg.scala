// generated by tscfg 0.3.3 on Thu Aug 25 23:25:59 PDT 2016
// source: example/issue5.conf

package tscfg.example

object ScalaIssue5Cfg {
  object Foo {
    object Config {
      def apply(c: scala.Option[com.typesafe.config.Config]): Config = {
        Config(
          c.map(c => if(c.hasPathOrNull("bar")) c.getString("bar") else "baz").get
        )
      }
    }
    case class Config(
      bar : java.lang.String
    ) {
      override def toString: java.lang.String = toString("")
      def toString(i:java.lang.String): java.lang.String = {
        i+ "bar = " + this.bar + "\n"
      }
    }
    def apply(c: scala.Option[com.typesafe.config.Config]): Foo = {
      Foo(
        Config(c.map(c => if (c.hasPath("config")) Some(c.getConfig("config")) else None).get)
      )
    }
  }
  case class Foo(
    config : ScalaIssue5Cfg.Foo.Config
  ) {
    override def toString: java.lang.String = toString("")
    def toString(i:java.lang.String): java.lang.String = {
      i+ "config:\n" + this.config.toString(i+"    ")
    }
  }
  def apply(c: scala.Option[com.typesafe.config.Config]): ScalaIssue5Cfg = {
    ScalaIssue5Cfg(
      Foo(c.map(c => if (c.hasPath("foo")) Some(c.getConfig("foo")) else None).get)
    )
  }
}
case class ScalaIssue5Cfg(
  foo : ScalaIssue5Cfg.Foo
) {
  override def toString: java.lang.String = toString("")
  def toString(i:java.lang.String): java.lang.String = {
    i+ "foo:\n" + this.foo.toString(i+"    ")
  }
}
