package tscfg.example;

public class JavaIssue10Cfg {
  public final JavaIssue10Cfg.Main main;
  public static class Main {
      public final Main.Email email;
        public final java.util.List<Main.Reals$Elm> reals;
      public static class Email {
              public final java.lang.String password;
                  public final java.lang.String server;
              
              public Email(com.typesafe.config.Config c) {
                this.password = c.getString("password");
                    this.server = c.getString("server");
              }
            }
            
    public static class Reals$Elm {
          public final double foo;
          
          public Reals$Elm(com.typesafe.config.Config c) {
            this.foo = c.getDouble("foo");
          }
        }
        
      public Main(com.typesafe.config.Config c) {
        this.email = c.hasPathOrNull("email") ? new Main.Email(c.getConfig("email")) : null;
          this.reals = c.hasPathOrNull("reals") ? $_LMain_Reals$Elm(c.getList("reals")) : null;
      }
        private static java.util.List<Main.Reals$Elm> $_LMain_Reals$Elm(com.typesafe.config.ConfigList cl) {
          java.util.ArrayList<Main.Reals$Elm> al = new java.util.ArrayList<>();
          for (com.typesafe.config.ConfigValue cv: cl) {
            al.add(new Main.Reals$Elm(((com.typesafe.config.ConfigObject)cv).toConfig()));
          }
          return java.util.Collections.unmodifiableList(al);
        }
    }
    
  public JavaIssue10Cfg(com.typesafe.config.Config c) {
    this.main = new JavaIssue10Cfg.Main(c.getConfig("main"));
  }
}
