package tscfg.example;

public class JavaIssue31Cfg {
  public final int a;
  public final JavaIssue31Cfg.B b;

  public final int getA() {
    return a;
  }

  public final JavaIssue31Cfg.B getB() {
    return b;
  }

  public static class B {
    public final java.lang.String c;
    public final B.D d;

    public final java.lang.String getC() {
      return c;
    }

    public final B.D getD() {
      return d;
    }

    public static class D {
      public final boolean e;

      public final boolean getE() {
        return e;
      }

      public D(
          com.typesafe.config.Config c,
          java.lang.String parentPath,
          $TsCfgValidator $tsCfgValidator) {
        this.e = c.hasPathOrNull("e") && c.getBoolean("e");
      }
    }

    public B(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.c = $_reqStr(parentPath, c, "c", $tsCfgValidator);
      this.d =
          c.hasPathOrNull("d")
              ? new B.D(c.getConfig("d"), parentPath + "d.", $tsCfgValidator)
              : new B.D(
                  com.typesafe.config.ConfigFactory.parseString("d{}"),
                  parentPath + "d.",
                  $tsCfgValidator);
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

  public JavaIssue31Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.a = $_reqInt(parentPath, c, "a", $tsCfgValidator);
    this.b =
        c.hasPathOrNull("b")
            ? new JavaIssue31Cfg.B(c.getConfig("b"), parentPath + "b.", $tsCfgValidator)
            : new JavaIssue31Cfg.B(
                com.typesafe.config.ConfigFactory.parseString("b{}"),
                parentPath + "b.",
                $tsCfgValidator);
    $tsCfgValidator.validate();
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
