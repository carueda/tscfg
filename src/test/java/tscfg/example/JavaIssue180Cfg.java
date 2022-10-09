package tscfg.example;

public class JavaIssue180Cfg {
  public final JavaIssue180Cfg.Cfg cfg;
  public static class TypeA {
    public final java.lang.String buzz;
    public final java.lang.String fizz;
    
    public TypeA(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.buzz = $_reqStr(parentPath, c, "buzz", $tsCfgValidator);
      this.fizz = $_reqStr(parentPath, c, "fizz", $tsCfgValidator);
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
  
  public static class TypeB {
    public final TypeA bar;
    public final TypeA foo;
    
    public TypeB(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.bar = c.hasPathOrNull("bar") ? new TypeA(c.getConfig("bar"), parentPath + "bar.", $tsCfgValidator) : new TypeA(com.typesafe.config.ConfigFactory.parseString("bar{}"), parentPath + "bar.", $tsCfgValidator);
      this.foo = c.hasPathOrNull("foo") ? new TypeA(c.getConfig("foo"), parentPath + "foo.", $tsCfgValidator) : new TypeA(com.typesafe.config.ConfigFactory.parseString("foo{}"), parentPath + "foo.", $tsCfgValidator);
    }
  }
  
  public static class Cfg {
    public final java.lang.String additionalParam;
    public final TypeB typeB;
    
    public Cfg(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.additionalParam = $_reqStr(parentPath, c, "additionalParam", $tsCfgValidator);
      this.typeB = c.hasPathOrNull("typeB") ? new TypeB(c.getConfig("typeB"), parentPath + "typeB.", $tsCfgValidator) : new TypeB(com.typesafe.config.ConfigFactory.parseString("typeB{}"), parentPath + "typeB.", $tsCfgValidator);
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
  
  public JavaIssue180Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.cfg = c.hasPathOrNull("cfg") ? new JavaIssue180Cfg.Cfg(c.getConfig("cfg"), parentPath + "cfg.", $tsCfgValidator) : new JavaIssue180Cfg.Cfg(com.typesafe.config.ConfigFactory.parseString("cfg{}"), parentPath + "cfg.", $tsCfgValidator);
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
