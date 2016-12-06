package tscfg.example;

public class JCfg {
  public final java.lang.Long durHr;
  public final java.lang.String foo;
  public final java.lang.String optStr;
  public final java.util.List<java.util.List<JCfg.$_E1>> positions;
  public static class $_E1 {
    public final java.util.List<java.lang.Boolean> attrs;
    public final double lat;
    public final double lon;
    
    public $_E1(com.typesafe.config.Config c) {
      this.attrs = c.hasPathOrNull("attrs") ? $_L$_bln(c.getList("attrs")) : null;
      this.lat = c.hasPathOrNull("lat") ? c.getDouble("lat") : 35.1;
      this.lon = c.getDouble("lon");
    }
  }
  
  public JCfg(com.typesafe.config.Config c) {
    this.durHr = c.hasPathOrNull("durHr") ? c.getDuration("durHr", java.util.concurrent.TimeUnit.HOURS) : null;
    this.foo = c.hasPathOrNull("foo") ? c.getString("foo") : "foo \"val\" etc ";
    this.optStr = c.hasPathOrNull("optStr") ? c.getString("optStr") : null;
    this.positions = $_L$_LJCfg_$_E1(c.getList("positions"));
  }
  private static java.util.List<JCfg.$_E1> $_LJCfg_$_E1(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<JCfg.$_E1> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add(new JCfg.$_E1(((com.typesafe.config.ConfigObject)cv).toConfig()));
    }
    return java.util.Collections.unmodifiableList(al);
  }
  private static java.util.List<java.util.List<JCfg.$_E1>> $_L$_LJCfg_$_E1(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<java.util.List<JCfg.$_E1>> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add($_LJCfg_$_E1((com.typesafe.config.ConfigList)cv));
    }
    return java.util.Collections.unmodifiableList(al);
  }

  private static java.lang.Boolean $_bln(com.typesafe.config.ConfigValue cv) {
    java.lang.Object u = cv.unwrapped();
    if (cv.valueType() != com.typesafe.config.ConfigValueType.BOOLEAN ||
      !(u instanceof java.lang.Boolean)) throw $_expE(cv, "boolean");
    return (java.lang.Boolean) u;
  }
  private static java.lang.RuntimeException $_expE(com.typesafe.config.ConfigValue cv, java.lang.String exp) {
    java.lang.Object u = cv.unwrapped();
    return new java.lang.RuntimeException(cv.origin().lineNumber()
      + ": expecting: " +exp + " got: " + (u instanceof java.lang.String ? "\"" +u+ "\"" : u));
  }
  private static java.util.List<java.lang.Boolean> $_L$_bln(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<java.lang.Boolean> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add($_bln(cv));
    }
    return java.util.Collections.unmodifiableList(al);
  }
}
