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
    def apply(c: com.typesafe.config.Config): ScalaIssue11Cfg.Foo = {
      ScalaIssue11Cfg.Foo(
        clone_     = if(c.hasPathOrNull("clone")) c.getString("clone") else "..",
        finalize_  = if(c.hasPathOrNull("finalize")) c.getString("finalize") else "..",
        getClass_  = if(c.hasPathOrNull("getClass")) c.getString("getClass") else "..",
        notify_    = if(c.hasPathOrNull("notify")) c.getString("notify") else "..",
        notifyAll_ = if(c.hasPathOrNull("notifyAll")) c.getString("notifyAll") else "..",
        toString_  = if(c.hasPathOrNull("toString")) c.getString("toString") else "..",
        wait_      = if(c.hasPathOrNull("wait")) c.getString("wait") else ".."
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue11Cfg = {
    ScalaIssue11Cfg(
      foo = ScalaIssue11Cfg.Foo(c.getConfig("foo"))
    )
  }
}
