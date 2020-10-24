package tscfg.example;

public class JavaIssue33bCfg {
  public final JavaIssue33bCfg.Endpoint endpoint;

  public static class Endpoint {
    public final Endpoint.Baz baz;
    public final java.lang.Integer foo;
    public final java.lang.String url;

    public static class Baz {
      public final java.lang.String key;

      public Baz(
          com.typesafe.config.Config c,
          java.lang.String parentPath,
          $TsCfgValidator $tsCfgValidator) {
        this.key = c.hasPathOrNull("key") ? c.getString("key") : "bar";
      }
    }

    public Endpoint(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.baz =
          c.hasPathOrNull("baz")
              ? new Endpoint.Baz(c.getConfig("baz"), parentPath + "baz.", $tsCfgValidator)
              : new Endpoint.Baz(
                  com.typesafe.config.ConfigFactory.parseString("baz{}"),
                  parentPath + "baz.",
                  $tsCfgValidator);
      this.foo = c.hasPathOrNull("foo") ? c.getInt("foo") : null;
      this.url = c.hasPathOrNull("url") ? c.getString("url") : "http://example.net";
    }
  }

  public JavaIssue33bCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.endpoint =
        c.hasPathOrNull("endpoint")
            ? new JavaIssue33bCfg.Endpoint(
                c.getConfig("endpoint"), parentPath + "endpoint.", $tsCfgValidator)
            : new JavaIssue33bCfg.Endpoint(
                com.typesafe.config.ConfigFactory.parseString("endpoint{}"),
                parentPath + "endpoint.",
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
