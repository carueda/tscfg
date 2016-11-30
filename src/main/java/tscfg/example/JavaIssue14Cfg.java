package tscfg.example;

public class JavaIssue14Cfg {
  public final _0 _0;
  public final java.lang.String _2;

  public static class _0 {
    public final java.lang.String _1;

    public _0(com.typesafe.config.Config c) {
      this._1 = c.getString("1");
    }
  }

  public JavaIssue14Cfg(com.typesafe.config.Config c) {
    this._0 = new _0(_$config(c, "0"));
    this._2 = c.getString("2");
  }

  private static com.typesafe.config.Config _$config(com.typesafe.config.Config c, java.lang.String path) {
    return c.hasPathOrNull(path) ? c.getConfig(path) : null;
  }
}

