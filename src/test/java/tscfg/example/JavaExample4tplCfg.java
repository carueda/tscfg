package tscfg.example;

public class JavaExample4tplCfg {
  public final JavaExample4tplCfg.Endpoint endpoint;
  public static class Endpoint {
    public final Endpoint.Notifications notifications;
    public final java.lang.String path;
    public final int port;
    public final Endpoint.Stuff stuff;
    public static class Notifications {
      public final java.util.List<Notifications.Emails$Elm> emails;
      public static class Emails$Elm {
        public final java.lang.String email;
        public final java.lang.String name;
        
        public Emails$Elm(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
          this.email = $_reqStr(parentPath, c, "email", $tsCfgValidator);
          this.name = c.hasPathOrNull("name") ? c.getString("name") : null;
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
      
      public Notifications(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
        this.emails = $_LNotifications_Emails$Elm(c.getList("emails"), parentPath, $tsCfgValidator);
      }
      private static java.util.List<Notifications.Emails$Elm> $_LNotifications_Emails$Elm(com.typesafe.config.ConfigList cl, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
        java.util.ArrayList<Notifications.Emails$Elm> al = new java.util.ArrayList<>();
        for (com.typesafe.config.ConfigValue cv: cl) {
          al.add(new Notifications.Emails$Elm(((com.typesafe.config.ConfigObject)cv).toConfig(), parentPath, $tsCfgValidator));
        }
        return java.util.Collections.unmodifiableList(al);
      }
    }
    
    public static class Stuff {
      public final java.util.List<java.util.List<java.lang.Double>> coefs;
      public final int port2;
      
      public Stuff(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
        this.coefs = $_L$_L$_dbl(c.getList("coefs"), parentPath, $tsCfgValidator);
        this.port2 = $_reqInt(parentPath, c, "port2", $tsCfgValidator);
      }
      private static int $_reqInt(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, $TsCfgValidator $tsCfgValidator) {
        if (c == null) return 0;
        try {
          return c.getInt(path);
        }
        catch(com.typesafe.config.ConfigException e) {
          $tsCfgValidator.addBadPath(parentPath + path, e);
          return 0;
        }
      }
    
    }
    
    public Endpoint(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.notifications = c.hasPathOrNull("notifications") ? new Endpoint.Notifications(c.getConfig("notifications"), parentPath + "notifications.", $tsCfgValidator) : new Endpoint.Notifications(com.typesafe.config.ConfigFactory.parseString("notifications{}"), parentPath + "notifications.", $tsCfgValidator);
      this.path = $_reqStr(parentPath, c, "path", $tsCfgValidator);
      this.port = c.hasPathOrNull("port") ? c.getInt("port") : 8080;
      this.stuff = c.hasPathOrNull("stuff") ? new Endpoint.Stuff(c.getConfig("stuff"), parentPath + "stuff.", $tsCfgValidator) : null;
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
  
  public JavaExample4tplCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.endpoint = c.hasPathOrNull("endpoint") ? new JavaExample4tplCfg.Endpoint(c.getConfig("endpoint"), parentPath + "endpoint.", $tsCfgValidator) : new JavaExample4tplCfg.Endpoint(com.typesafe.config.ConfigFactory.parseString("endpoint{}"), parentPath + "endpoint.", $tsCfgValidator);
    $tsCfgValidator.validate();
  }

  private static java.util.List<java.util.List<java.lang.Double>> $_L$_L$_dbl(com.typesafe.config.ConfigList cl, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
    java.util.ArrayList<java.util.List<java.lang.Double>> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add($_L$_dbl((com.typesafe.config.ConfigList)cv, parentPath, $tsCfgValidator));
    }
    return java.util.Collections.unmodifiableList(al);
  }
  private static java.util.List<java.lang.Double> $_L$_dbl(com.typesafe.config.ConfigList cl, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
    java.util.ArrayList<java.lang.Double> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv: cl) {
      al.add($_dbl(cv));
    }
    return java.util.Collections.unmodifiableList(al);
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
