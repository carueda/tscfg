package tscfg

object GenExamples {

  def main(args: Array[String]): Unit = {
    examples.foreach { example =>
      val javaOrScala = if (example.className.startsWith("Java")) "java" else "scala"
      val genArgs = List(
          "--spec", s"example/${example.spec}",
          "--pn",   "tscfg.example",
          "--cn",   example.className,
          "--dd",   s"src/main/$javaOrScala/tscfg/example/",
          s"--$javaOrScala"
      ) ++ example.extraArgs
      Main.main(genArgs.toArray)
      println()
    }
  }

  case class Example(spec: String, className: String, extraArgs: String*)

  val examples: List[Example] = List(
     Example("def.example.conf",           "JavaExampleCfg", "--toPropString")
    ,Example("def.example.conf",           "ScalaExampleCfg")
    ,Example("example0.spec.conf",         "JavaExample0Cfg", "--toPropString")
    ,Example("example0.spec.conf",         "ScalaExample0Cfg")
    ,Example("example1.spec.conf",         "JavaExample1Cfg")
    ,Example("example1.spec.conf",         "ScalaExample1Cfg")
    ,Example("example-duration.spec.conf", "JavaExampleDurationCfg" )
    ,Example("example-duration.spec.conf", "ScalaExampleDurationCfg")
    ,Example("issue5.conf",                "JavaIssue5Cfg")
    ,Example("issue5.conf",                "ScalaIssue5Cfg")
    ,Example("issue11.conf",               "JavaIssue11Cfg")
    ,Example("issue11.conf",               "ScalaIssue11Cfg")
    ,Example("issue12.conf",               "JavaIssue12Cfg")
    ,Example("issue12.conf",               "ScalaIssue12Cfg")
    ,Example("issue13.conf",               "ScalaIssue13Cfg")
    ,Example("issue14.conf",               "JavaIssue14Cfg")
    ,Example("issue14.conf",               "ScalaIssue14Cfg")
  )
}
