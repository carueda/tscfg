package tscfg.example;

public class JavaIssue127Cfg {
  public final JavaIssue127Cfg.Example example;
  public static class Shared {
    public final java.lang.String c;
    public final int d;
    
    public Shared(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.c = $_reqStr(parentPath, c, "c", $tsCfgValidator);
      this.d = $_reqInt(parentPath, c, "d", $tsCfgValidator);
    }
    private static int $_reqInt(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, $TsCfgValidator $tsCfgValidator) {
      if (c == null) return 0;
      try {
        return c.getInt(path);
      }
      catch(com.typesafe.config.ConfigException e) {
        $tsCfgValidator.addBadPath(parentPath + path, e);
        return 0;
      }
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
  
  public static class Example {
    public final Shared a;
    
    public Example(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.a = c.hasPathOrNull("a") ? new Shared(c.getConfig("a"), parentPath + "a.", $tsCfgValidator) : new Shared(com.typesafe.config.ConfigFactory.parseString("a{}"), parentPath + "a.", $tsCfgValidator);
    }
  }
  
  public JavaIssue127Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.example = c.hasPathOrNull("example") ? new JavaIssue127Cfg.Example(c.getConfig("example"), parentPath + "example.", $tsCfgValidator) : new JavaIssue127Cfg.Example(com.typesafe.config.ConfigFactory.parseString("example{}"), parentPath + "example.", $tsCfgValidator);
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
