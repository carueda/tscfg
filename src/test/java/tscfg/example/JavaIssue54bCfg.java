package tscfg.example;

public class JavaIssue54bCfg {
  public final JavaIssue54bCfg.Root root;

  public static class Root {
    public final Shared e;
    public final java.util.List<Shared> f;

    public static class Shared {
      public final java.lang.String b;
      public final Shared.C c;

      public static class C {
        public final int d;

        public C(
            com.typesafe.config.Config c,
            java.lang.String parentPath,
            $TsCfgValidator $tsCfgValidator) {
          this.d = $_reqInt(parentPath, c, "d", $tsCfgValidator);
        }

        private static int $_reqInt(
            java.lang.String parentPath,
            com.typesafe.config.Config c,
            java.lang.String path,
            $TsCfgValidator $tsCfgValidator) {
          if (c == null) return 0;
          try {
            return c.getInt(path);
          } catch (com.typesafe.config.ConfigException e) {
            $tsCfgValidator.addBadPath(parentPath + path, e);
            return 0;
          }
        }
      }

      public Shared(
          com.typesafe.config.Config c,
          java.lang.String parentPath,
          $TsCfgValidator $tsCfgValidator) {
        this.b = $_reqStr(parentPath, c, "b", $tsCfgValidator);
        this.c =
            c.hasPathOrNull("c")
                ? new Shared.C(c.getConfig("c"), parentPath + "c.", $tsCfgValidator)
                : new Shared.C(
                    com.typesafe.config.ConfigFactory.parseString("c{}"),
                    parentPath + "c.",
                    $tsCfgValidator);
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

    public Root(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.e =
          c.hasPathOrNull("e")
              ? new Shared(c.getConfig("e"), parentPath + "e.", $tsCfgValidator)
              : new Shared(
                  com.typesafe.config.ConfigFactory.parseString("e{}"),
                  parentPath + "e.",
                  $tsCfgValidator);
      this.f = $_LShared(c.getList("f"), parentPath, $tsCfgValidator);
    }

    private static java.util.List<Shared> $_LShared(
        com.typesafe.config.ConfigList cl,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      java.util.ArrayList<Shared> al = new java.util.ArrayList<>();
      for (com.typesafe.config.ConfigValue cv : cl) {
        al.add(
            new Shared(
                ((com.typesafe.config.ConfigObject) cv).toConfig(), parentPath, $tsCfgValidator));
      }
      return java.util.Collections.unmodifiableList(al);
    }
  }

  public JavaIssue54bCfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.root =
        c.hasPathOrNull("root")
            ? new JavaIssue54bCfg.Root(c.getConfig("root"), "root.", $tsCfgValidator)
            : new JavaIssue54bCfg.Root(
                com.typesafe.config.ConfigFactory.parseString("root{}"), "root.", $tsCfgValidator);
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
