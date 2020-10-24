package tscfg.example;

public class JavaIssue59Cfg {
  public final java.util.List<java.lang.String> foolist;

  public JavaIssue59Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.foolist = $_L$_str(c.getList("foolist"), parentPath, $tsCfgValidator);
    $tsCfgValidator.validate();
  }

  private static java.util.List<java.lang.String> $_L$_str(
      com.typesafe.config.ConfigList cl,
      java.lang.String parentPath,
      $TsCfgValidator $tsCfgValidator) {
    java.util.ArrayList<java.lang.String> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv : cl) {
      al.add($_str(cv));
    }
    return java.util.Collections.unmodifiableList(al);
  }

  private static java.lang.RuntimeException $_expE(
      com.typesafe.config.ConfigValue cv, java.lang.String exp) {
    java.lang.Object u = cv.unwrapped();
    return new java.lang.RuntimeException(
        cv.origin().lineNumber()
            + ": expecting: "
            + exp
            + " got: "
            + (u instanceof java.lang.String ? "\"" + u + "\"" : u));
  }

  private static java.lang.String $_str(com.typesafe.config.ConfigValue cv) {
    return java.lang.String.valueOf(cv.unwrapped());
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
