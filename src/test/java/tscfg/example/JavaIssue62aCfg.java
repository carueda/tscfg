package tscfg.example;

public class JavaIssue62aCfg {
  public final JavaIssue62aCfg.Foo foo;
  // NOTE: incomplete #62 implementation
  public enum FruitType {
    apple,
    banana,
    pineapple;
  }
  public static class Foo {
    public final FruitType fruit;
    
    public Foo(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.fruit = FruitType.valueOf(c.getString("fruit"));
    }
  }
  
  public JavaIssue62aCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.foo = c.hasPathOrNull("foo") ? new JavaIssue62aCfg.Foo(c.getConfig("foo"), parentPath + "foo.", $tsCfgValidator) : new JavaIssue62aCfg.Foo(com.typesafe.config.ConfigFactory.parseString("foo{}"), parentPath + "foo.", $tsCfgValidator);
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
