package tscfg.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import java.io.File;

public class JavaUse {
  public static void main(String[] args) {
    File configFile = new File(args[0]);

    // usual Typesafe Config mechanism to load the file
    Config tsConfig = ConfigFactory.parseFile(configFile).resolve();

    // but, instead of:
    Config endpoint = tsConfig.getConfig("endpoint");
    String path    = endpoint.getString("path");
    String url     = endpoint.hasPathOrNull("url")    ? endpoint.getString("url") : "http://example.net";
    Integer serial = endpoint.hasPathOrNull("serial") ? endpoint.getInt("serial") : null;
    int port       = endpoint.hasPathOrNull("port")   ? endpoint.getInt("interface.port") : 8080;
    String type    = endpoint.hasPathOrNull("type")   ? endpoint.getString("interface.type") : null;

    // you can:
    JavaExampleCfg cfg = new JavaExampleCfg(tsConfig);
    String path2    = cfg.endpoint.path;
    String url2     = cfg.endpoint.url;
    Integer serial2 = cfg.endpoint.serial;
    int port2       = cfg.endpoint.interface_.port;
    String type2    = cfg.endpoint.interface_.type;

    System.out.println("\n*** tscfg toString: ***");
    System.out.println(cfg.toString());

    System.out.println("\n*** Typesafe Config toString: ***");
    ConfigRenderOptions options = ConfigRenderOptions.defaults()
        .setFormatted(true).setComments(true).setOriginComments(false);
    System.out.println(tsConfig.root().render(options));
  }
}
