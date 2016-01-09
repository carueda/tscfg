package mypackage;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;

public class Use {
  public static void main(String[] args) {
    File configFile = new File(args[0]);

    System.out.println("parsing: " + configFile);

    //Config tsConfig = ConfigFactory.systemEnvironment();
    Config tsConfig = ConfigFactory.parseFile(configFile).resolve();

    // but, instead of:
    String path    = tsConfig.getString("endpoint.path");
    String url     = tsConfig.getString("endpoint.url");
    Integer serial = tsConfig.getInt("endpoint.serial");
    int port       = tsConfig.getInt("endpoint.interface.port");

    // you can:
    TsCfg cfg = new TsCfg(tsConfig);
    String path2    = cfg.endpoint.path;
    String url2     = cfg.endpoint.url;
    Integer serials = cfg.endpoint.serial;
    int port2       = cfg.endpoint.interface_.port;

    System.out.println("*** TsCfg: *** ");
    System.out.println(cfg.toString());
  }
}
