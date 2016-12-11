package tscfg.example;

public class JavaIssue14Cfg {
  public final JavaIssue14Cfg._0 _0;
  public final java.lang.String _2;
  public static class _0 {
    public final java.lang.String _1;
    
    public _0(com.typesafe.config.Config c) {
      this._1 = c.hasPathOrNull("1") ? c.getString("1") : "bar";
    }
  }
  
  public JavaIssue14Cfg(com.typesafe.config.Config c) {
    this._0 = new JavaIssue14Cfg._0(c.getConfig("0"));
    this._2 = c.hasPathOrNull("2") ? c.getString("2") : "foo";
  }
}
