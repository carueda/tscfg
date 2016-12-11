package tscfg.example;

public class JavaIssue11Cfg {
  public final JavaIssue11Cfg.Foo foo;
  public static class Foo {
      public final java.lang.String clone;
        public final java.lang.String finalize;
        public final java.lang.String getClass;
        public final java.lang.String notify;
        public final java.lang.String notifyAll;
        public final java.lang.String toString;
        public final java.lang.String wait;
      
      public Foo(com.typesafe.config.Config c) {
        this.clone = c.hasPathOrNull("clone") ? c.getString("clone") : "..";
          this.finalize = c.hasPathOrNull("finalize") ? c.getString("finalize") : "..";
          this.getClass = c.hasPathOrNull("getClass") ? c.getString("getClass") : "..";
          this.notify = c.hasPathOrNull("notify") ? c.getString("notify") : "..";
          this.notifyAll = c.hasPathOrNull("notifyAll") ? c.getString("notifyAll") : "..";
          this.toString = c.hasPathOrNull("toString") ? c.getString("toString") : "..";
          this.wait = c.hasPathOrNull("wait") ? c.getString("wait") : "..";
      }
    }
    
  public JavaIssue11Cfg(com.typesafe.config.Config c) {
    this.foo = new JavaIssue11Cfg.Foo(c.getConfig("foo"));
  }
}
