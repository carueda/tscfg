package tscfg.example;

public class JavaIssue33Cfg {
  public final JavaIssue33Cfg.Endpoint endpoint;

  public static class Endpoint {
    public final java.lang.String url;

    public Endpoint(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.url = c.hasPathOrNull("url") ? c.getString("url") : "http://example.net";
    }
  }

  public JavaIssue33Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.endpoint =
        c.hasPathOrNull("endpoint")
            ? new JavaIssue33Cfg.Endpoint(c.getConfig("endpoint"), "endpoint.", $tsCfgValidator)
            : new JavaIssue33Cfg.Endpoint(
                com.typesafe.config.ConfigFactory.parseString("endpoint{}"),
                "endpoint.",
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
