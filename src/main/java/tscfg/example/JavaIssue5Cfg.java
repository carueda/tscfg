package tscfg.example;

public class JavaIssue5Cfg {
  public final Foo foo;

  public static class Foo {
    public final Config config;

    public static class Config {
      public final java.lang.String bar;

      public Config(com.typesafe.config.Config c) {
        this.bar = c.getString("bar");
      }
    }

    public Foo(com.typesafe.config.Config c) {
      this.config = new Config(_$config(c, "config"));
    }
  }

  public JavaIssue5Cfg(com.typesafe.config.Config c) {
    this.foo = new Foo(_$config(c, "foo"));
  }

  private static com.typesafe.config.Config _$config(com.typesafe.config.Config c, java.lang.String path) {
    return c.hasPathOrNull(path) ? c.getConfig(path) : null;
  }
}

