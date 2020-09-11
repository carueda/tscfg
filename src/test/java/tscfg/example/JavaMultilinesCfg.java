package tscfg.example;

public class JavaMultilinesCfg {
  public final java.lang.String a;
  public final java.lang.String b;
  public final java.lang.String c;
  public final java.lang.String d;
  
  public JavaMultilinesCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.a = c.hasPathOrNull("a") ? c.getString("a") : "some\nlines";
    this.b = c.hasPathOrNull("b") ? c.getString("b") : "other\n\"quoted\"\nlines";
    this.c = c.hasPathOrNull("c") ? c.getString("c") : "'simply quoted' string";
    this.d = c.hasPathOrNull("d") ? c.getString("d") : "some \b control \t \\ chars \r\f";
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
