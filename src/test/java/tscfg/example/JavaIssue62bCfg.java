package tscfg.example;

public class JavaIssue62bCfg {
  public final JavaIssue62bCfg.Foo foo;

  public enum FruitType {
    apple,
    banana,
    pineapple;
  }

  public static class Foo {
    public final FruitType fruit;
    public final Foo.Other other;
    public final java.util.List<FruitType> someFruits;

    public static class Other {
      public final FruitType aFruit;

      public Other(
          com.typesafe.config.Config c,
          java.lang.String parentPath,
          $TsCfgValidator $tsCfgValidator) {
        this.aFruit = FruitType.valueOf(c.getString("aFruit"));
      }
    }

    public Foo(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.fruit = FruitType.valueOf(c.getString("fruit"));
      this.other =
          c.hasPathOrNull("other")
              ? new Foo.Other(c.getConfig("other"), parentPath + "other.", $tsCfgValidator)
              : new Foo.Other(
                  com.typesafe.config.ConfigFactory.parseString("other{}"),
                  parentPath + "other.",
                  $tsCfgValidator);
      this.someFruits = $_LFruitType(c.getList("someFruits"), parentPath, $tsCfgValidator);
    }

    private static java.util.List<FruitType> $_LFruitType(
        com.typesafe.config.ConfigList cl,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      java.util.ArrayList<FruitType> al = new java.util.ArrayList<>();
      for (com.typesafe.config.ConfigValue cv : cl) {
        al.add(FruitType.valueOf(cv.unwrapped().toString()));
      }
      return java.util.Collections.unmodifiableList(al);
    }
  }

  public JavaIssue62bCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.foo =
        c.hasPathOrNull("foo")
            ? new JavaIssue62bCfg.Foo(c.getConfig("foo"), parentPath + "foo.", $tsCfgValidator)
            : new JavaIssue62bCfg.Foo(
                com.typesafe.config.ConfigFactory.parseString("foo{}"),
                parentPath + "foo.",
                $tsCfgValidator);
    $tsCfgValidator.validate();
  }

  private static final class $TsCfgValidator {
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
