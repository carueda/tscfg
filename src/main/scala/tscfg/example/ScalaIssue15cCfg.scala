package tscfg.example

object ScalaIssue15cCfg {
  object _$0$Elm_ {
    def apply(c: com.typesafe.config.Config): _$0$Elm_ = {
      _$0$Elm_(
        $list$dbl(c.getList("attrs")),
        c.getDouble("lat"),
        c.getDouble("lon")
      )
    }
  }
  case class _$0$Elm_(
    attrs : scala.collection.immutable.List[scala.Double],
    lat   : scala.Double,
    lon   : scala.Double
  )

  def apply(c: com.typesafe.config.Config): ScalaIssue15cCfg = {
    ScalaIssue15cCfg(
      $list_$0$Elm_(c.getList("positions"))
    )
  }

  private def $dbl(cv:com.typesafe.config.ConfigValue): scala.Double = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER)
      || !u.isInstanceOf[java.lang.Number]) throw $expE(cv, "double")
    u.asInstanceOf[java.lang.Number].doubleValue()
  }
  private def $expE(cv:com.typesafe.config.ConfigValue, exp:java.lang.String) = {
    val u: Any = cv.unwrapped
    new java.lang.RuntimeException(cv.origin.lineNumber +
      ": expecting: " + exp + " got: " +
      (if (u.isInstanceOf[java.lang.String]) "\"" + u + "\"" else u))
  }
  private def $list$dbl(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[scala.Double] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $dbl(cv)).toList
  }
  private def $list_$0$Elm_(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[ScalaIssue15cCfg._$0$Elm_] = {
    import scala.collection.JavaConversions._
    cl.map(cv => _$0$Elm_(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
  }
}
case class ScalaIssue15cCfg(
  positions : scala.collection.immutable.List[ScalaIssue15cCfg._$0$Elm_]
)

