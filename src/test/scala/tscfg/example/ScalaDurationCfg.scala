package tscfg.example

final case class ScalaDurationCfg(
  durations : ScalaDurationCfg.Durations
)
object ScalaDurationCfg {
  
  /** 
    * @param millis
    *   optional duration with default value;
    *   reported long (Long) is in milliseconds, either 550,000 if value is missing
    *   or whatever is provided converted to millis
    * @param days
    *   optional duration; reported Long (Option[Long] in scala) is null (None) if value is missing
    *   or whatever is provided converted to days
    * @param hours
    *   required duration; reported long (Long) is whatever is provided
    *   converted to hours
    */
  final case class Durations(
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
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaDurationCfg.Durations = {
      ScalaDurationCfg.Durations(
        days        = if(c.hasPathOrNull("days")) scala.Some(c.getDuration("days", java.util.concurrent.TimeUnit.DAYS)) else scala.None,
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
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaDurationCfg(
      durations = ScalaDurationCfg.Durations(if(c.hasPathOrNull("durations")) c.getConfig("durations") else com.typesafe.config.ConfigFactory.parseString("durations{}"), parentPath + "durations.", $tsCfgValidator)
    )
    $tsCfgValidator.validate()
    $result
  }
  final class $TsCfgValidator {
    private val badPaths = scala.collection.mutable.ArrayBuffer[java.lang.String]()

    def addBadPath(path: java.lang.String, e: com.typesafe.config.ConfigException): Unit = {
      badPaths += s"'$path': ${e.getClass.getName}(${e.getMessage})"
    }

    def addInvalidEnumValue(path: java.lang.String, value: java.lang.String, enumName: java.lang.String): Unit = {
      badPaths += s"'$path': invalid value $value for enumeration $enumName"
    }

    def validate(): Unit = {
      if (badPaths.nonEmpty) {
        throw new com.typesafe.config.ConfigException(
          badPaths.mkString("Invalid configuration:\n    ", "\n    ", "")
        ){}
      }
    }
  }
}
