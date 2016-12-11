package tscfg.example;

public class JavaIssue13Cfg {
  public final JavaIssue13Cfg.Issue issue;
  public static class Issue {
      public final java.lang.String optionalFoo;
      
      public Issue(com.typesafe.config.Config c) {
        this.optionalFoo = c.hasPathOrNull("optionalFoo") ? c.getString("optionalFoo") : null;
      }
    }
    
  public JavaIssue13Cfg(com.typesafe.config.Config c) {
    this.issue = new JavaIssue13Cfg.Issue(c.getConfig("issue"));
  }
}
