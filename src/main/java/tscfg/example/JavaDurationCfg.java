package tscfg.example;

public class JavaDurationCfg {
    public final Durations durations;

    public static class Durations {
        public final java.lang.Long days;
        public final long hours;
        public final long millis;

        public Durations(com.typesafe.config.Config c) {
            this.days = c != null && c.hasPathOrNull("days") ? c.getDuration("days", java.util.concurrent.TimeUnit.DAYS) : null;
            this.hours = c.getDuration("hours", java.util.concurrent.TimeUnit.HOURS);
            this.millis = c != null && c.hasPathOrNull("millis") ? c.getDuration("millis", java.util.concurrent.TimeUnit.MILLISECONDS) : 550000;
        }
    }

    public JavaDurationCfg(com.typesafe.config.Config c) {
        this.durations = new Durations(_$config(c, "durations"));
    }

    private static com.typesafe.config.Config _$config(com.typesafe.config.Config c, java.lang.String path) {
      return c != null && c.hasPathOrNull(path) ? c.getConfig(path) : null;
    }
}

