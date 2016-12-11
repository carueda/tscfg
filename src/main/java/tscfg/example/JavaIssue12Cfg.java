package tscfg.example;

public class JavaIssue12Cfg {
  public final JavaIssue12Cfg.Boolean Boolean;
  public final JavaIssue12Cfg.Option Option;
  public final JavaIssue12Cfg.String String;
  public final JavaIssue12Cfg.Int_ int_;
  public static class Boolean {
    public final java.lang.String bar;
    
    public Boolean(com.typesafe.config.Config c) {
      this.bar = c.hasPathOrNull("bar") ? c.getString("bar") : "foo";
    }
  }
  
  public static class Option {
    public final java.lang.String bar;
    
    public Option(com.typesafe.config.Config c) {
      this.bar = c.hasPathOrNull("bar") ? c.getString("bar") : "baz";
    }
  }
  
  public static class String {
    public final java.lang.String bar;
    
    public String(com.typesafe.config.Config c) {
      this.bar = c.hasPathOrNull("bar") ? c.getString("bar") : "baz";
    }
  }
  
  public static class Int_ {
    public final java.lang.Integer bar;
    
    public Int_(com.typesafe.config.Config c) {
      this.bar = c.hasPathOrNull("bar") ? c.getInt("bar") : 1;
    }
  }
  
  public JavaIssue12Cfg(com.typesafe.config.Config c) {
    this.Boolean = new JavaIssue12Cfg.Boolean(c.getConfig("Boolean"));
    this.Option = new JavaIssue12Cfg.Option(c.getConfig("Option"));
    this.String = new JavaIssue12Cfg.String(c.getConfig("String"));
    this.int_ = new JavaIssue12Cfg.Int_(c.getConfig("int"));
  }
}
