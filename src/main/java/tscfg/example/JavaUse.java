package tscfg.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import java.io.File;

/*
 * sbt> runMain tscfg.example.JavaUse src/main/tscfg/example/example.conf
 */
public class JavaUse {
  public static void main(String[] args) {
    String configFilename = args.length > 0 ? args[0] : "src/main/tscfg/example/example.conf";
    System.out.println("Loading " + configFilename);
    File configFile = new File(configFilename);

    // usual Typesafe Config mechanism to load the file
    Config tsConfig = ConfigFactory.parseFile(configFile).resolve();
  
    // create instance of the tscfg generated main class. This will
    // perform all validations according to required properties and types:
    JavaExampleCfg cfg = new JavaExampleCfg(tsConfig);
  
    // access the configuration properties in a type-safe fashion while also
    // enjoying your IDE features for code completion, navigation, etc:
    String path    = cfg.endpoint.path;
    String url     = cfg.endpoint.url;
    Integer serial = cfg.endpoint.serial;
    int port       = cfg.endpoint.interface_.port;
    String type    = cfg.endpoint.interface_.type;
  
    System.out.println("\n*** tscfg POJO structure (in JSON): *** ");
    System.out.println("  " + toJson(cfg).replaceAll("\n", "\n  "));
  
    System.out.println("\n*** Typesafe rendering of input Config object: *** ");
    ConfigRenderOptions options = ConfigRenderOptions.defaults()
        .setFormatted(true).setComments(true).setOriginComments(false);
    System.out.println(tsConfig.root().render(options));
  }

  private static String toJson(JavaExampleCfg cfg) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(cfg);
  }
}
