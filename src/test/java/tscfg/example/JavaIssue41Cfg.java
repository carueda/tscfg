package tscfg.example;

public class JavaIssue41Cfg {
  public final java.util.Optional<java.lang.Integer> a;
  public final int b;
  public final JavaIssue41Cfg.C c;
  public final java.util.Optional<java.util.List<java.lang.Double>> i;

  public static class C {
    public final java.util.Optional<java.lang.String> d;
    public final java.lang.String e;
    public final java.util.Optional<C.F> f;

    public static class F {
      public final int g;
      public final java.lang.String h;

      public F(
          com.typesafe.config.Config c,
          java.lang.String parentPath,
          $TsCfgValidator $tsCfgValidator) {
        this.g = $_reqInt(parentPath, c, "g", $tsCfgValidator);
        this.h = $_reqStr(parentPath, c, "h", $tsCfgValidator);
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

    public C(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.d =
          c.hasPathOrNull("d")
              ? java.util.Optional.of(c.getString("d"))
              : java.util.Optional.empty();
      this.e = c.hasPathOrNull("e") ? c.getString("e") : "hello";
      this.f =
          c.hasPathOrNull("f")
              ? java.util.Optional.of(new C.F(c.getConfig("f"), parentPath + "f.", $tsCfgValidator))
              : java.util.Optional.empty();
    }
  }

  public JavaIssue41Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.a =
        c.hasPathOrNull("a") ? java.util.Optional.of(c.getInt("a")) : java.util.Optional.empty();
    this.b = c.hasPathOrNull("b") ? c.getInt("b") : 10;
    this.c =
        c.hasPathOrNull("c")
            ? new JavaIssue41Cfg.C(c.getConfig("c"), "c.", $tsCfgValidator)
            : new JavaIssue41Cfg.C(
                com.typesafe.config.ConfigFactory.parseString("c{}"), "c.", $tsCfgValidator);
    this.i =
        c.hasPathOrNull("i")
            ? java.util.Optional.of($_L$_dbl(c.getList("i"), parentPath, $tsCfgValidator))
            : java.util.Optional.empty();
    $tsCfgValidator.validate();
  }

  private static java.util.List<java.lang.Double> $_L$_dbl(
      com.typesafe.config.ConfigList cl,
      java.lang.String parentPath,
      $TsCfgValidator $tsCfgValidator) {
    java.util.ArrayList<java.lang.Double> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv : cl) {
      al.add($_dbl(cv));
    }
    return java.util.Collections.unmodifiableList(al);
  }

  private static java.lang.Double $_dbl(com.typesafe.config.ConfigValue cv) {
    java.lang.Object u = cv.unwrapped();
    if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER
        || !(u instanceof java.lang.Number)) throw $_expE(cv, "double");
    return ((java.lang.Number) u).doubleValue();
  }

  private static java.lang.RuntimeException $_expE(
      com.typesafe.config.ConfigValue cv, java.lang.String exp) {
    java.lang.Object u = cv.unwrapped();
    return new java.lang.RuntimeException(
        cv.origin().lineNumber()
            + ": expecting: "
            + exp
            + " got: "
            + (u instanceof java.lang.String ? "\"" + u + "\"" : u));
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
