package tscfg.example;

public class JavaIssue30Cfg {
  public final JavaIssue30Cfg.Foo_object foo_object;
  public final int other_stuff;
  public static class Foo_object {
    public final java.lang.String _0;
    public final java.lang.String bar_baz;
    
    public Foo_object(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this._0 = $_reqStr(parentPath, c, "0", $tsCfgValidator);
      this.bar_baz = $_reqStr(parentPath, c, "bar-baz", $tsCfgValidator);
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
  
  public JavaIssue30Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.foo_object = c.hasPathOrNull("foo-object") ? new JavaIssue30Cfg.Foo_object(c.getConfig("foo-object"), parentPath + "foo-object.", $tsCfgValidator) : new JavaIssue30Cfg.Foo_object(com.typesafe.config.ConfigFactory.parseString("foo-object{}"), parentPath + "foo-object.", $tsCfgValidator);
    this.other_stuff = $_reqInt(parentPath, c, "other stuff", $tsCfgValidator);
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
