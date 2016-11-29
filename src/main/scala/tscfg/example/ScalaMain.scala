package tscfg.example

import com.typesafe.config.ConfigFactory

object ScalaMain {
  def main(args: Array[String]): Unit = {
    import org.json4s._
    import org.json4s.native.Serialization
    import org.json4s.native.Serialization.writePretty
    implicit val formats = Serialization.formats(NoTypeHints)

    {
      println("\nissue10:")
      val c0 = ScalaIssue10Cfg(ConfigFactory.parseString(
        """
          |main = {
          |  reals = [ { foo: 3.14 } ]
          |}
        """.stripMargin
      ))
      println("c0 = " + writePretty(c0))

      val c1 = ScalaIssue10Cfg(ConfigFactory.parseString(
        """
          |main = {
          |  email = {
          |    server = "foo"
          |    password = "pw"
          |  }
          |}
        """.stripMargin
      ))
      println("c1 = " + writePretty(c1))
    }

    {
      println("\nissue15a:")
      val c = ScalaIssue15aCfg(ConfigFactory.parseString(
        """
          |ii: [1,2 ,3 ]
        """.stripMargin
      ))
      println("c = " + writePretty(c))
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
      println("c = " + writePretty(c))
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
      println("c = " + writePretty(c))
    }

    {
      println("\nissue15d:")
      val c = ScalaIssue15dCfg(ConfigFactory.parseString(
        """
          |baz: [ [ {dd: 1, aa: true}, {dd: 2} ] ]
          |""".stripMargin
      ))
      println("c = " + writePretty(c))
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
      println("c = " + writePretty(c))
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
      println("c = " + writePretty(c))
    }
  }
}
