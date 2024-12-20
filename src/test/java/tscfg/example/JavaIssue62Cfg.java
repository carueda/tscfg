package tscfg.example;

public class JavaIssue62Cfg {
    
  /**
   * Use of the enum at first level
   */
  public final FruitType fruit;
    
  /**
   * Use of the enum in a list
   */
  public final java.util.List<FruitType> fruits;
  
  /**
   * Comment for enum FruitType
   */
  public enum FruitType {
      
    /**
     * comment for element apple
     */
    apple,
      
    /**
     * comment for element banana
     */
    banana,
      
    /**
     * comment for element pineapple
     */
    pineapple;
  }
  public JavaIssue62Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.fruit = FruitType.valueOf(c.getString("fruit"));
    this.fruits = $_LFruitType(c.getList("fruits"), parentPath, $tsCfgValidator);
    $tsCfgValidator.validate();
  }
  private static java.util.List<FruitType> $_LFruitType(com.typesafe.config.ConfigList cl, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
    java.util.ArrayList<FruitType> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add(FruitType.valueOf(cv.unwrapped().toString()));
    }
    return java.util.Collections.unmodifiableList(al);
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
