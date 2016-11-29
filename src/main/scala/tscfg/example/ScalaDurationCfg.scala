package tscfg.example

case class ScalaDurationCfg(
  durations : ScalaDurationCfg.Durations
)

object ScalaDurationCfg {
  case class Durations(
    days   : scala.Option[scala.Long],
    hours  : scala.Long,
    millis : scala.Long
  )

  object Durations {
    def apply(c: com.typesafe.config.Config): Durations = {
      Durations(
        if(c.hasPathOrNull("days")) Some(c.getDuration("days", java.util.concurrent.TimeUnit.DAYS)) else None,
        c.getDuration("hours", java.util.concurrent.TimeUnit.HOURS),
        if(c.hasPathOrNull("millis")) c.getDuration("millis", java.util.concurrent.TimeUnit.MILLISECONDS) else 550000
      )
    }
  }
  def apply(c: com.typesafe.config.Config): ScalaDurationCfg = {
    ScalaDurationCfg(
      Durations(c.getConfig("durations"))
    )
  }
}
