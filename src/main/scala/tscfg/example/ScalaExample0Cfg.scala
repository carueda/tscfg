package tscfg.example

case class ScalaExample0Cfg(
  service : ScalaExample0Cfg.Service
)

object ScalaExample0Cfg {
  case class Service(
    debug    : scala.Boolean,
    factor   : scala.Double,
    poolSize : scala.Int,
    url      : java.lang.String
  )

  object Service {
    def apply(c: com.typesafe.config.Config): Service = {
      Service(
        c.getBoolean("debug"),
        c.getDouble("factor"),
        c.getInt("poolSize"),
        c.getString("url")
      )
    }
  }
  def apply(c: com.typesafe.config.Config): ScalaExample0Cfg = {
    ScalaExample0Cfg(
      Service(c.getConfig("service"))
    )
  }
}
