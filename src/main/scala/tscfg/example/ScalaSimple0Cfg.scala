package tscfg.example

object ScalaSimple0Cfg {
  def apply(c: com.typesafe.config.Config) = build(Some(c))
  def apply() = build(None)
  def build(c: scala.Option[com.typesafe.config.Config]): ScalaSimple0Cfg = {
    ScalaSimple0Cfg(
      c.get.getString("foo"),
      c.get.getInt("num")
    )
  }
}
case class ScalaSimple0Cfg(
  foo : java.lang.String,
  num : scala.Int
)

