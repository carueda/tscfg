// generated by tscfg 0.3.3 on Thu Aug 25 23:25:59 PDT 2016
// source: example/issue11.conf

package tscfg.example

object ScalaIssue11Cfg {
  object Foo {
    def apply(c: scala.Option[com.typesafe.config.Config]): Foo = {
      Foo(
        c.map(c => if(c.hasPathOrNull("clone")) c.getString("clone") else "..").get,
        c.map(c => if(c.hasPathOrNull("finalize")) c.getString("finalize") else "..").get,
        c.map(c => if(c.hasPathOrNull("getClass")) c.getString("getClass") else "..").get,
        c.map(c => if(c.hasPathOrNull("notify")) c.getString("notify") else "..").get,
        c.map(c => if(c.hasPathOrNull("notifyAll")) c.getString("notifyAll") else "..").get,
        c.map(c => if(c.hasPathOrNull("toString")) c.getString("toString") else "..").get,
        c.map(c => if(c.hasPathOrNull("wait")) c.getString("wait") else "..").get
      )
    }
  }
  case class Foo(
    clone_     : java.lang.String,
    finalize_  : java.lang.String,
    getClass_  : java.lang.String,
    notify_    : java.lang.String,
    notifyAll_ : java.lang.String,
    toString_  : java.lang.String,
    wait_      : java.lang.String
  ) {
    override def toString: java.lang.String = toString("")
    def toString(i:java.lang.String): java.lang.String = {
      i+ "clone     = " + this.clone_ + "\n"+
      i+ "finalize  = " + this.finalize_ + "\n"+
      i+ "getClass  = " + this.getClass_ + "\n"+
      i+ "notify    = " + this.notify_ + "\n"+
      i+ "notifyAll = " + this.notifyAll_ + "\n"+
      i+ "toString  = " + this.toString_ + "\n"+
      i+ "wait      = " + this.wait_ + "\n"
    }
  }
  def apply(c: scala.Option[com.typesafe.config.Config]): ScalaIssue11Cfg = {
    ScalaIssue11Cfg(
      Foo(c.map(c => if (c.hasPath("foo")) Some(c.getConfig("foo")) else None).get)
    )
  }
}
case class ScalaIssue11Cfg(
  foo : ScalaIssue11Cfg.Foo
) {
  override def toString: java.lang.String = toString("")
  def toString(i:java.lang.String): java.lang.String = {
    i+ "foo:\n" + this.foo.toString(i+"    ")
  }
}
