package tscfg.example

case class ScalaDurationCfg(
  durations : ScalaDurationCfg.Durations
)
object ScalaDurationCfg {
  case class Durations(
    days        : scala.Option[scala.Long],
    duration_dy : scala.Long,
    duration_hr : scala.Long,
    duration_mi : scala.Long,
    duration_ms : scala.Long,
    duration_ns : scala.Long,
    duration_se : scala.Long,
    duration_µs : scala.Long,
    hours       : scala.Long,
    millis      : scala.Long
  )
  object Durations {
    def apply(c: com.typesafe.config.Config): ScalaDurationCfg.Durations = {
      ScalaDurationCfg.Durations(
        days        = if(c.hasPathOrNull("days")) Some(c.getDuration("days", java.util.concurrent.TimeUnit.DAYS)) else None,
        duration_dy = if(c.hasPathOrNull("duration_dy")) c.getDuration("duration_dy", java.util.concurrent.TimeUnit.DAYS) else 0,
        duration_hr = if(c.hasPathOrNull("duration_hr")) c.getDuration("duration_hr", java.util.concurrent.TimeUnit.HOURS) else 0,
        duration_mi = if(c.hasPathOrNull("duration_mi")) c.getDuration("duration_mi", java.util.concurrent.TimeUnit.MINUTES) else 0,
        duration_ms = if(c.hasPathOrNull("duration_ms")) c.getDuration("duration_ms", java.util.concurrent.TimeUnit.MILLISECONDS) else 0,
        duration_ns = if(c.hasPathOrNull("duration_ns")) c.getDuration("duration_ns", java.util.concurrent.TimeUnit.NANOSECONDS) else 0,
        duration_se = if(c.hasPathOrNull("duration_se")) c.getDuration("duration_se", java.util.concurrent.TimeUnit.SECONDS) else 0,
        duration_µs = if(c.hasPathOrNull("duration_µs")) c.getDuration("duration_µs", java.util.concurrent.TimeUnit.MICROSECONDS) else 0,
        hours       = c.getDuration("hours", java.util.concurrent.TimeUnit.HOURS),
        millis      = if(c.hasPathOrNull("millis")) c.getDuration("millis", java.util.concurrent.TimeUnit.MILLISECONDS) else 550000
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaDurationCfg = {
    ScalaDurationCfg(
      durations = ScalaDurationCfg.Durations(c.getConfig("durations"))
    )
  }
}
