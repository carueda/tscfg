package tscfg.example;

public class JavaDuration2Cfg {
  public final JavaDuration2Cfg.Durations durations;

  public static class Durations {
    public final java.time.Duration days;
    public final java.time.Duration duration_dy;
    public final java.time.Duration duration_hr;
    public final java.time.Duration duration_mi;
    public final java.time.Duration duration_ms;
    public final java.time.Duration duration_ns;
    public final java.time.Duration duration_se;
    public final java.time.Duration duration_µs;
    public final java.time.Duration hours;
    public final java.time.Duration millis;

    public Durations(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.days = c.hasPathOrNull("days") ? c.getDuration("days") : null;
      this.duration_dy =
          c.hasPathOrNull("duration_dy")
              ? c.getDuration("duration_dy")
              : java.time.Duration.parse("PT0S");
      this.duration_hr =
          c.hasPathOrNull("duration_hr")
              ? c.getDuration("duration_hr")
              : java.time.Duration.parse("PT0S");
      this.duration_mi =
          c.hasPathOrNull("duration_mi")
              ? c.getDuration("duration_mi")
              : java.time.Duration.parse("PT0S");
      this.duration_ms =
          c.hasPathOrNull("duration_ms")
              ? c.getDuration("duration_ms")
              : java.time.Duration.parse("PT0S");
      this.duration_ns =
          c.hasPathOrNull("duration_ns")
              ? c.getDuration("duration_ns")
              : java.time.Duration.parse("PT0S");
      this.duration_se =
          c.hasPathOrNull("duration_se")
              ? c.getDuration("duration_se")
              : java.time.Duration.parse("PT0S");
      this.duration_µs =
          c.hasPathOrNull("duration_µs")
              ? c.getDuration("duration_µs")
              : java.time.Duration.parse("PT0S");
      this.hours = c.getDuration("hours");
      this.millis =
          c.hasPathOrNull("millis") ? c.getDuration("millis") : java.time.Duration.parse("PT9M10S");
    }
  }

  public JavaDuration2Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.durations =
        c.hasPathOrNull("durations")
            ? new JavaDuration2Cfg.Durations(
                c.getConfig("durations"), "durations.", $tsCfgValidator)
            : new JavaDuration2Cfg.Durations(
                com.typesafe.config.ConfigFactory.parseString("durations{}"),
                "durations.",
                $tsCfgValidator);
    $tsCfgValidator.validate();
  }

  private static final class $TsCfgValidator {
    private final java.util.List<java.lang.String> badPaths = new java.util.ArrayList<>();

    void addBadPath(java.lang.String path, com.typesafe.config.ConfigException e) {
      badPaths.add("'" + path + "': " + e.getClass().getName() + "(" + e.getMessage() + ")");
    }

    void validate() {
      if (!badPaths.isEmpty()) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("Invalid configuration:");
        for (java.lang.String path : badPaths) {
          sb.append("\n    ").append(path);
        }
        throw new com.typesafe.config.ConfigException(sb.toString()) {};
      }
    }
  }
}
