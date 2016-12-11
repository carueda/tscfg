package tscfg.example;

public class JavaIssue15cCfg {
  public final java.util.List<JavaIssue15cCfg.Positions$Elm> positions;
  public final JavaIssue15cCfg.Qaz qaz;
  public static class Positions$Elm {
    public final java.util.List<java.util.List<Positions$Elm.Attrs$Elm>> attrs;
    public final double lat;
    public final double lon;
    public static class Attrs$Elm {
      public final long foo;
      
      public Attrs$Elm(com.typesafe.config.Config c) {
        this.foo = c.getLong("foo");
      }
    }
    
    public Positions$Elm(com.typesafe.config.Config c) {
      this.attrs = $_L$_LPositions$Elm_Attrs$Elm(c.getList("attrs"));
      this.lat = c.getDouble("lat");
      this.lon = c.getDouble("lon");
    }
    private static java.util.List<java.util.List<Positions$Elm.Attrs$Elm>> $_L$_LPositions$Elm_Attrs$Elm(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.util.List<Positions$Elm.Attrs$Elm>> al = new java.util.ArrayList<>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($_LPositions$Elm_Attrs$Elm((com.typesafe.config.ConfigList)cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }
    private static java.util.List<Positions$Elm.Attrs$Elm> $_LPositions$Elm_Attrs$Elm(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<Positions$Elm.Attrs$Elm> al = new java.util.ArrayList<>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add(new Positions$Elm.Attrs$Elm(((com.typesafe.config.ConfigObject)cv).toConfig()));
      }
      return java.util.Collections.unmodifiableList(al);
    }
  }
  
  public static class Qaz {
    public final Qaz.Aa aa;
    public static class Aa {
      public final java.util.List<Aa.Bb$Elm> bb;
      public static class Bb$Elm {
        public final java.lang.String cc;
        
        public Bb$Elm(com.typesafe.config.Config c) {
          this.cc = c.getString("cc");
        }
      }
      
      public Aa(com.typesafe.config.Config c) {
        this.bb = $_LAa_Bb$Elm(c.getList("bb"));
      }
      private static java.util.List<Aa.Bb$Elm> $_LAa_Bb$Elm(com.typesafe.config.ConfigList cl) {
        java.util.ArrayList<Aa.Bb$Elm> al = new java.util.ArrayList<>();
        for (com.typesafe.config.ConfigValue cv: cl) {
          al.add(new Aa.Bb$Elm(((com.typesafe.config.ConfigObject)cv).toConfig()));
        }
        return java.util.Collections.unmodifiableList(al);
      }
    }
    
    public Qaz(com.typesafe.config.Config c) {
      this.aa = new Qaz.Aa(c.getConfig("aa"));
    }
  }
  
  public JavaIssue15cCfg(com.typesafe.config.Config c) {
    this.positions = $_LJavaIssue15cCfg_Positions$Elm(c.getList("positions"));
    this.qaz = new JavaIssue15cCfg.Qaz(c.getConfig("qaz"));
  }
  private static java.util.List<JavaIssue15cCfg.Positions$Elm> $_LJavaIssue15cCfg_Positions$Elm(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<JavaIssue15cCfg.Positions$Elm> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add(new JavaIssue15cCfg.Positions$Elm(((com.typesafe.config.ConfigObject)cv).toConfig()));
    }
    return java.util.Collections.unmodifiableList(al);
  }
}
