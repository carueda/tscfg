package tscfg.example;

public class JavaIssue54exampleECfg {
  public final JavaIssue54exampleECfg.ExampleE exampleE;
  public static class Struct {
    public final int a;
    
    public Struct(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.a = $_reqInt(parentPath, c, "a", $tsCfgValidator);
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
  
  public static class ExampleE {
    public final Struct test;
    
    public ExampleE(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.test = c.hasPathOrNull("test") ? new Struct(c.getConfig("test"), parentPath + "test.", $tsCfgValidator) : new Struct(com.typesafe.config.ConfigFactory.parseString("test{}"), parentPath + "test.", $tsCfgValidator);
    }
  }
  
  public JavaIssue54exampleECfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.exampleE = c.hasPathOrNull("exampleE") ? new JavaIssue54exampleECfg.ExampleE(c.getConfig("exampleE"), parentPath + "exampleE.", $tsCfgValidator) : new JavaIssue54exampleECfg.ExampleE(com.typesafe.config.ConfigFactory.parseString("exampleE{}"), parentPath + "exampleE.", $tsCfgValidator);
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
