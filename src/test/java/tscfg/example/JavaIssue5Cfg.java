package tscfg.example;

public class JavaIssue5Cfg {
  public final JavaIssue5Cfg.Foo foo;

  public static class Foo {
    public final Foo.Config config;

    public static class Config {
      public final java.lang.String bar;

      public Config(
          com.typesafe.config.Config c,
          java.lang.String parentPath,
          $TsCfgValidator $tsCfgValidator) {
        this.bar = c.hasPathOrNull("bar") ? c.getString("bar") : "baz";
      }
    }

    public Foo(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.config =
          c.hasPathOrNull("config")
              ? new Foo.Config(c.getConfig("config"), parentPath + "config.", $tsCfgValidator)
              : new Foo.Config(
                  com.typesafe.config.ConfigFactory.parseString("config{}"),
                  parentPath + "config.",
                  $tsCfgValidator);
    }
  }

  public JavaIssue5Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.foo =
        c.hasPathOrNull("foo")
            ? new JavaIssue5Cfg.Foo(c.getConfig("foo"), parentPath + "foo.", $tsCfgValidator)
            : new JavaIssue5Cfg.Foo(
                com.typesafe.config.ConfigFactory.parseString("foo{}"),
                parentPath + "foo.",
                $tsCfgValidator);
    $tsCfgValidator.validate();
  }

  private static final class $TsCfgValidator {
    private final java.util.List<java.lang.String> badPaths = new java.util.ArrayList<>();

    void addBadPath(java.lang.String path, com.typesafe.config.ConfigException e) {
      badPaths.add("'" + path + "': " + e.getClass().getName() + "(" + e.getMessage() + ")");
    }

    void validate() {
      if (!badPaths.isEmpty()) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("Invalid configuration:");
        for (java.lang.String path : badPaths) {
          sb.append("\n    ").append(path);
        }
        throw new com.typesafe.config.ConfigException(sb.toString()) {};
      }
    }
  }
}
