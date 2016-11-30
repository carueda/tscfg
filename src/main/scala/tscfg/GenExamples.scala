package tscfg

object GenExamples {

  def main(args: Array[String]): Unit = {
    examples.foreach { example =>

      val classNameSuffix = {
        val noPath = example.spec.substring(example.spec.lastIndexOf('/') + 1)
        val noDef = noPath.replaceAll("""^def\.""", "")
        val symbol = noDef.substring(0, noDef.indexOf('.'))
        util.upperFirst(symbol) + "Cfg"
      }

      List("scala", "java") foreach { lang ⇒
        val genArgs = List(
            "--spec", s"example/${example.spec}",
            "--pn",   "tscfg.example",
            "--cn",   util.upperFirst(lang) + classNameSuffix,
            "--dd",   s"src/main/$lang/tscfg/example/",
            s"--$lang"
        ) ++ example.extraArgs
        Main.main(genArgs.toArray)
        println()
      }
    }
  }

  case class Example(spec: String, extraArgs: String*)

  val examples: List[Example] = List(
     Example("example.spec.conf")
    ,Example("example.spec.conf")
    ,Example("example0.spec.conf")
    ,Example("example0.spec.conf")
    ,Example("example1.spec.conf")
    ,Example("example1.spec.conf")
    ,Example("duration.spec.conf")
    ,Example("duration.spec.conf")
    ,Example("issue5.conf")
    ,Example("issue5.conf")
    ,Example("issue11.conf")
    ,Example("issue11.conf")
    ,Example("issue12.conf")
    ,Example("issue12.conf")
    ,Example("issue13.conf")
    ,Example("issue14.conf")
    ,Example("issue14.conf")
  )
}
