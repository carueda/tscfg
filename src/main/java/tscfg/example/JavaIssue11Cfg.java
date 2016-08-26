// generated by tscfg 0.3.3 on Fri Aug 26 00:45:29 PDT 2016
// source: example/issue11.conf

package tscfg.example;

public class JavaIssue11Cfg {
  public final Foo foo;
  public static class Foo {
    public final java.lang.String clone;
    public final java.lang.String finalize;
    public final java.lang.String getClass;
    public final java.lang.String notify;
    public final java.lang.String notifyAll;
    public final java.lang.String toString;
    public final java.lang.String wait;
    public Foo(com.typesafe.config.Config c) {
      this.clone = c != null && c.hasPathOrNull("clone") ? c.getString("clone") : "..";
      this.finalize = c != null && c.hasPathOrNull("finalize") ? c.getString("finalize") : "..";
      this.getClass = c != null && c.hasPathOrNull("getClass") ? c.getString("getClass") : "..";
      this.notify = c != null && c.hasPathOrNull("notify") ? c.getString("notify") : "..";
      this.notifyAll = c != null && c.hasPathOrNull("notifyAll") ? c.getString("notifyAll") : "..";
      this.toString = c != null && c.hasPathOrNull("toString") ? c.getString("toString") : "..";
      this.wait = c != null && c.hasPathOrNull("wait") ? c.getString("wait") : "..";
    }
    public java.lang.String toString() { return toString(""); }
    public java.lang.String toString(java.lang.String i) {
      return i+ "clone = " + this.clone + "\n"
          +i+ "finalize = " + this.finalize + "\n"
          +i+ "getClass = " + this.getClass + "\n"
          +i+ "notify = " + this.notify + "\n"
          +i+ "notifyAll = " + this.notifyAll + "\n"
          +i+ "toString = " + this.toString + "\n"
          +i+ "wait = " + this.wait + "\n";
    }
    public java.lang.String toPropString() { return toPropString(""); }
    public java.lang.String toPropString(java.lang.String p) {
      return p+ "clone = " + this.clone + "\n"
          +p+ "finalize = " + this.finalize + "\n"
          +p+ "getClass = " + this.getClass + "\n"
          +p+ "notify = " + this.notify + "\n"
          +p+ "notifyAll = " + this.notifyAll + "\n"
          +p+ "toString = " + this.toString + "\n"
          +p+ "wait = " + this.wait + "\n";
    }
  }
  public JavaIssue11Cfg(com.typesafe.config.Config c) {
    this.foo = new Foo(__$config(c, "foo"));
  }
  public java.lang.String toString() { return toString(""); }
  public java.lang.String toString(java.lang.String i) {
    return i+ "foo {\n" + this.foo.toString(i+"    ") +i+ "}\n";
  }
  public java.lang.String toPropString() { return toPropString(""); }
  public java.lang.String toPropString(java.lang.String p) {
    return this.foo.toPropString(p+"foo.");
  }
  private static com.typesafe.config.Config __$config(com.typesafe.config.Config c, java.lang.String path) {
    return c != null && c.hasPath(path) ? c.getConfig(path) : null;
  }
}
