package tscfg.example

import com.typesafe.config.ConfigFactory

object ScalaIssue15Main {
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
  }
}
