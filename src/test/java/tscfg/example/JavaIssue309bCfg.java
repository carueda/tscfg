package tscfg.example;

public class JavaIssue309bCfg {
  public final SomeExtension foo;
  public abstract static class SomeAbstract {
    public final java.lang.String something;
    
    public SomeAbstract(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.something = $_reqStr(parentPath, c, "something", $tsCfgValidator);
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
  
  public static class SomeExtension extends SomeAbstract {
    
    
    public SomeExtension(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      super(c, parentPath, $tsCfgValidator);
      
    }
  }
  
  public JavaIssue309bCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.foo = c.hasPathOrNull("foo") ? new SomeExtension(c.getConfig("foo"), parentPath + "foo.", $tsCfgValidator) : new SomeExtension(com.typesafe.config.ConfigFactory.parseString("foo{}"), parentPath + "foo.", $tsCfgValidator);
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
