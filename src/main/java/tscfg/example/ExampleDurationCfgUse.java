package tscfg.example;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * <pre>
    > runMain tscfg.example.ExampleDurationCfgUse
    [info] Running tscfg.example.ExampleDurationCfgUse
    Input:
      durations {
        days = "48hour"
        hours = "1d"
      }

    Output:
      durations {
          days = 2
          hours = 24
          millis = 550000
      }
 * </pre>
 */
public class ExampleDurationCfgUse {
  public static void main(String[] args) {
    String input =
        "durations {\n" +
        "  days = \"48hour\"\n" +
        "  hours = \"1d\"\n" +
        "}";

    System.out.println("Input:\n  " + input.replace("\n", "\n  "));

    Config tsConfig = ConfigFactory.parseString(input).resolve();
    ExampleDurationCfg cfg = new ExampleDurationCfg(tsConfig);
    Long days = cfg.durations.days;
    long hours = cfg.durations.hours;
    long millis = cfg.durations.millis;

    System.out.println("\nOutput:\n  " + cfg.toString().replace("\n", "\n  "));
  }
}
