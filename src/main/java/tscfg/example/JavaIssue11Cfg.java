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
      this.clone = c.getString("clone");
      this.finalize = c.getString("finalize");
      this.getClass = c.getString("getClass");
      this.notify = c.getString("notify");
      this.notifyAll = c.getString("notifyAll");
      this.toString = c.getString("toString");
      this.wait = c.getString("wait");
    }
  }

  public JavaIssue11Cfg(com.typesafe.config.Config c) {
    this.foo = new Foo(_$config(c, "foo"));
  }

  private static com.typesafe.config.Config _$config(com.typesafe.config.Config c, java.lang.String path) {
    return c.hasPathOrNull(path) ? c.getConfig(path) : null;
  }
}

