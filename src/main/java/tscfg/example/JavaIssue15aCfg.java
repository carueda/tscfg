package tscfg.example;

public class JavaIssue15aCfg {
  public final java.util.List<java.lang.Integer> ii;
  
  public JavaIssue15aCfg(com.typesafe.config.Config c) {
    this.ii = $_L$_int(c.getList("ii"));
  }

  private static java.util.List<java.lang.Integer> $_L$_int(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<java.lang.Integer> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add($_int(cv));
    }
    return java.util.Collections.unmodifiableList(al);
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
}
