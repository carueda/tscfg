package tscfg.example

case class ScalaIssue11Cfg(
  foo : ScalaIssue11Cfg.Foo
)

object ScalaIssue11Cfg {
  case class Foo(
    clone_     : java.lang.String,
    finalize_  : java.lang.String,
    getClass_  : java.lang.String,
    notify_    : java.lang.String,
    notifyAll_ : java.lang.String,
    toString_  : java.lang.String,
    wait_      : java.lang.String
  )

  object Foo {
    def apply(c: com.typesafe.config.Config): Foo = {
      Foo(
        c.getString("clone"),
        c.getString("finalize"),
        c.getString("getClass"),
        c.getString("notify"),
        c.getString("notifyAll"),
        c.getString("toString"),
        c.getString("wait")
      )
    }
  }
  def apply(c: com.typesafe.config.Config): ScalaIssue11Cfg = {
    ScalaIssue11Cfg(
      Foo(c.getConfig("foo"))
    )
  }
}
