package tscfg.example

case class ScalaExample0Cfg(
  service : ScalaExample0Cfg.Service
)
object ScalaExample0Cfg {
  case class Service(
    debug    : scala.Boolean,
    doLog    : scala.Boolean,
    factor   : scala.Double,
    poolSize : scala.Int,
    url      : java.lang.String
  )
  object Service {
    def apply(c: com.typesafe.config.Config): ScalaExample0Cfg.Service = {
      ScalaExample0Cfg.Service(
        debug    = !c.hasPathOrNull("debug") || c.getBoolean("debug"),
        doLog    = c.hasPathOrNull("doLog") && c.getBoolean("doLog"),
        factor   = if(c.hasPathOrNull("factor")) c.getDouble("factor") else 0.75,
        poolSize = if(c.hasPathOrNull("poolSize")) c.getInt("poolSize") else 32,
        url      = if(c.hasPathOrNull("url")) c.getString("url") else "http://example.net/rest"
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaExample0Cfg = {
    ScalaExample0Cfg(
      service = ScalaExample0Cfg.Service(c.getConfig("service"))
    )
  }
}
