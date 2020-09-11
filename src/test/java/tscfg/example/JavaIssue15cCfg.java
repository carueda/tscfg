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
      
      public Attrs$Elm(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
        this.foo = $_reqLng(parentPath, c, "foo", $tsCfgValidator);
      }
      private static long $_reqLng(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, $TsCfgValidator $tsCfgValidator) {
        if (c == null) return 0;
        try {
          return c.getLong(path);
        }
        catch(com.typesafe.config.ConfigException e) {
          $tsCfgValidator.addBadPath(parentPath + path, e);
          return 0;
        }
      }
    
    }
    
    public Positions$Elm(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.attrs = $_L$_LPositions$Elm_Attrs$Elm(c.getList("attrs"), parentPath, $tsCfgValidator);
      this.lat = $_reqDbl(parentPath, c, "lat", $tsCfgValidator);
      this.lon = $_reqDbl(parentPath, c, "lon", $tsCfgValidator);
    }
    private static java.util.List<java.util.List<Positions$Elm.Attrs$Elm>> $_L$_LPositions$Elm_Attrs$Elm(com.typesafe.config.ConfigList cl, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      java.util.ArrayList<java.util.List<Positions$Elm.Attrs$Elm>> al = new java.util.ArrayList<>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($_LPositions$Elm_Attrs$Elm((com.typesafe.config.ConfigList)cv, parentPath, $tsCfgValidator));
      }
      return java.util.Collections.unmodifiableList(al);
    }
    private static java.util.List<Positions$Elm.Attrs$Elm> $_LPositions$Elm_Attrs$Elm(com.typesafe.config.ConfigList cl, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      java.util.ArrayList<Positions$Elm.Attrs$Elm> al = new java.util.ArrayList<>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add(new Positions$Elm.Attrs$Elm(((com.typesafe.config.ConfigObject)cv).toConfig(), parentPath, $tsCfgValidator));
      }
      return java.util.Collections.unmodifiableList(al);
    }
    private static double $_reqDbl(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, $TsCfgValidator $tsCfgValidator) {
      if (c == null) return 0;
      try {
        return c.getDouble(path);
      }
      catch(com.typesafe.config.ConfigException e) {
        $tsCfgValidator.addBadPath(parentPath + path, e);
        return 0;
      }
    }
  
  }
  
  public static class Qaz {
    public final Qaz.Aa aa;
    public static class Aa {
      public final java.util.List<Aa.Bb$Elm> bb;
      public static class Bb$Elm {
        public final java.lang.String cc;
        
        public Bb$Elm(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
          this.cc = $_reqStr(parentPath, c, "cc", $tsCfgValidator);
        }
        private static java.lang.String $_reqStr(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, $TsCfgValidator $tsCfgValidator) {
          if (c == null) return null;
          try {
            return c.getString(path);
          }
          catch(com.typesafe.config.ConfigException e) {
            $tsCfgValidator.addBadPath(parentPath + path, e);
            return null;
          }
        }
      
      }
      
      public Aa(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
        this.bb = $_LAa_Bb$Elm(c.getList("bb"), parentPath, $tsCfgValidator);
      }
      private static java.util.List<Aa.Bb$Elm> $_LAa_Bb$Elm(com.typesafe.config.ConfigList cl, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
        java.util.ArrayList<Aa.Bb$Elm> al = new java.util.ArrayList<>();
        for (com.typesafe.config.ConfigValue cv: cl) {
          al.add(new Aa.Bb$Elm(((com.typesafe.config.ConfigObject)cv).toConfig(), parentPath, $tsCfgValidator));
        }
        return java.util.Collections.unmodifiableList(al);
      }
    }
    
    public Qaz(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.aa = c.hasPathOrNull("aa") ? new Qaz.Aa(c.getConfig("aa"), parentPath + "aa.", $tsCfgValidator) : new Qaz.Aa(com.typesafe.config.ConfigFactory.parseString("aa{}"), parentPath + "aa.", $tsCfgValidator);
    }
  }
  
  public JavaIssue15cCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.positions = $_LJavaIssue15cCfg_Positions$Elm(c.getList("positions"), parentPath, $tsCfgValidator);
    this.qaz = c.hasPathOrNull("qaz") ? new JavaIssue15cCfg.Qaz(c.getConfig("qaz"), parentPath + "qaz.", $tsCfgValidator) : new JavaIssue15cCfg.Qaz(com.typesafe.config.ConfigFactory.parseString("qaz{}"), parentPath + "qaz.", $tsCfgValidator);
    $tsCfgValidator.validate();
  }
  private static java.util.List<JavaIssue15cCfg.Positions$Elm> $_LJavaIssue15cCfg_Positions$Elm(com.typesafe.config.ConfigList cl, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
    java.util.ArrayList<JavaIssue15cCfg.Positions$Elm> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add(new JavaIssue15cCfg.Positions$Elm(((com.typesafe.config.ConfigObject)cv).toConfig(), parentPath, $tsCfgValidator));
    }
    return java.util.Collections.unmodifiableList(al);
  }
  private static final class $TsCfgValidator  {
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
