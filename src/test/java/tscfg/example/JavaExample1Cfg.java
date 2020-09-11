package tscfg.example;

public class JavaExample1Cfg {
  public final java.lang.String bazOptionalWithDefault;
  public final java.lang.String bazOptionalWithNoDefault;
  public final java.lang.String fooRequired;
  
  public JavaExample1Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.bazOptionalWithDefault = c.hasPathOrNull("bazOptionalWithDefault") ? c.getString("bazOptionalWithDefault") : "hello";
    this.bazOptionalWithNoDefault = c.hasPathOrNull("bazOptionalWithNoDefault") ? c.getString("bazOptionalWithNoDefault") : null;
    this.fooRequired = $_reqStr(parentPath, c, "fooRequired", $tsCfgValidator);
    $tsCfgValidator.validate();
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
