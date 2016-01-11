package tscfg.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import java.io.File;

public class Use {
  public static void main(String[] args) {
    File configFile = new File(args[0]);

    // usual Typesafe Config mechanism to load the file
    Config tsConfig = ConfigFactory.parseFile(configFile).resolve();

    // but, instead of:
    Config endpoint = tsConfig.getConfig("endpoint");
//    String path    = endpoint.getString("path");
//    String url     = endpoint.hasPath("url")    ? endpoint.getString("url") : "http://example.net";
//    Integer serial = endpoint.hasPath("serial") ? endpoint.getInt("serial") : null;
//    int port       = endpoint.hasPath("port")   ? endpoint.getInt("interface.port") : 8080;

    // you can:
    ExampleCfg cfg = new ExampleCfg(tsConfig);
    String path2    = cfg.endpoint.path;
    String url2     = cfg.endpoint.url;
    Integer serial2 = cfg.endpoint.serial;
    int port2       = cfg.endpoint.interface_.port;

    System.out.println("port2=" + port2);
    System.out.println("*** TsCfg: *** ");
    System.out.println(cfg.toString());

    ConfigRenderOptions options = ConfigRenderOptions.defaults()
        .setFormatted(true).setComments(true).setOriginComments(false);
    System.out.println("*** tsConfig: ***");
    System.out.println(tsConfig.root().render(options));
  }
}
