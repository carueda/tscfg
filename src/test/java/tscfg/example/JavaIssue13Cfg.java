package tscfg.example;

public class JavaIssue13Cfg {
  public final JavaIssue13Cfg.Issue issue;

  public static class Issue {
    public final java.lang.String optionalFoo;

    public Issue(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.optionalFoo = c.hasPathOrNull("optionalFoo") ? c.getString("optionalFoo") : null;
    }
  }

  public JavaIssue13Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.issue =
        c.hasPathOrNull("issue")
            ? new JavaIssue13Cfg.Issue(c.getConfig("issue"), "issue.", $tsCfgValidator)
            : new JavaIssue13Cfg.Issue(
                com.typesafe.config.ConfigFactory.parseString("issue{}"),
                "issue.",
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
