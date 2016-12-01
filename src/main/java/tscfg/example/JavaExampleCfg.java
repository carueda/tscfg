package tscfg.example;

public class JavaExampleCfg {
  public final Endpoint endpoint;

  public static class Endpoint {
    public final int intReq;
    public final Interface_ interface_;
    public final java.lang.String path;
    public final java.lang.Integer serial;
    public final java.lang.String url;

    public static class Interface_ {
      public final int port;
      public final java.lang.String type;

      public Interface_(com.typesafe.config.Config c) {
        this.port = c.hasPathOrNull("port") ? c.getInt("port") : 8080;
        this.type = c.hasPathOrNull("type") ? c.getString("type") : null;
      }
    }

    public Endpoint(com.typesafe.config.Config c) {
      this.intReq = c.getInt("intReq");
      this.interface_ = new Interface_(_$config(c, "interface"));
      this.path = c.getString("path");
      this.serial = c.hasPathOrNull("serial") ? c.getInt("serial") : null;
      this.url = c.hasPathOrNull("url") ? c.getString("url") : "http://example.net";
    }
  }

  public JavaExampleCfg(com.typesafe.config.Config c) {
    this.endpoint = new Endpoint(_$config(c, "endpoint"));
  }

  private static com.typesafe.config.Config _$config(com.typesafe.config.Config c, java.lang.String path) {
    return c.hasPathOrNull(path) ? c.getConfig(path) : null;
  }
}

