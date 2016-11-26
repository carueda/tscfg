package tscfg.example

object ScalaIssue15dCfg {
  object _$0$Elm_ {
    def apply(c: com.typesafe.config.Config): _$0$Elm_ = {
      _$0$Elm_(
        if (c.hasPathOrNull("aa")) Some(c.getBoolean("aa")) else None,
        c.getDouble("dd")
      )
    }
  }
  case class _$0$Elm_(
    aa : scala.Option[scala.Boolean],
    dd : scala.Double
  )


  def apply(c: com.typesafe.config.Config): ScalaIssue15dCfg = {
    ScalaIssue15dCfg(
      $list$list_$0$Elm_(c.getList("baz"))
    )
  }

  private def $list$list_$0$Elm_(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[scala.collection.immutable.List[ScalaIssue15dCfg._$0$Elm_]] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $list_$0$Elm_(cv.asInstanceOf[com.typesafe.config.ConfigList])).toList
  }
  private def $list_$0$Elm_(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[ScalaIssue15dCfg._$0$Elm_] = {
    import scala.collection.JavaConversions._
    cl.map(cv => _$0$Elm_(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
  }
}
case class ScalaIssue15dCfg(
  baz : scala.collection.immutable.List[scala.collection.immutable.List[ScalaIssue15dCfg._$0$Elm_]]
)

