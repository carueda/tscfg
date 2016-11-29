package tscfg.example

import com.typesafe.config.ConfigFactory

object ScalaMain {
  def main(args: Array[String]): Unit = {
    {
      println("\nissue15a:")
      val c = ScalaIssue15aCfg(ConfigFactory.parseString(
        """
          |ii: [1,2 ,3 ]
        """.stripMargin
      ))
      println("c.ii  = " + c.ii)
    }

    {
      println("\nissue15b:")
      val c = ScalaIssue15bCfg(ConfigFactory.parseString(
        """
          |strings:  [hello, world, true]
          |integers: [[1, 2, 3], [4, 5]]
          |doubles:  [3.14, 2.7182, 1.618]
          |longs:    [1, 9999999999]
          |booleans: [true, false]
          |""".stripMargin
      ))
      println("c.strings    = " + c.strings)
      println("c.integers   = " + c.integers)
      println("c.doubles    = " + c.doubles)
      println("c.longs      = " + c.longs)
      println("c.booleans   = " + c.booleans)
    }

    {
      println("\nissue15c:")
      val c = ScalaIssue15cCfg(ConfigFactory.parseString(
        """
          |positions: [
          |  { lat: 1, lon: 2, attrs = [ [ {foo: 99}             ] ] },
          |  { lat: 3, lon: 4, attrs = [ [ {foo: 3.14}, {foo: 0} ] ] }
          |]
          |qaz = {
          |  aa = { bb = [ { cc: hoho } ]  }
          |}
          |""".stripMargin
      ))
      println("c.positions  = " + c.positions)
      println("c.qaz        = " + c.qaz)
    }

    {
      println("\nissue15d:")
      val c = ScalaIssue15dCfg(ConfigFactory.parseString(
        """
          |baz: [ [ {dd: 1, aa: true}, {dd: 2} ] ]
          |""".stripMargin
      ))
      println("c.baz    = " + c.baz)
    }

    {
      println("\nissue15:")
      val c = ScalaIssue15Cfg(ConfigFactory.parseString(
        """
          |positions: [
          |  {
          |    numbers: [ 1, 2, 3 ]
          |    positions: [ [ { other: 33, stuff: baz } ] ]
          |  }
          |]
          |""".stripMargin
      ))
      println("c.positions.head.numbers   = " + c.positions.head.numbers)
      println("c.positions.head.positions = " + c.positions.head.positions)
    }

    {
      println("\nduration:")
      val c = ScalaDurationCfg(ConfigFactory.parseString(
        """
          |durations {
          |  days  = "10d"
          |  hours = "24h"
          |}
          |""".stripMargin
      ))
      println("c.durations.days   = " + c.durations.days)
      println("c.durations.hours  = " + c.durations.hours)
      println("c.durations.millis = " + c.durations.millis)
    }
  }
}
