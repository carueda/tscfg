package tscfg.example;

public class JavaExample0Cfg {
  public final JavaExample0Cfg.Service service;
  public static class Service {
    public final boolean debug;
    public final double factor;
    public final int poolSize;
    public final java.lang.String url;
    
    public Service(com.typesafe.config.Config c) {
      this.debug = c.hasPathOrNull("debug") ? c.getBoolean("debug") : true;
      this.factor = c.hasPathOrNull("factor") ? c.getDouble("factor") : 0.75;
      this.poolSize = c.hasPathOrNull("poolSize") ? c.getInt("poolSize") : 32;
      this.url = c.hasPathOrNull("url") ? c.getString("url") : "http://example.net/rest";
    }
  }
  
  public JavaExample0Cfg(com.typesafe.config.Config c) {
    this.service = new JavaExample0Cfg.Service(c.getConfig("service"));
  }
}
