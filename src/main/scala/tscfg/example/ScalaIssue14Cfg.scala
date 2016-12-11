package tscfg.example

case class ScalaIssue14Cfg(
  _0 : ScalaIssue14Cfg._0,
  _2 : java.lang.String
)
object ScalaIssue14Cfg {
  case class _0(
    _1 : java.lang.String
  )
  object _0 {
    def apply(c: com.typesafe.config.Config): ScalaIssue14Cfg._0 = {
      ScalaIssue14Cfg._0(
        _1 = if(c.hasPathOrNull("1")) c.getString("1") else "bar"
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue14Cfg = {
    ScalaIssue14Cfg(
      _0 = ScalaIssue14Cfg._0(c.getConfig("0")),
      _2 = if(c.hasPathOrNull("2")) c.getString("2") else "foo"
    )
  }
}
