package tscfg.example;

public class JavaIssue10Cfg {
  public final JavaIssue10Cfg.Main main;

  public static class Main {
    public final Main.Email email;
    public final java.util.List<Main.Reals$Elm> reals;

    public static class Email {
      public final java.lang.String password;
      public final java.lang.String server;

      public Email(
          com.typesafe.config.Config c,
          java.lang.String parentPath,
          $TsCfgValidator $tsCfgValidator) {
        this.password = $_reqStr(parentPath, c, "password", $tsCfgValidator);
        this.server = $_reqStr(parentPath, c, "server", $tsCfgValidator);
      }

      private static java.lang.String $_reqStr(
          java.lang.String parentPath,
          com.typesafe.config.Config c,
          java.lang.String path,
          $TsCfgValidator $tsCfgValidator) {
        if (c == null) return null;
        try {
          return c.getString(path);
        } catch (com.typesafe.config.ConfigException e) {
          $tsCfgValidator.addBadPath(parentPath + path, e);
          return null;
        }
      }
    }

    public static class Reals$Elm {
      public final double foo;

      public Reals$Elm(
          com.typesafe.config.Config c,
          java.lang.String parentPath,
          $TsCfgValidator $tsCfgValidator) {
        this.foo = $_reqDbl(parentPath, c, "foo", $tsCfgValidator);
      }

      private static double $_reqDbl(
          java.lang.String parentPath,
          com.typesafe.config.Config c,
          java.lang.String path,
          $TsCfgValidator $tsCfgValidator) {
        if (c == null) return 0;
        try {
          return c.getDouble(path);
        } catch (com.typesafe.config.ConfigException e) {
          $tsCfgValidator.addBadPath(parentPath + path, e);
          return 0;
        }
      }
    }

    public Main(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.email =
          c.hasPathOrNull("email")
              ? new Main.Email(c.getConfig("email"), parentPath + "email.", $tsCfgValidator)
              : null;
      this.reals =
          c.hasPathOrNull("reals")
              ? $_LMain_Reals$Elm(c.getList("reals"), parentPath, $tsCfgValidator)
              : null;
    }

    private static java.util.List<Main.Reals$Elm> $_LMain_Reals$Elm(
        com.typesafe.config.ConfigList cl,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      java.util.ArrayList<Main.Reals$Elm> al = new java.util.ArrayList<>();
      for (com.typesafe.config.ConfigValue cv : cl) {
        al.add(
            new Main.Reals$Elm(
                ((com.typesafe.config.ConfigObject) cv).toConfig(), parentPath, $tsCfgValidator));
      }
      return java.util.Collections.unmodifiableList(al);
    }
  }

  public JavaIssue10Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.main =
        c.hasPathOrNull("main")
            ? new JavaIssue10Cfg.Main(c.getConfig("main"), parentPath + "main.", $tsCfgValidator)
            : new JavaIssue10Cfg.Main(
                com.typesafe.config.ConfigFactory.parseString("main{}"),
                parentPath + "main.",
                $tsCfgValidator);
    $tsCfgValidator.validate();
  }

  private static final class $TsCfgValidator {
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
