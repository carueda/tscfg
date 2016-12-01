package tscfg.example;

public class JavaIssue15Cfg {
  public final java.util.List<Positions$Elm> positions;

  public static class Positions$Elm {
    public final java.util.List<java.lang.Integer> numbers;
    public final java.util.List<java.util.List<Positions$2$Elm>> positions;

    public static class Positions$2$Elm {
      public final int other;
      public final java.lang.String stuff;

      public Positions$2$Elm(com.typesafe.config.Config c) {
        this.other = c.getInt("other");
        this.stuff = c.getString("stuff");
      }
    }


    public Positions$Elm(com.typesafe.config.Config c) {
      this.numbers = $list$int(c.getList("numbers"));
      this.positions = $list$listPositions$2$Elm(c.getList("positions"));
    }

    private static java.util.List<java.util.List<Positions$2$Elm>> $list$listPositions$2$Elm(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.util.List<Positions$2$Elm>> al = new java.util.ArrayList<>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($listPositions$2$Elm((com.typesafe.config.ConfigList)cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }
    private static java.util.List<Positions$2$Elm> $listPositions$2$Elm(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<Positions$2$Elm> al = new java.util.ArrayList<>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add(new Positions$2$Elm(((com.typesafe.config.ConfigObject)cv).toConfig()));
      }
      return java.util.Collections.unmodifiableList(al);
    }
  }


  public JavaIssue15Cfg(com.typesafe.config.Config c) {
    this.positions = $listPositions$Elm(c.getList("positions"));
  }

  private static java.lang.RuntimeException $expE(com.typesafe.config.ConfigValue cv, java.lang.String exp) {
    java.lang.Object u = cv.unwrapped();
    return new java.lang.RuntimeException(cv.origin().lineNumber()
      + ": expecting: " +exp + " got: " + (u instanceof java.lang.String ? "\"" +u+ "\"" : u));
  }
  private static java.lang.Integer $int(com.typesafe.config.ConfigValue cv) {
    java.lang.Object u = cv.unwrapped();
    if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER ||
      !(u instanceof java.lang.Integer)) throw $expE(cv, "integer");
    return (java.lang.Integer) u;
  }
  private static java.util.List<java.lang.Integer> $list$int(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<java.lang.Integer> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add($int(cv));
    }
    return java.util.Collections.unmodifiableList(al);
  }
  private static java.util.List<Positions$Elm> $listPositions$Elm(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<Positions$Elm> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add(new Positions$Elm(((com.typesafe.config.ConfigObject)cv).toConfig()));
    }
    return java.util.Collections.unmodifiableList(al);
  }
}

