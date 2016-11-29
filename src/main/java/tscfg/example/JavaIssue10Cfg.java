package tscfg.example;

public class JavaIssue10Cfg {
  public final Main main;

  public static class Main {
    public final Email email;

    public static class Email {
      public final java.lang.String password;
      public final java.lang.String server;

      public Email(com.typesafe.config.Config c) {
        this.password = c.getString("password");
        this.server = c.getString("server");
      }
    }

    public Main(com.typesafe.config.Config c) {
      this.email = c.hasPathOrNull("email") ? new Email(_$config(c, "email")) : null;
    }
  }

  public JavaIssue10Cfg(com.typesafe.config.Config c) {
    this.main = new Main(_$config(c, "main"));
  }

  private static com.typesafe.config.Config _$config(com.typesafe.config.Config c, java.lang.String path) {
    return c.hasPathOrNull(path) ? c.getConfig(path) : null;
  }
}

