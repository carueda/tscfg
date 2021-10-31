package tscfg.example;

public class JavaIssue11Cfg {
  public final JavaIssue11Cfg.Foo foo;

  public static class Foo {
    public final java.lang.String clone;
    public final java.lang.String finalize;
    public final java.lang.String getClass;
    public final java.lang.String notify;
    public final java.lang.String notifyAll;
    public final java.lang.String toString;
    public final java.lang.String wait;

    public Foo(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.clone = c.hasPathOrNull("clone") ? c.getString("clone") : "..";
      this.finalize = c.hasPathOrNull("finalize") ? c.getString("finalize") : "..";
      this.getClass = c.hasPathOrNull("getClass") ? c.getString("getClass") : "..";
      this.notify = c.hasPathOrNull("notify") ? c.getString("notify") : "..";
      this.notifyAll = c.hasPathOrNull("notifyAll") ? c.getString("notifyAll") : "..";
      this.toString = c.hasPathOrNull("toString") ? c.getString("toString") : "..";
      this.wait = c.hasPathOrNull("wait") ? c.getString("wait") : "..";
    }
  }

  public JavaIssue11Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.foo =
        c.hasPathOrNull("foo")
            ? new JavaIssue11Cfg.Foo(c.getConfig("foo"), "foo.", $tsCfgValidator)
            : new JavaIssue11Cfg.Foo(
                com.typesafe.config.ConfigFactory.parseString("foo{}"), "foo.", $tsCfgValidator);
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
