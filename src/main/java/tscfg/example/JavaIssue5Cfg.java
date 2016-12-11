package tscfg.example;

public class JavaIssue5Cfg {
  public final JavaIssue5Cfg.Foo foo;
  public static class Foo {
    public final Foo.Config config;
    public static class Config {
      public final java.lang.String bar;
      
      public Config(com.typesafe.config.Config c) {
        this.bar = c.hasPathOrNull("bar") ? c.getString("bar") : "baz";
      }
    }
    
    public Foo(com.typesafe.config.Config c) {
      this.config = new Foo.Config(c.getConfig("config"));
    }
  }
  
  public JavaIssue5Cfg(com.typesafe.config.Config c) {
    this.foo = new JavaIssue5Cfg.Foo(c.getConfig("foo"));
  }
}
