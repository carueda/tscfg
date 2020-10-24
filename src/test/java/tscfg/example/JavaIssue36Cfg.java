package tscfg.example;

public class JavaIssue36Cfg {
  public final JavaIssue36Cfg.Obj obj;

  public static class Obj {
    public final Obj.Baz baz;
    public final Obj.Foo foo;

    public static class Baz {
      public final java.lang.String bar;

      public Baz(
          com.typesafe.config.Config c,
          java.lang.String parentPath,
          $TsCfgValidator $tsCfgValidator) {
        this.bar = $_reqStr(parentPath, c, "bar", $tsCfgValidator);
      }

      private static java.lang.String $_reqStr(
          java.lang.String parentPath,
          com.typesafe.config.Config c,
          java.lang.String path,
          $TsCfgValidator $tsCfgValidator) {
        if (c == null) return null;
        try {
          return c.getString(path);
        } catch (com.typesafe.config.ConfigException e) {
          $tsCfgValidator.addBadPath(parentPath + path, e);
          return null;
        }
      }
    }

    public static class Foo {
      public final int bar;

      public Foo(
          com.typesafe.config.Config c,
          java.lang.String parentPath,
          $TsCfgValidator $tsCfgValidator) {
        this.bar = $_reqInt(parentPath, c, "bar", $tsCfgValidator);
      }

      private static int $_reqInt(
          java.lang.String parentPath,
          com.typesafe.config.Config c,
          java.lang.String path,
          $TsCfgValidator $tsCfgValidator) {
        if (c == null) return 0;
        try {
          return c.getInt(path);
        } catch (com.typesafe.config.ConfigException e) {
          $tsCfgValidator.addBadPath(parentPath + path, e);
          return 0;
        }
      }
    }

    public Obj(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.baz =
          c.hasPathOrNull("baz")
              ? new Obj.Baz(c.getConfig("baz"), parentPath + "baz.", $tsCfgValidator)
              : new Obj.Baz(
                  com.typesafe.config.ConfigFactory.parseString("baz{}"),
                  parentPath + "baz.",
                  $tsCfgValidator);
      this.foo =
          c.hasPathOrNull("foo")
              ? new Obj.Foo(c.getConfig("foo"), parentPath + "foo.", $tsCfgValidator)
              : new Obj.Foo(
                  com.typesafe.config.ConfigFactory.parseString("foo{}"),
                  parentPath + "foo.",
                  $tsCfgValidator);
    }
  }

  public JavaIssue36Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.obj =
        c.hasPathOrNull("obj")
            ? new JavaIssue36Cfg.Obj(c.getConfig("obj"), parentPath + "obj.", $tsCfgValidator)
            : new JavaIssue36Cfg.Obj(
                com.typesafe.config.ConfigFactory.parseString("obj{}"),
                parentPath + "obj.",
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
