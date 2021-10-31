package tscfg.example;

public class JavaIssue12Cfg {
  public final JavaIssue12Cfg.Boolean Boolean;
  public final JavaIssue12Cfg.Option Option;
  public final JavaIssue12Cfg.String String;
  public final JavaIssue12Cfg.Int_ int_;

  public static class Boolean {
    public final java.lang.String bar;

    public Boolean(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.bar = c.hasPathOrNull("bar") ? c.getString("bar") : "foo";
    }
  }

  public static class Option {
    public final java.lang.String bar;

    public Option(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.bar = c.hasPathOrNull("bar") ? c.getString("bar") : "baz";
    }
  }

  public static class String {
    public final java.lang.String bar;

    public String(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.bar = c.hasPathOrNull("bar") ? c.getString("bar") : "baz";
    }
  }

  public static class Int_ {
    public final int bar;

    public Int_(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.bar = c.hasPathOrNull("bar") ? c.getInt("bar") : 1;
    }
  }

  public JavaIssue12Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.Boolean =
        c.hasPathOrNull("Boolean")
            ? new JavaIssue12Cfg.Boolean(c.getConfig("Boolean"), "Boolean.", $tsCfgValidator)
            : new JavaIssue12Cfg.Boolean(
                com.typesafe.config.ConfigFactory.parseString("Boolean{}"),
                "Boolean.",
                $tsCfgValidator);
    this.Option =
        c.hasPathOrNull("Option")
            ? new JavaIssue12Cfg.Option(c.getConfig("Option"), "Option.", $tsCfgValidator)
            : new JavaIssue12Cfg.Option(
                com.typesafe.config.ConfigFactory.parseString("Option{}"),
                "Option.",
                $tsCfgValidator);
    this.String =
        c.hasPathOrNull("String")
            ? new JavaIssue12Cfg.String(c.getConfig("String"), "String.", $tsCfgValidator)
            : new JavaIssue12Cfg.String(
                com.typesafe.config.ConfigFactory.parseString("String{}"),
                "String.",
                $tsCfgValidator);
    this.int_ =
        c.hasPathOrNull("int")
            ? new JavaIssue12Cfg.Int_(c.getConfig("int"), "int.", $tsCfgValidator)
            : new JavaIssue12Cfg.Int_(
                com.typesafe.config.ConfigFactory.parseString("int{}"), "int.", $tsCfgValidator);
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
