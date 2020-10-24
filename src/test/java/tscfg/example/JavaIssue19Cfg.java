package tscfg.example;

public class JavaIssue19Cfg {
  public final java.lang.String _$_foo_;
  public final boolean do_log;

  public JavaIssue19Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this._$_foo_ = c.hasPathOrNull("\"$_foo\"") ? c.getString("\"$_foo\"") : "baz";
    this.do_log = $_reqBln(parentPath, c, "do log", $tsCfgValidator);
    $tsCfgValidator.validate();
  }

  private static boolean $_reqBln(
      java.lang.String parentPath,
      com.typesafe.config.Config c,
      java.lang.String path,
      $TsCfgValidator $tsCfgValidator) {
    if (c == null) return false;
    try {
      return c.getBoolean(path);
    } catch (com.typesafe.config.ConfigException e) {
      $tsCfgValidator.addBadPath(parentPath + path, e);
      return false;
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
