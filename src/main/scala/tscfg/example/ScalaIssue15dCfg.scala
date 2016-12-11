package tscfg.example

case class ScalaIssue15dCfg(
  baz : scala.List[scala.List[ScalaIssue15dCfg.Baz$Elm]]
)
object ScalaIssue15dCfg {
  case class Baz$Elm(
    aa : scala.Option[scala.Boolean],
    dd : scala.Double
  )
  object Baz$Elm {
    def apply(c: com.typesafe.config.Config): ScalaIssue15dCfg.Baz$Elm = {
      ScalaIssue15dCfg.Baz$Elm(
        aa = if(c.hasPathOrNull("aa")) Some(c.getBoolean("aa")) else None,
        dd = c.getDouble("dd")
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue15dCfg = {
    ScalaIssue15dCfg(
      baz = $_L$_LScalaIssue15dCfg_Baz$Elm(c.getList("baz"))
    )
  }
  private def $_L$_LScalaIssue15dCfg_Baz$Elm(cl:com.typesafe.config.ConfigList): scala.List[scala.List[ScalaIssue15dCfg.Baz$Elm]] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_LScalaIssue15dCfg_Baz$Elm(cv.asInstanceOf[com.typesafe.config.ConfigList])).toList
  }
  private def $_LScalaIssue15dCfg_Baz$Elm(cl:com.typesafe.config.ConfigList): scala.List[ScalaIssue15dCfg.Baz$Elm] = {
    import scala.collection.JavaConversions._
    cl.map(cv => ScalaIssue15dCfg.Baz$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
  }
}
