package tscfg.example

case class ScalaDurationCfg(
  durations : ScalaDurationCfg.Durations
)
object ScalaDurationCfg {
  case class Durations(
    days        : scala.Option[scala.Long],
    duration_dy : scala.Long,
    duration_hr : scala.Long,
    duration_mi : scala.Long,
    duration_ms : scala.Long,
    duration_ns : scala.Long,
    duration_se : scala.Long,
    duration_µs : scala.Long,
    hours       : scala.Long,
    millis      : scala.Long
  )
  object Durations {
    def apply(c: com.typesafe.config.Config): ScalaDurationCfg.Durations = {
      ScalaDurationCfg.Durations(
        days        = if(c.hasPathOrNull("days")) Some(c.getDuration("days", java.util.concurrent.TimeUnit.DAYS)) else None,
        duration_dy = if(c.hasPathOrNull("duration_dy")) c.getDuration("duration_dy", java.util.concurrent.TimeUnit.DAYS) else 0,
        duration_hr = if(c.hasPathOrNull("duration_hr")) c.getDuration("duration_hr", java.util.concurrent.TimeUnit.HOURS) else 0,
        duration_mi = if(c.hasPathOrNull("duration_mi")) c.getDuration("duration_mi", java.util.concurrent.TimeUnit.MINUTES) else 0,
        duration_ms = if(c.hasPathOrNull("duration_ms")) c.getDuration("duration_ms", java.util.concurrent.TimeUnit.MILLISECONDS) else 0,
        duration_ns = if(c.hasPathOrNull("duration_ns")) c.getDuration("duration_ns", java.util.concurrent.TimeUnit.NANOSECONDS) else 0,
        duration_se = if(c.hasPathOrNull("duration_se")) c.getDuration("duration_se", java.util.concurrent.TimeUnit.SECONDS) else 0,
        duration_µs = if(c.hasPathOrNull("duration_µs")) c.getDuration("duration_µs", java.util.concurrent.TimeUnit.MICROSECONDS) else 0,
        hours       = c.getDuration("hours", java.util.concurrent.TimeUnit.HOURS),
        millis      = if(c.hasPathOrNull("millis")) c.getDuration("millis", java.util.concurrent.TimeUnit.MILLISECONDS) else 550000
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaDurationCfg = {
    ScalaDurationCfg(
      durations = ScalaDurationCfg.Durations(c.getConfig("durations"))
    )
  }

  private def $_L$_L$_int(cl:com.typesafe.config.ConfigList): scala.List[scala.List[scala.Int]] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_L$_int(cv.asInstanceOf[com.typesafe.config.ConfigList])).toList
  }
  private def $_L$_bln(cl:com.typesafe.config.ConfigList): scala.List[scala.Boolean] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_bln(cv)).toList
  }
  private def $_L$_dbl(cl:com.typesafe.config.ConfigList): scala.List[scala.Double] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_dbl(cv)).toList
  }
  private def $_L$_int(cl:com.typesafe.config.ConfigList): scala.List[scala.Int] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_int(cv)).toList
  }
  private def $_L$_lng(cl:com.typesafe.config.ConfigList): scala.List[scala.Long] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_lng(cv)).toList
  }
  private def $_L$_str(cl:com.typesafe.config.ConfigList): scala.List[java.lang.String] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_str(cv)).toList
  }
  private def $_bln(cv:com.typesafe.config.ConfigValue): scala.Boolean = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.BOOLEAN) ||
      !u.isInstanceOf[java.lang.Boolean]) throw $_expE(cv, "boolean")
    u.asInstanceOf[java.lang.Boolean].booleanValue()
  }
  private def $_dbl(cv:com.typesafe.config.ConfigValue): scala.Double = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) ||
      !u.isInstanceOf[java.lang.Number]) throw $_expE(cv, "double")
    u.asInstanceOf[java.lang.Number].doubleValue()
  }
  private def $_expE(cv:com.typesafe.config.ConfigValue, exp:java.lang.String) = {
    val u: Any = cv.unwrapped
    new java.lang.RuntimeException(cv.origin.lineNumber +
      ": expecting: " + exp + " got: " +
      (if (u.isInstanceOf[java.lang.String]) "\"" + u + "\"" else u))
  }
  private def $_int(cv:com.typesafe.config.ConfigValue): scala.Int = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) ||
      !u.isInstanceOf[Integer]) throw $_expE(cv, "integer")
    u.asInstanceOf[Integer]
  }
  private def $_lng(cv:com.typesafe.config.ConfigValue): scala.Long = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) ||
      !u.isInstanceOf[java.lang.Integer] && !u.isInstanceOf[java.lang.Long]) throw $_expE(cv, "long")
    u.asInstanceOf[java.lang.Number].longValue()
  }
  private def $_str(cv:com.typesafe.config.ConfigValue) =
    java.lang.String.valueOf(cv.unwrapped())
}
