package tscfg.example;

public class JavaIssue13Cfg {
  public final Issue issue;

  public static class Issue {
    public final java.lang.String optionalFoo;

    public Issue(com.typesafe.config.Config c) {
      this.optionalFoo = c.hasPathOrNull("optionalFoo") ? c.getString("optionalFoo") : null;
    }
  }

  public JavaIssue13Cfg(com.typesafe.config.Config c) {
    this.issue = new Issue(_$config(c, "issue"));
  }

  private static com.typesafe.config.Config _$config(com.typesafe.config.Config c, java.lang.String path) {
    return c.hasPathOrNull(path) ? c.getConfig(path) : null;
  }
}

