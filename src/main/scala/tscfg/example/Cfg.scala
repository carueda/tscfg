package tscfg.example

case class Cfg(
  $_xyz     : scala.Option[java.lang.String],
  positions : scala.List[scala.List[Cfg.$_E1]]
)
object Cfg {
  case class $_E1(
    attrs : scala.Option[scala.List[scala.Boolean]],
    lat   : scala.Double,
    lon   : scala.Double
  )
  object $_E1 {
    def apply(c: com.typesafe.config.Config): $_E1 = {
      $_E1(
        attrs = if(c.hasPathOrNull("attrs")) scala.Some($_L$_bln(c.getList("attrs"))) else None,
        lat   = c.getDouble("lat"),
        lon   = c.getDouble("lon")
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): Cfg = {
    Cfg(
      $_xyz     = if(c.hasPathOrNull("$_xyz")) Some(c.getString("$_xyz")) else None,
      positions = $_L$_LCfg_$_E1(c.getList("positions"))
    )
  }
  private def $_LCfg_$_E1(cl:com.typesafe.config.ConfigList): scala.List[Cfg.$_E1] = {
    import scala.collection.JavaConversions._
    cl.map(cv => Cfg.$_E1(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
  }
  private def $_L$_LCfg_$_E1(cl:com.typesafe.config.ConfigList): scala.List[scala.List[Cfg.$_E1]] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_LCfg_$_E1(cv.asInstanceOf[com.typesafe.config.ConfigList])).toList
  }

  private def $_bln(cv:com.typesafe.config.ConfigValue): scala.Boolean = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.BOOLEAN) || !u.isInstanceOf[java.lang.Boolean])
      throw $_expE(cv, "boolean")
    u.asInstanceOf[java.lang.Boolean].booleanValue()
  }
  private def $_expE(cv:com.typesafe.config.ConfigValue, exp:java.lang.String) = {
    val u: Any = cv.unwrapped
    new java.lang.RuntimeException(cv.origin.lineNumber +
      ": expecting: " + exp + " got: " +
      (if (u.isInstanceOf[java.lang.String]) "\"" + u + "\"" else u))
  }
  private def $_L$_bln(cl:com.typesafe.config.ConfigList): scala.List[scala.Boolean] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_bln(cv)).toList
  }
}
