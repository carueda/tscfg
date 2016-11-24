package tscfg.example;

public class JavaIssue15bCfg {
    public final java.util.List<java.util.List<java.lang.Integer>> numbers;

    public JavaIssue15bCfg(com.typesafe.config.Config c) {
        this.numbers = $list$list$int(c.getList("numbers"));
    }

    private static java.util.List<java.lang.Integer> $list$int(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.lang.Integer> al = new java.util.ArrayList<java.lang.Integer>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($int(cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }

    private static java.util.List<java.util.List<java.lang.Integer>> $list$list$int(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.util.List<java.lang.Integer>> al = new java.util.ArrayList<java.util.List<java.lang.Integer>>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($list$int((com.typesafe.config.ConfigList)cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }

    private static java.lang.Integer $int(com.typesafe.config.ConfigValue cv) {
      java.lang.Object u = cv.unwrapped();
      if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER
          || !(u instanceof java.lang.Integer)) {
        _$exc(u, "integer");
      }
      return (java.lang.Integer) u;
    }

    private static void _$exc(java.lang.Object u, java.lang.String expected) {
      throw new java.lang.RuntimeException(
          "expecting: " +expected + " got: " +
              (u instanceof java.lang.String ? "\"" +u+ "\"" : u));
    }
}

