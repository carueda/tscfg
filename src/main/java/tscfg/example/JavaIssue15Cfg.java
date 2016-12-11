package tscfg.example;

public class JavaIssue15Cfg {
  public final java.util.List<JavaIssue15Cfg.Positions$Elm> positions;
  public static class Positions$Elm {
    public final java.util.List<java.lang.Integer> numbers;
    public final java.util.List<java.util.List<Positions$Elm.Positions$Elm2>> positions;
    public static class Positions$Elm2 {
      public final int other;
      public final java.lang.String stuff;
      
      public Positions$Elm2(com.typesafe.config.Config c) {
        this.other = c.getInt("other");
        this.stuff = c.getString("stuff");
      }
    }
    
    public Positions$Elm(com.typesafe.config.Config c) {
      this.numbers = $_L$_int(c.getList("numbers"));
      this.positions = $_L$_LPositions$Elm_Positions$Elm2(c.getList("positions"));
    }
    private static java.util.List<java.util.List<Positions$Elm.Positions$Elm2>> $_L$_LPositions$Elm_Positions$Elm2(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.util.List<Positions$Elm.Positions$Elm2>> al = new java.util.ArrayList<>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($_LPositions$Elm_Positions$Elm2((com.typesafe.config.ConfigList)cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }
    private static java.util.List<Positions$Elm.Positions$Elm2> $_LPositions$Elm_Positions$Elm2(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<Positions$Elm.Positions$Elm2> al = new java.util.ArrayList<>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add(new Positions$Elm.Positions$Elm2(((com.typesafe.config.ConfigObject)cv).toConfig()));
      }
      return java.util.Collections.unmodifiableList(al);
    }
  }
  
  public JavaIssue15Cfg(com.typesafe.config.Config c) {
    this.positions = $_LJavaIssue15Cfg_Positions$Elm(c.getList("positions"));
  }
  private static java.util.List<JavaIssue15Cfg.Positions$Elm> $_LJavaIssue15Cfg_Positions$Elm(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<JavaIssue15Cfg.Positions$Elm> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add(new JavaIssue15Cfg.Positions$Elm(((com.typesafe.config.ConfigObject)cv).toConfig()));
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
