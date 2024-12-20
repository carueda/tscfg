package tscfg.example;

public class JavaIssue64Cfg {
  public final JavaIssue64Cfg.Test test;
  
  /**
   * Comment for (abstract) BaseModelConfig
   */
  public abstract static class BaseModelConfig {
      
    /**
     * comment for scaling
     */
    public final double scaling;
      
    /**
     * comment for uuids
     */
    public final java.util.List<java.lang.String> uuids;
    
    public BaseModelConfig(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.scaling = $_reqDbl(parentPath, c, "scaling", $tsCfgValidator);
      this.uuids = $_L$_str(c.getList("uuids"), parentPath, $tsCfgValidator);
    }
    private static double $_reqDbl(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, $TsCfgValidator $tsCfgValidator) {
      if (c == null) return 0;
      try {
        return c.getDouble(path);
      }
      catch(com.typesafe.config.ConfigException e) {
        $tsCfgValidator.addBadPath(parentPath + path, e);
        return 0;
      }
    }
  
  }
  
  
  /**
   * Comment for LoadModelConfig
   */
  public static class LoadModelConfig extends BaseModelConfig {
      
    /**
     * comment for modelBehaviour
     */
    public final java.lang.String modelBehaviour;
      
    /**
     * comment for reference
     */
    public final java.lang.String reference;
    
    public LoadModelConfig(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      super(c, parentPath, $tsCfgValidator);
      this.modelBehaviour = $_reqStr(parentPath, c, "modelBehaviour", $tsCfgValidator);
      this.reference = $_reqStr(parentPath, c, "reference", $tsCfgValidator);
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
  
  public static class Test {
    public final LoadModelConfig loadModelConfig;
    
    public Test(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.loadModelConfig = c.hasPathOrNull("loadModelConfig") ? new LoadModelConfig(c.getConfig("loadModelConfig"), parentPath + "loadModelConfig.", $tsCfgValidator) : new LoadModelConfig(com.typesafe.config.ConfigFactory.parseString("loadModelConfig{}"), parentPath + "loadModelConfig.", $tsCfgValidator);
    }
  }
  
  public JavaIssue64Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.test = c.hasPathOrNull("test") ? new JavaIssue64Cfg.Test(c.getConfig("test"), parentPath + "test.", $tsCfgValidator) : new JavaIssue64Cfg.Test(com.typesafe.config.ConfigFactory.parseString("test{}"), parentPath + "test.", $tsCfgValidator);
    $tsCfgValidator.validate();
  }

  private static java.util.List<java.lang.String> $_L$_str(com.typesafe.config.ConfigList cl, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
    java.util.ArrayList<java.lang.String> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add($_str(cv));
    }
    return java.util.Collections.unmodifiableList(al);
  }
  private static java.lang.RuntimeException $_expE(com.typesafe.config.ConfigValue cv, java.lang.String exp) {
    java.lang.Object u = cv.unwrapped();
    return new java.lang.RuntimeException(cv.origin().lineNumber()
      + ": expecting: " +exp + " got: " + (u instanceof java.lang.String ? "\"" +u+ "\"" : u));
  }

  private static java.lang.String $_str(com.typesafe.config.ConfigValue cv) {
    return java.lang.String.valueOf(cv.unwrapped());
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
