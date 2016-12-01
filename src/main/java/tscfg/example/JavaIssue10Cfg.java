package tscfg.example;

public class JavaIssue10Cfg {
  public final Main main;

  public static class Main {
    public final Email email;
    public final java.util.List<Reals$Elm> reals;

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
      this.email = c.hasPathOrNull("email") ? new Email(_$config(c, "email")) : null;
      this.reals = c.hasPathOrNull("reals") ? $listReals$Elm(c.getList("reals")) : null;
    }

    private static java.util.List<Reals$Elm> $listReals$Elm(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<Reals$Elm> al = new java.util.ArrayList<>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add(new Reals$Elm(((com.typesafe.config.ConfigObject)cv).toConfig()));
      }
      return java.util.Collections.unmodifiableList(al);
    }
  }

  public JavaIssue10Cfg(com.typesafe.config.Config c) {
    this.main = new Main(_$config(c, "main"));
  }

  private static com.typesafe.config.Config _$config(com.typesafe.config.Config c, java.lang.String path) {
    return c.hasPathOrNull(path) ? c.getConfig(path) : null;
  }
}

