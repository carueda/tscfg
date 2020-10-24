package tscfg.example;

public class JavaIssue15dCfg {
  public final java.util.List<java.util.List<JavaIssue15dCfg.Baz$Elm>> baz;

  public static class Baz$Elm {
    public final java.lang.Boolean aa;
    public final double dd;

    public Baz$Elm(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.aa = c.hasPathOrNull("aa") ? c.getBoolean("aa") : null;
      this.dd = $_reqDbl(parentPath, c, "dd", $tsCfgValidator);
    }

    private static double $_reqDbl(
        java.lang.String parentPath,
        com.typesafe.config.Config c,
        java.lang.String path,
        $TsCfgValidator $tsCfgValidator) {
      if (c == null) return 0;
      try {
        return c.getDouble(path);
      } catch (com.typesafe.config.ConfigException e) {
        $tsCfgValidator.addBadPath(parentPath + path, e);
        return 0;
      }
    }
  }

  public JavaIssue15dCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.baz = $_L$_LJavaIssue15dCfg_Baz$Elm(c.getList("baz"), parentPath, $tsCfgValidator);
    $tsCfgValidator.validate();
  }

  private static java.util.List<java.util.List<JavaIssue15dCfg.Baz$Elm>>
      $_L$_LJavaIssue15dCfg_Baz$Elm(
          com.typesafe.config.ConfigList cl,
          java.lang.String parentPath,
          $TsCfgValidator $tsCfgValidator) {
    java.util.ArrayList<java.util.List<JavaIssue15dCfg.Baz$Elm>> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv : cl) {
      al.add(
          $_LJavaIssue15dCfg_Baz$Elm(
              (com.typesafe.config.ConfigList) cv, parentPath, $tsCfgValidator));
    }
    return java.util.Collections.unmodifiableList(al);
  }

  private static java.util.List<JavaIssue15dCfg.Baz$Elm> $_LJavaIssue15dCfg_Baz$Elm(
      com.typesafe.config.ConfigList cl,
      java.lang.String parentPath,
      $TsCfgValidator $tsCfgValidator) {
    java.util.ArrayList<JavaIssue15dCfg.Baz$Elm> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv : cl) {
      al.add(
          new JavaIssue15dCfg.Baz$Elm(
              ((com.typesafe.config.ConfigObject) cv).toConfig(), parentPath, $tsCfgValidator));
    }
    return java.util.Collections.unmodifiableList(al);
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
