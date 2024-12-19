package tscfg.example;

public record JavaIssue75Cfg(
  JavaIssue75Cfg.Simple simple
) {
  static final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();

  public record Simple(
    java.lang.String foo,
    int int_
  ) {
    
    public Simple(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this(
        c.hasPathOrNull("foo") ? c.getString("foo") : "simple",
        $_reqInt(parentPath, c, "int", $tsCfgValidator)
      );
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
  
  }
  
  public JavaIssue75Cfg(com.typesafe.config.Config c) {
    this(
      c.hasPathOrNull("simple") ? new JavaIssue75Cfg.Simple(c.getConfig("simple"), "simple.", $tsCfgValidator) : new JavaIssue75Cfg.Simple(com.typesafe.config.ConfigFactory.parseString("simple{}"), "simple.", $tsCfgValidator)
    );
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
