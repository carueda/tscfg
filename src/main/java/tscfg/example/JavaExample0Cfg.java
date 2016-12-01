package tscfg.example;

public class JavaExample0Cfg {
  public final Service service;

  public static class Service {
    public final boolean debug;
    public final double factor;
    public final int poolSize;
    public final java.lang.String url;

    public Service(com.typesafe.config.Config c) {
      this.debug = c.getBoolean("debug");
      this.factor = c.getDouble("factor");
      this.poolSize = c.getInt("poolSize");
      this.url = c.getString("url");
    }
  }

  public JavaExample0Cfg(com.typesafe.config.Config c) {
    this.service = new Service(_$config(c, "service"));
  }

  private static com.typesafe.config.Config _$config(com.typesafe.config.Config c, java.lang.String path) {
    return c.hasPathOrNull(path) ? c.getConfig(path) : null;
  }
}

