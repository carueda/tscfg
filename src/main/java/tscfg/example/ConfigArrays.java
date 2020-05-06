package tscfg.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

// see https://github.com/lightbend/config/issues/685
public class ConfigArrays {
  public static void main(String[] args) {
    String[] strings = {
        "list = [0, 1] | [2,3]",
        "list = [0]  bar baz [1,2,3]",
        "list = [0] abc [bar, baz] ||| xyz [1,2,3]",
    };
    for (String input: strings) {
      System.out.println("1. `" + input + "`");
    }
    
    for (String input: strings) {
      Config c = ConfigFactory.parseString(input).resolve();
      ConfigRenderOptions options = ConfigRenderOptions.defaults().setOriginComments(false);
      String r = c.root().render(options);
      System.out.println("1. `" + input + "`:\n\n"+
          "        " + r.replaceAll("\n", "\n        "));
    }
  }
}
