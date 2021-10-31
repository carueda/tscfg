package tscfg.example;

public class JavaIssue75aCfg {
  public final JavaIssue75aCfg.Simple simple;

  public static class Simple {
    public final java.lang.String foo;
    public final int int_;

    public Simple(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.foo = c.hasPathOrNull("foo") ? c.getString("foo") : "simple";
      this.int_ = $_reqInt(parentPath, c, "int", $tsCfgValidator);
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

  public JavaIssue75aCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.simple =
        c.hasPathOrNull("simple")
            ? new JavaIssue75aCfg.Simple(c.getConfig("simple"), "simple.", $tsCfgValidator)
            : new JavaIssue75aCfg.Simple(
                com.typesafe.config.ConfigFactory.parseString("simple{}"),
                "simple.",
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
