package tscfg.example

case class ScalaExampleCfg(
  endpoint : ScalaExampleCfg.Endpoint
)
object ScalaExampleCfg {
  case class Endpoint(
    intReq    : scala.Int,
    interface : ScalaExampleCfg.Endpoint.Interface,
    path      : java.lang.String,
    serial    : scala.Option[scala.Int],
    url       : java.lang.String
  )
  object Endpoint {
    case class Interface(
      port   : scala.Int,
      `type` : scala.Option[java.lang.String]
    )
    object Interface {
      def apply(c: com.typesafe.config.Config): ScalaExampleCfg.Endpoint.Interface = {
        ScalaExampleCfg.Endpoint.Interface(
          port   = if(c.hasPathOrNull("port")) c.getInt("port") else 8080,
          `type` = if(c.hasPathOrNull("type")) Some(c.getString("type")) else None
        )
      }
    }
          
    def apply(c: com.typesafe.config.Config): ScalaExampleCfg.Endpoint = {
      ScalaExampleCfg.Endpoint(
        intReq    = c.getInt("intReq"),
        interface = ScalaExampleCfg.Endpoint.Interface(c.getConfig("interface")),
        path      = c.getString("path"),
        serial    = if(c.hasPathOrNull("serial")) Some(c.getInt("serial")) else None,
        url       = if(c.hasPathOrNull("url")) c.getString("url") else "http://example.net"
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaExampleCfg = {
    ScalaExampleCfg(
      endpoint = ScalaExampleCfg.Endpoint(c.getConfig("endpoint"))
    )
  }
}
      
