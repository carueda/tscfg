package tscfg.example;

public class JavaIssue309aCfg {
    
  /**
   * comment1
   */
  public final JavaIssue309aCfg.EmptyObj emptyObj;
    
  /**
   * comment2
   */
  public final int other;
  
  /**
   * comment1
   */
  public static class EmptyObj {
    
    
    public EmptyObj(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      
    }
  }
  
  public JavaIssue309aCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.emptyObj = c.hasPathOrNull("emptyObj") ? new JavaIssue309aCfg.EmptyObj(c.getConfig("emptyObj"), parentPath + "emptyObj.", $tsCfgValidator) : new JavaIssue309aCfg.EmptyObj(com.typesafe.config.ConfigFactory.parseString("emptyObj{}"), parentPath + "emptyObj.", $tsCfgValidator);
    this.other = $_reqInt(parentPath, c, "other", $tsCfgValidator);
    $tsCfgValidator.validate();
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
