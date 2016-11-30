package tscfg.example

case class ScalaExample1Cfg(
  bazOptionalWithDefault   : java.lang.String,
  bazOptionalWithNoDefault : scala.Option[java.lang.String],
  fooRequired              : java.lang.String
)

object ScalaExample1Cfg {
  def apply(c: com.typesafe.config.Config): ScalaExample1Cfg = {
    ScalaExample1Cfg(
      c.getString("bazOptionalWithDefault"),
      if(c.hasPathOrNull("bazOptionalWithNoDefault")) Some(c.getString("bazOptionalWithNoDefault")) else None,
      c.getString("fooRequired")
    )
  }
}
