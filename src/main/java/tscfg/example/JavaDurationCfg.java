package tscfg.example;

public class JavaDurationCfg {
  public final JavaDurationCfg.Durations durations;
  public static class Durations {
      public final java.lang.Long days;
        public final long duration_dy;
        public final long duration_hr;
        public final long duration_mi;
        public final long duration_ms;
        public final long duration_ns;
        public final long duration_se;
        public final long duration_µs;
        public final long hours;
        public final long millis;
      
      public Durations(com.typesafe.config.Config c) {
        this.days = c.hasPathOrNull("days") ? c.getDuration("days", java.util.concurrent.TimeUnit.DAYS) : null;
          this.duration_dy = c.hasPathOrNull("duration_dy") ? c.getDuration("duration_dy", java.util.concurrent.TimeUnit.DAYS) : 0;
          this.duration_hr = c.hasPathOrNull("duration_hr") ? c.getDuration("duration_hr", java.util.concurrent.TimeUnit.HOURS) : 0;
          this.duration_mi = c.hasPathOrNull("duration_mi") ? c.getDuration("duration_mi", java.util.concurrent.TimeUnit.MINUTES) : 0;
          this.duration_ms = c.hasPathOrNull("duration_ms") ? c.getDuration("duration_ms", java.util.concurrent.TimeUnit.MILLISECONDS) : 0;
          this.duration_ns = c.hasPathOrNull("duration_ns") ? c.getDuration("duration_ns", java.util.concurrent.TimeUnit.NANOSECONDS) : 0;
          this.duration_se = c.hasPathOrNull("duration_se") ? c.getDuration("duration_se", java.util.concurrent.TimeUnit.SECONDS) : 0;
          this.duration_µs = c.hasPathOrNull("duration_µs") ? c.getDuration("duration_µs", java.util.concurrent.TimeUnit.MICROSECONDS) : 0;
          this.hours = c.getDuration("hours", java.util.concurrent.TimeUnit.HOURS);
          this.millis = c.hasPathOrNull("millis") ? c.getDuration("millis", java.util.concurrent.TimeUnit.MILLISECONDS) : 550000;
      }
    }
    
  public JavaDurationCfg(com.typesafe.config.Config c) {
    this.durations = new JavaDurationCfg.Durations(c.getConfig("durations"));
  }
}
