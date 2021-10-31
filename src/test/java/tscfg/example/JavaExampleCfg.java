package tscfg.example;

public class JavaExampleCfg {
  public final JavaExampleCfg.Endpoint endpoint;

  public static class Endpoint {
    public final int intReq;
    public final Endpoint.Interface_ interface_;
    public final java.lang.String path;
    public final java.lang.Integer serial;
    public final java.lang.String url;

    public static class Interface_ {
      public final int port;
      public final java.lang.String type;

      public Interface_(
          com.typesafe.config.Config c,
          java.lang.String parentPath,
          $TsCfgValidator $tsCfgValidator) {
        this.port = c.hasPathOrNull("port") ? c.getInt("port") : 8080;
        this.type = c.hasPathOrNull("type") ? c.getString("type") : null;
      }
    }

    public Endpoint(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.intReq = $_reqInt(parentPath, c, "intReq", $tsCfgValidator);
      this.interface_ =
          c.hasPathOrNull("interface")
              ? new Endpoint.Interface_(
                  c.getConfig("interface"), parentPath + "interface.", $tsCfgValidator)
              : new Endpoint.Interface_(
                  com.typesafe.config.ConfigFactory.parseString("interface{}"),
                  parentPath + "interface.",
                  $tsCfgValidator);
      this.path = $_reqStr(parentPath, c, "path", $tsCfgValidator);
      this.serial = c.hasPathOrNull("serial") ? c.getInt("serial") : null;
      this.url = c.hasPathOrNull("url") ? c.getString("url") : "http://example.net";
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

  public JavaExampleCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.endpoint =
        c.hasPathOrNull("endpoint")
            ? new JavaExampleCfg.Endpoint(c.getConfig("endpoint"), "endpoint.", $tsCfgValidator)
            : new JavaExampleCfg.Endpoint(
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
