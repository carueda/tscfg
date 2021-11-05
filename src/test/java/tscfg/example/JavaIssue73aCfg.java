package tscfg.example;

public class JavaIssue73aCfg {
  public final JavaIssue73aCfg.Test test;
  public abstract static class AbstractA extends java.lang.Object {
    public final java.lang.String a;
    
    public AbstractA(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.a = $_reqStr(parentPath, c, "a", $tsCfgValidator);
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
  
  public static class ImplA extends AbstractA {
    public final java.lang.String b;
    
    public ImplA(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      super(c, parentPath, $tsCfgValidator);
      this.b = $_reqStr(parentPath, c, "b", $tsCfgValidator);
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
    public final ImplA impl;
    
    public Test(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.impl = c.hasPathOrNull("impl") ? new ImplA(c.getConfig("impl"), parentPath + "impl.", $tsCfgValidator) : new ImplA(com.typesafe.config.ConfigFactory.parseString("impl{}"), parentPath + "impl.", $tsCfgValidator);
    }
  }
  
  public JavaIssue73aCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.test = c.hasPathOrNull("test") ? new JavaIssue73aCfg.Test(c.getConfig("test"), parentPath + "test.", $tsCfgValidator) : new JavaIssue73aCfg.Test(com.typesafe.config.ConfigFactory.parseString("test{}"), parentPath + "test.", $tsCfgValidator);
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
