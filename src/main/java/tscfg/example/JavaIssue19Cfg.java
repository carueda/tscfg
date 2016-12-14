package tscfg.example;

public class JavaIssue19Cfg {
  public final java.lang.String _$_foo_;
  public final boolean _do_log_;
  
  public JavaIssue19Cfg(com.typesafe.config.Config c) {
    this._$_foo_ = c.hasPathOrNull("\"$_foo\"") ? c.getString("\"$_foo\"") : "baz";
    this._do_log_ = c.getBoolean("\"do log\"");
  }
}
