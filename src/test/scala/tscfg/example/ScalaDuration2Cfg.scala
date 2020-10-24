package tscfg.example

final case class ScalaDuration2Cfg(
    durations: ScalaDuration2Cfg.Durations
)
object ScalaDuration2Cfg {
  final case class Durations(
      days: scala.Option[java.time.Duration],
      duration_dy: java.time.Duration,
      duration_hr: java.time.Duration,
      duration_mi: java.time.Duration,
      duration_ms: java.time.Duration,
      duration_ns: java.time.Duration,
      duration_se: java.time.Duration,
      duration_µs: java.time.Duration,
      hours: java.time.Duration,
      millis: java.time.Duration
  )
  object Durations {
    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaDuration2Cfg.Durations = {
      ScalaDuration2Cfg.Durations(
        days = if (c.hasPathOrNull("days")) Some(c.getDuration("days")) else None,
        duration_dy =
          if (c.hasPathOrNull("duration_dy")) c.getDuration("duration_dy")
          else java.time.Duration.parse("PT0S"),
        duration_hr =
          if (c.hasPathOrNull("duration_hr")) c.getDuration("duration_hr")
          else java.time.Duration.parse("PT0S"),
        duration_mi =
          if (c.hasPathOrNull("duration_mi")) c.getDuration("duration_mi")
          else java.time.Duration.parse("PT0S"),
        duration_ms =
          if (c.hasPathOrNull("duration_ms")) c.getDuration("duration_ms")
          else java.time.Duration.parse("PT0S"),
        duration_ns =
          if (c.hasPathOrNull("duration_ns")) c.getDuration("duration_ns")
          else java.time.Duration.parse("PT0S"),
        duration_se =
          if (c.hasPathOrNull("duration_se")) c.getDuration("duration_se")
          else java.time.Duration.parse("PT0S"),
        duration_µs =
          if (c.hasPathOrNull("duration_µs")) c.getDuration("duration_µs")
          else java.time.Duration.parse("PT0S"),
        hours = c.getDuration("hours"),
        millis =
          if (c.hasPathOrNull("millis")) c.getDuration("millis")
          else java.time.Duration.parse("PT9M10S")
      )
    }
  }

  def apply(c: com.typesafe.config.Config): ScalaDuration2Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaDuration2Cfg(
      durations = ScalaDuration2Cfg.Durations(
        if (c.hasPathOrNull("durations")) c.getConfig("durations")
        else com.typesafe.config.ConfigFactory.parseString("durations{}"),
        parentPath + "durations.",
        $tsCfgValidator
      )
    )
    $tsCfgValidator.validate()
    $result
  }
  private final class $TsCfgValidator {
    private val badPaths = scala.collection.mutable.ArrayBuffer[java.lang.String]()

    def addBadPath(path: java.lang.String, e: com.typesafe.config.ConfigException): Unit = {
      badPaths += s"'$path': ${e.getClass.getName}(${e.getMessage})"
    }

    def addInvalidEnumValue(
        path: java.lang.String,
        value: java.lang.String,
        enumName: java.lang.String
    ): Unit = {
      badPaths += s"'$path': invalid value $value for enumeration $enumName"
    }

    def validate(): Unit = {
      if (badPaths.nonEmpty) {
        throw new com.typesafe.config.ConfigException(
          badPaths.mkString("Invalid configuration:\n    ", "\n    ", "")
        ) {}
      }
    }
  }
}
