package tscfg.example

object ScalaIssue15dCfg {
  object Baz$Elm {
    def apply(c: com.typesafe.config.Config): Baz$Elm = {
      Baz$Elm(
        if (c.hasPathOrNull("aa")) Some(c.getBoolean("aa")) else None,
        c.getDouble("dd")
      )
    }
  }
  case class Baz$Elm(
    aa : scala.Option[scala.Boolean],
    dd : scala.Double
  )


  def apply(c: com.typesafe.config.Config): ScalaIssue15dCfg = {
    ScalaIssue15dCfg(
      $list$listBaz$Elm(c.getList("baz"))
    )
  }

  private def $list$listBaz$Elm(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[scala.collection.immutable.List[Baz$Elm]] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $listBaz$Elm(cv.asInstanceOf[com.typesafe.config.ConfigList])).toList
  }
  private def $listBaz$Elm(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[Baz$Elm] = {
    import scala.collection.JavaConversions._
    cl.map(cv => Baz$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
  }
}
case class ScalaIssue15dCfg(
  baz : scala.collection.immutable.List[scala.collection.immutable.List[ScalaIssue15dCfg.Baz$Elm]]
)

