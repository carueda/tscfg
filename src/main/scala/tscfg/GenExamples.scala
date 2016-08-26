package tscfg

object GenExamples {
  def main(args: Array[String]): Unit = {
    examples.foreach { case (spec, className) =>
      val javaOrScala = if (className.startsWith("Java")) "java" else "scala"
      Main.main(Array(
          "--spec", s"example/$spec",
          "--pn",   "tscfg.example",
          "--cn",   className,
          "--dd",   s"src/main/$javaOrScala/tscfg/example/",
          s"--$javaOrScala"
      ))
      println()
    }
  }

  val examples: List[(String,String)] = List(
    ("def.example.conf",           "JavaExampleCfg"),
    ("def.example.conf",           "ScalaExampleCfg"),
    ("example0.spec.conf",         "JavaExample0Cfg"),
    ("example0.spec.conf",         "ScalaExample0Cfg"),
    ("example1.spec.conf",         "JavaExample1Cfg"),
    ("example1.spec.conf",         "ScalaExample1Cfg"),
    ("example-duration.spec.conf", "JavaExampleDurationCfg"),
    ("example-duration.spec.conf", "ScalaExampleDurationCfg"),
    ("issue5.conf",                "JavaIssue5Cfg"),
    ("issue5.conf",                "ScalaIssue5Cfg"),
    ("issue11.conf",               "JavaIssue11Cfg"),
    ("issue11.conf",               "ScalaIssue11Cfg")
    ,("issue12.conf",              "JavaIssue12Cfg")
    ,("issue12.conf",              "ScalaIssue12Cfg")
  )
}
