package tscfg.example;

public class JavaIssue15dCfg {
  public final java.util.List<java.util.List<Baz$Elm>> baz;

  public static class Baz$Elm {
    public final java.lang.Boolean aa;
    public final double dd;

    public Baz$Elm(com.typesafe.config.Config c) {
      this.aa = c != null && c.hasPathOrNull("aa") ? c.getBoolean("aa") : null;
      this.dd = c.getDouble("dd");
    }
  }


  public JavaIssue15dCfg(com.typesafe.config.Config c) {
    this.baz = $list$listBaz$Elm(c.getList("baz"));
  }

  private static java.util.List<java.util.List<Baz$Elm>> $list$listBaz$Elm(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<java.util.List<Baz$Elm>> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add($listBaz$Elm((com.typesafe.config.ConfigList)cv));
    }
    return java.util.Collections.unmodifiableList(al);
  }
  private static java.util.List<Baz$Elm> $listBaz$Elm(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<Baz$Elm> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add(new Baz$Elm(((com.typesafe.config.ConfigObject)cv).toConfig()));
    }
    return java.util.Collections.unmodifiableList(al);
  }
}

