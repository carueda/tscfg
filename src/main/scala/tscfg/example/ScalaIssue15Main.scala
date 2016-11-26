package tscfg.example

import com.typesafe.config.ConfigFactory

object ScalaIssue15Main {
  def main(args: Array[String]): Unit = {
    {
      println("\nissue15a:")
      val c = new JavaIssue15aCfg(ConfigFactory.parseString(
        """
          |ii: [1,2 ,3 ]
        """.stripMargin
      ))
      println("c.ii  = " + c.ii)
    }
  }
}
