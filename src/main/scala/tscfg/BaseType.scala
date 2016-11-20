package tscfg

abstract sealed class BaseType {
  val base: String
  val qualification: Option[String] = None
}

case class UnqualifiedBaseType(base: String) extends BaseType

case class QualifiedBaseType(base: String, q: String) extends BaseType {
  override val qualification: Option[String] = Some(q)
}

object BaseType {
  def apply(base: String, qualification: String) = {
    QualifiedBaseType(base,
      if (base == "duration")
        canonicalDurationUnit(qualification)
      else qualification
    )
  }

  def apply(base: String) = UnqualifiedBaseType(base)

  // https://github.com/typesafehub/config/blob/master/HOCON.md#duration-format
  private def canonicalDurationUnit(s: String): String = s match {
    case "ns" | "nano" | "nanos" | "nanosecond" | "nanoseconds"     => "nanosecond"
    case "us" | "micro" | "micros" | "microsecond" | "microseconds" => "microsecond"
    case "ms" | "milli" | "millis" | "millisecond" | "milliseconds" => "millisecond"
    case "s" | "second" | "seconds" => "second"
    case "m" | "minute" | "minutes" => "minute"
    case "h" | "hour" | "hours"     => "hour"
    case "d" | "day" | "days"       => "day"
    case _ => throw new AssertionError()
  }
}
