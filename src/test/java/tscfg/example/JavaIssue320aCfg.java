package tscfg.example;

public class JavaIssue320aCfg {
  public final JavaIssue320aCfg.SomeAB someAB;
  public static class SomeAB {
    public final java.lang.String a;
    public final boolean b;
    
    public SomeAB(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.a = $_reqStr(parentPath, c, "a", $tsCfgValidator);
      this.b = $_reqBln(parentPath, c, "b", $tsCfgValidator);
    }
    private static boolean $_reqBln(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, $TsCfgValidator $tsCfgValidator) {
      if (c == null) return false;
      try {
        return c.getBoolean(path);
      }
      catch(com.typesafe.config.ConfigException e) {
        $tsCfgValidator.addBadPath(parentPath + path, e);
        return false;
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
  
  public JavaIssue320aCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.someAB = c.hasPathOrNull("someAB") ? new JavaIssue320aCfg.SomeAB(c.getConfig("someAB"), parentPath + "someAB.", $tsCfgValidator) : new JavaIssue320aCfg.SomeAB(com.typesafe.config.ConfigFactory.parseString("someAB{}"), parentPath + "someAB.", $tsCfgValidator);
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
