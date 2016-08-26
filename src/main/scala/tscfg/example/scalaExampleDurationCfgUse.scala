package tscfg.example

import com.typesafe.config.{Config, ConfigFactory}

/**
 <pre>
    > runMain tscfg.example.scalaExampleDurationCfgUse
    [info] Running tscfg.example.scalaExampleDurationCfgUse
    Input:
      durations {
        days = "48hour"
        hours = "1d"
      }

    Output:
      Durations(
          days = Some(2)
          hours = 24
          millis = 550000
      )
 </pre>
 */
object scalaExampleDurationCfgUse {
  def main(args: Array[String]): Unit = {
    val input =
        "durations {\n" +
        "  days = \"48hour\"\n" +
        "  hours = \"1d\"\n" +
        "}"

    println("Input:\n  " + input.replace("\n", "\n  "))

    val tsConfig: Config = ConfigFactory.parseString(input).resolve
    val cfg = ScalaExampleDurationCfg(Some(tsConfig))

    val days: Option[Long] = cfg.durations.days
    val hours: Long = cfg.durations.hours
    val millis: Long = cfg.durations.millis

    println("\nOutput:\n  " + cfg.toString.replace("\n", "\n  "))
  }
}
