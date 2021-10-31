package tscfg.example;

public class JavaIssue33aCfg {
  public final JavaIssue33aCfg.Endpoint endpoint;

  public static class Endpoint {
    public final Endpoint.More more;

    public static class More {
      public final java.lang.String url;

      public More(
          com.typesafe.config.Config c,
          java.lang.String parentPath,
          $TsCfgValidator $tsCfgValidator) {
        this.url = c.hasPathOrNull("url") ? c.getString("url") : "http://example.net";
      }
    }

    public Endpoint(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.more =
          c.hasPathOrNull("more")
              ? new Endpoint.More(c.getConfig("more"), parentPath + "more.", $tsCfgValidator)
              : new Endpoint.More(
                  com.typesafe.config.ConfigFactory.parseString("more{}"),
                  parentPath + "more.",
                  $tsCfgValidator);
    }
  }

  public JavaIssue33aCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.endpoint =
        c.hasPathOrNull("endpoint")
            ? new JavaIssue33aCfg.Endpoint(c.getConfig("endpoint"), "endpoint.", $tsCfgValidator)
            : new JavaIssue33aCfg.Endpoint(
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
