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
    def apply(c: com.typesafe.config.Config): _0 = {
      _0(
        c.getString("1")
      )
    }
  }
  def apply(c: com.typesafe.config.Config): ScalaIssue14Cfg = {
    ScalaIssue14Cfg(
      _0(c.getConfig("0")),
      c.getString("2")
    )
  }
}
