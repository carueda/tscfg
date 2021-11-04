package tscfg.example;

public class JavaExample0Cfg {
  public final JavaExample0Cfg.Service service;
  public static class Service {
    public final boolean debug;
    public final boolean doLog;
    public final double factor;
    public final int poolSize;
    public final java.lang.String url;
    
    public Service(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.debug = !c.hasPathOrNull("debug") || c.getBoolean("debug");
      this.doLog = c.hasPathOrNull("doLog") && c.getBoolean("doLog");
      this.factor = c.hasPathOrNull("factor") ? c.getDouble("factor") : 0.75;
      this.poolSize = c.hasPathOrNull("poolSize") ? c.getInt("poolSize") : 32;
      this.url = c.hasPathOrNull("url") ? c.getString("url") : "http://example.net/rest";
    }
  }
  
  public JavaExample0Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.service = c.hasPathOrNull("service") ? new JavaExample0Cfg.Service(c.getConfig("service"), parentPath + "service.", $tsCfgValidator) : new JavaExample0Cfg.Service(com.typesafe.config.ConfigFactory.parseString("service{}"), parentPath + "service.", $tsCfgValidator);
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
