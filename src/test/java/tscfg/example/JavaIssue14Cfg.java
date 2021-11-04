package tscfg.example;

public class JavaIssue14Cfg {
  public final JavaIssue14Cfg._0 _0;
  public final java.lang.String _2;
  public static class _0 {
    public final java.lang.String _1;
    
    public _0(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this._1 = c.hasPathOrNull("1") ? c.getString("1") : "bar";
    }
  }
  
  public JavaIssue14Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this._0 = c.hasPathOrNull("0") ? new JavaIssue14Cfg._0(c.getConfig("0"), parentPath + "0.", $tsCfgValidator) : new JavaIssue14Cfg._0(com.typesafe.config.ConfigFactory.parseString("0{}"), parentPath + "0.", $tsCfgValidator);
    this._2 = c.hasPathOrNull("2") ? c.getString("2") : "foo";
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
