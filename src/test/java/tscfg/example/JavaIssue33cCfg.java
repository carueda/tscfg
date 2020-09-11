package tscfg.example;

public class JavaIssue33cCfg {
  public final JavaIssue33cCfg.Endpoint endpoint;
  public static class Endpoint {
    public final Endpoint.OptObj optObj;
    public final java.lang.String req;
    public static class OptObj {
      public final java.lang.String key;
      
      public OptObj(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
        this.key = c.hasPathOrNull("key") ? c.getString("key") : "bar";
      }
    }
    
    public Endpoint(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.optObj = c.hasPathOrNull("optObj") ? new Endpoint.OptObj(c.getConfig("optObj"), parentPath + "optObj.", $tsCfgValidator) : new Endpoint.OptObj(com.typesafe.config.ConfigFactory.parseString("optObj{}"), parentPath + "optObj.", $tsCfgValidator);
      this.req = $_reqStr(parentPath, c, "req", $tsCfgValidator);
    }
    private static java.lang.String $_reqStr(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, $TsCfgValidator $tsCfgValidator) {
      if (c == null) return null;
      try {
        return c.getString(path);
      }
      catch(com.typesafe.config.ConfigException e) {
        $tsCfgValidator.addBadPath(parentPath + path, e);
        return null;
      }
    }
  
  }
  
  public JavaIssue33cCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.endpoint = c.hasPathOrNull("endpoint") ? new JavaIssue33cCfg.Endpoint(c.getConfig("endpoint"), parentPath + "endpoint.", $tsCfgValidator) : new JavaIssue33cCfg.Endpoint(com.typesafe.config.ConfigFactory.parseString("endpoint{}"), parentPath + "endpoint.", $tsCfgValidator);
    $tsCfgValidator.validate();
  }
  private static final class $TsCfgValidator  {
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
