package tscfg.example;

public class JavaIssue312bCfg {
    
  /**
   * Description of the required endpoint.
   * /\* nested doc comment delimiters *\/ escaped.
   */
  public final JavaIssue312bCfg.Endpoint endpoint;
  
  /**
   * Description of the required endpoint.
   * /\* nested doc comment delimiters *\/ escaped.
   */
  public static class Endpoint {
      
    /**
     * Configuration for notifications.
     */
    public final Endpoint.Notification notification;
      
    /**
     * The associated path.
     * For example, "/home/foo/bar"
     */
    public final java.lang.String path;
      
    /**
     * Port for the endpoint service.
     */
    public final int port;
    
    /**
     * Configuration for notifications.
     */
    public static class Notification {
        
      /**
       * Emails to send notifications to.
       */
      public final java.util.List<Notification.Emails$Elm> emails;
      public static class Emails$Elm {
          
        /**
         * The email address.
         */
        public final java.lang.String email;
          
        /**
         * The name of the recipient.
         */
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
      
      public Notification(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
        this.emails = $_LNotification_Emails$Elm(c.getList("emails"), parentPath, $tsCfgValidator);
      }
      private static java.util.List<Notification.Emails$Elm> $_LNotification_Emails$Elm(com.typesafe.config.ConfigList cl, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
        java.util.ArrayList<Notification.Emails$Elm> al = new java.util.ArrayList<>();
        for (com.typesafe.config.ConfigValue cv: cl) {
          al.add(new Notification.Emails$Elm(((com.typesafe.config.ConfigObject)cv).toConfig(), parentPath, $tsCfgValidator));
        }
        return java.util.Collections.unmodifiableList(al);
      }
    }
    
    public Endpoint(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
      this.notification = c.hasPathOrNull("notification") ? new Endpoint.Notification(c.getConfig("notification"), parentPath + "notification.", $tsCfgValidator) : new Endpoint.Notification(com.typesafe.config.ConfigFactory.parseString("notification{}"), parentPath + "notification.", $tsCfgValidator);
      this.path = $_reqStr(parentPath, c, "path", $tsCfgValidator);
      this.port = c.hasPathOrNull("port") ? c.getInt("port") : 8080;
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
  
  public JavaIssue312bCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.endpoint = c.hasPathOrNull("endpoint") ? new JavaIssue312bCfg.Endpoint(c.getConfig("endpoint"), parentPath + "endpoint.", $tsCfgValidator) : new JavaIssue312bCfg.Endpoint(com.typesafe.config.ConfigFactory.parseString("endpoint{}"), parentPath + "endpoint.", $tsCfgValidator);
    $tsCfgValidator.validate();
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
