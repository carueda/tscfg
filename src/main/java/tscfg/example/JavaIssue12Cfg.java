package tscfg.example;

public class JavaIssue12Cfg {
  public final Boolean Boolean;
  public final Option Option;
  public final String String;
  public final Int_ int_;

  public static class Boolean {
    public final java.lang.String bar;

    public Boolean(com.typesafe.config.Config c) {
      this.bar = c.getString("bar");
    }
  }
  public static class Option {
    public final java.lang.String bar;

    public Option(com.typesafe.config.Config c) {
      this.bar = c.getString("bar");
    }
  }
  public static class String {
    public final java.lang.String bar;

    public String(com.typesafe.config.Config c) {
      this.bar = c.getString("bar");
    }
  }
  public static class Int_ {
    public final int bar;

    public Int_(com.typesafe.config.Config c) {
      this.bar = c.getInt("bar");
    }
  }

  public JavaIssue12Cfg(com.typesafe.config.Config c) {
    this.Boolean = new Boolean(_$config(c, "Boolean"));
    this.Option = new Option(_$config(c, "Option"));
    this.String = new String(_$config(c, "String"));
    this.int_ = new Int_(_$config(c, "int"));
  }

  private static com.typesafe.config.Config _$config(com.typesafe.config.Config c, java.lang.String path) {
    return c.hasPathOrNull(path) ? c.getConfig(path) : null;
  }
}

