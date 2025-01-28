package tscfg.example;

/**
 * @param endpoint
 *   Description of the required endpoint.
 */
public record JavaIssue312aCfg(
  JavaIssue312aCfg.Endpoint endpoint
) {
  static final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();

  
  /**
   * Description of the required endpoint.
   * /\* nested doc comment delimiters *\/ escaped.
   * 
   * @param notification
   *    Configuration for notifications.
   * @param path
   *    The associated path.
   *    For example, "/home/foo/bar"
   * @param port
   *    Port for the endpoint service.
   */
  public record Endpoint(
    Endpoint.Notification notification,
    java.lang.String path,
    int port
  ) {
    
    /**
     * Configuration for notifications.
     * 
     * @param emails
     *    Emails to send notifications to.
     */
    public record Notification(
      java.util.List<Notification.Emails$Elm> emails
    ) {
      public record Emails$Elm(
        java.lang.String email,
        java.lang.String name
      ) {
        
        public Emails$Elm(com.typesafe.config.Config c, java.lang.String parentPath, $TsCfgValidator $tsCfgValidator) {
          this(
            $_reqStr(parentPath, c, "email", $tsCfgValidator),
            c.hasPathOrNull("name") ? c.getString("name") : null
          );
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
        this(
          $_LNotification_Emails$Elm(c.getList("emails"), parentPath, $tsCfgValidator)
        );
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
      this(
        c.hasPathOrNull("notification") ? new Endpoint.Notification(c.getConfig("notification"), parentPath + "notification.", $tsCfgValidator) : new Endpoint.Notification(com.typesafe.config.ConfigFactory.parseString("notification{}"), parentPath + "notification.", $tsCfgValidator),
        $_reqStr(parentPath, c, "path", $tsCfgValidator),
        c.hasPathOrNull("port") ? c.getInt("port") : 8080
      );
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
  
  public JavaIssue312aCfg(com.typesafe.config.Config c) {
    this(
      c.hasPathOrNull("endpoint") ? new JavaIssue312aCfg.Endpoint(c.getConfig("endpoint"), "endpoint.", $tsCfgValidator) : new JavaIssue312aCfg.Endpoint(com.typesafe.config.ConfigFactory.parseString("endpoint{}"), "endpoint.", $tsCfgValidator)
    );
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
