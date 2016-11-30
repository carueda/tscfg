package tscfg.example

case class ScalaExampleCfg(
  endpoint : ScalaExampleCfg.Endpoint
)

object ScalaExampleCfg {
  case class Endpoint(
    intReq    : scala.Int,
    interface : Endpoint.Interface,
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
      def apply(c: com.typesafe.config.Config): Interface = {
        Interface(
          if(c.hasPathOrNull("port")) c.getInt("port") else 8080,
          if(c.hasPathOrNull("type")) Some(c.getString("type")) else None
        )
      }
    }
    def apply(c: com.typesafe.config.Config): Endpoint = {
      Endpoint(
        c.getInt("intReq"),
        Interface(c.getConfig("interface")),
        c.getString("path"),
        if(c.hasPathOrNull("serial")) Some(c.getInt("serial")) else None,
        if(c.hasPathOrNull("url")) c.getString("url") else "http://example.net"
      )
    }
  }
  def apply(c: com.typesafe.config.Config): ScalaExampleCfg = {
    ScalaExampleCfg(
      Endpoint(c.getConfig("endpoint"))
    )
  }
}
