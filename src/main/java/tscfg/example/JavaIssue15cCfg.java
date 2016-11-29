package tscfg.example;

public class JavaIssue15cCfg {
  public final java.util.List<Positions$1$Elm> positions;
  public final Qaz qaz;

  public static class Positions$1$Elm {
    public final java.util.List<java.util.List<Attrs$2$Elm>> attrs;
    public final double lat;
    public final double lon;

    public static class Attrs$2$Elm {
      public final long foo;

      public Attrs$2$Elm(com.typesafe.config.Config c) {
        this.foo = c.getLong("foo");
      }
    }


    public Positions$1$Elm(com.typesafe.config.Config c) {
      this.attrs = $list$listAttrs$2$Elm(c.getList("attrs"));
      this.lat = c.getDouble("lat");
      this.lon = c.getDouble("lon");
    }

    private static java.util.List<java.util.List<Attrs$2$Elm>> $list$listAttrs$2$Elm(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.util.List<Attrs$2$Elm>> al = new java.util.ArrayList<>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($listAttrs$2$Elm((com.typesafe.config.ConfigList)cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }
    private static java.util.List<Attrs$2$Elm> $listAttrs$2$Elm(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<Attrs$2$Elm> al = new java.util.ArrayList<>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add(new Attrs$2$Elm(((com.typesafe.config.ConfigObject)cv).toConfig()));
      }
      return java.util.Collections.unmodifiableList(al);
    }
  }

  public static class Qaz {
    public final Aa aa;

    public static class Aa {
      public final java.util.List<Bb$Elm> bb;

      public static class Bb$Elm {
        public final java.lang.String cc;

        public Bb$Elm(com.typesafe.config.Config c) {
          this.cc = c.getString("cc");
        }
      }


      public Aa(com.typesafe.config.Config c) {
        this.bb = $listBb$Elm(c.getList("bb"));
      }

      private static java.util.List<Bb$Elm> $listBb$Elm(com.typesafe.config.ConfigList cl) {
        java.util.ArrayList<Bb$Elm> al = new java.util.ArrayList<>();
        for (com.typesafe.config.ConfigValue cv: cl) {
          al.add(new Bb$Elm(((com.typesafe.config.ConfigObject)cv).toConfig()));
        }
        return java.util.Collections.unmodifiableList(al);
      }
    }

    public Qaz(com.typesafe.config.Config c) {
      this.aa = new Aa(_$config(c, "aa"));
    }
  }

  public JavaIssue15cCfg(com.typesafe.config.Config c) {
    this.positions = $listPositions$1$Elm(c.getList("positions"));
    this.qaz = new Qaz(_$config(c, "qaz"));
  }

  private static java.util.List<Positions$1$Elm> $listPositions$1$Elm(com.typesafe.config.ConfigList cl) {
    java.util.ArrayList<Positions$1$Elm> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add(new Positions$1$Elm(((com.typesafe.config.ConfigObject)cv).toConfig()));
    }
    return java.util.Collections.unmodifiableList(al);
  }
  private static com.typesafe.config.Config _$config(com.typesafe.config.Config c, java.lang.String path) {
    return c != null && c.hasPathOrNull(path) ? c.getConfig(path) : null;
  }
}

