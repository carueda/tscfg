package tscfg.example;

public class JavaIssue15bCfg {
  public final java.util.List<java.lang.Boolean> booleans;
  public final java.util.List<java.lang.Double> doubles;
  public final java.util.List<java.util.List<java.lang.Integer>> integers;
  public final java.util.List<java.lang.Long> longs;
  public final java.util.List<java.lang.String> strings;
  
  public JavaIssue15bCfg(com.typesafe.config.Config c) {
    this.booleans = $_L$_bln(c.getList("booleans"));
    this.doubles = $_L$_dbl(c.getList("doubles"));
    this.integers = $_L$_L$_int(c.getList("integers"));
    this.longs = $_L$_lng(c.getList("longs"));
    this.strings = $_L$_str(c.getList("strings"));
  }

  private static java.util.List<java.util.List<java.lang.Integer>> $_L$_L$_int(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<java.util.List<java.lang.Integer>> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add($_L$_int((com.typesafe.config.ConfigList)cv));
    }
    return java.util.Collections.unmodifiableList(al);
  }
  private static java.util.List<java.lang.Boolean> $_L$_bln(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<java.lang.Boolean> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add($_bln(cv));
    }
    return java.util.Collections.unmodifiableList(al);
  }
  private static java.util.List<java.lang.Double> $_L$_dbl(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<java.lang.Double> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add($_dbl(cv));
    }
    return java.util.Collections.unmodifiableList(al);
  }
  private static java.util.List<java.lang.Integer> $_L$_int(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<java.lang.Integer> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add($_int(cv));
    }
    return java.util.Collections.unmodifiableList(al);
  }
  private static java.util.List<java.lang.Long> $_L$_lng(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<java.lang.Long> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add($_lng(cv));
    }
    return java.util.Collections.unmodifiableList(al);
  }
  private static java.util.List<java.lang.String> $_L$_str(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<java.lang.String> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add($_str(cv));
    }
    return java.util.Collections.unmodifiableList(al);
  }
  private static java.lang.Boolean $_bln(com.typesafe.config.ConfigValue cv) {
    java.lang.Object u = cv.unwrapped();
    if (cv.valueType() != com.typesafe.config.ConfigValueType.BOOLEAN ||
      !(u instanceof java.lang.Boolean)) throw $_expE(cv, "boolean");
    return (java.lang.Boolean) u;
  }
  private static java.lang.Double $_dbl(com.typesafe.config.ConfigValue cv) {
    java.lang.Object u = cv.unwrapped();
    if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER ||
      !(u instanceof java.lang.Number)) throw $_expE(cv, "double");
    return ((java.lang.Number) u).doubleValue();
  }
  private static java.lang.RuntimeException $_expE(com.typesafe.config.ConfigValue cv, java.lang.String exp) {
    java.lang.Object u = cv.unwrapped();
    return new java.lang.RuntimeException(cv.origin().lineNumber()
      + ": expecting: " +exp + " got: " + (u instanceof java.lang.String ? "\"" +u+ "\"" : u));
  }
  private static java.lang.Integer $_int(com.typesafe.config.ConfigValue cv) {
    java.lang.Object u = cv.unwrapped();
    if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER ||
      !(u instanceof java.lang.Integer)) throw $_expE(cv, "integer");
    return (java.lang.Integer) u;
  }
  private static java.lang.Long $_lng(com.typesafe.config.ConfigValue cv) {
    java.lang.Object u = cv.unwrapped();
    if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER ||
      !(u instanceof java.lang.Long) && !(u instanceof java.lang.Integer)) throw $_expE(cv, "long");
    return ((java.lang.Number) u).longValue();
  }
  private static java.lang.String $_str(com.typesafe.config.ConfigValue cv) {
    return java.lang.String.valueOf(cv.unwrapped());
  }
}
