package tscfg.example

object ScalaSimple0Cfg {
  object Aaa {
    def apply(c: com.typesafe.config.Config) = build(Some(c))
    def apply() = build(None)
    def build(c: scala.Option[com.typesafe.config.Config]): Aaa = {
      Aaa(
        c.get.getLong("bbb")
      )
    }
  }
  case class Aaa(
    bbb : scala.Long
  )

  def apply(c: com.typesafe.config.Config) = build(Some(c))
  def apply() = build(None)
  def build(c: scala.Option[com.typesafe.config.Config]): ScalaSimple0Cfg = {
    ScalaSimple0Cfg(
      Aaa.build(c.map(c => if (c.hasPath("aaa")) Some(c.getConfig("aaa")) else None).get),
      c.get.getString("foo"),
      c.get.getInt("num")
    )
  }
}
case class ScalaSimple0Cfg(
  aaa : ScalaSimple0Cfg.Aaa,
  foo : java.lang.String,
  num : scala.Int
)

