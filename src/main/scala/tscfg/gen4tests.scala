package tscfg

import tscfg.generators.java.JavaGen
import tscfg.generators.scala.ScalaGen
import tscfg.generators.{GenOpts, Generator}
import tscfg.ns.NamespaceMan

import java.io.{File, FileWriter, PrintWriter}

// $COVERAGE-OFF$
object gen4tests {
  def main(args: Array[String]): Unit = {
    val sourceDir = new File("src/main/tscfg/example")
    sourceDir
      .listFiles()
      .filter(_.getName.endsWith(".spec.conf"))
      .sortBy(_.getName)
      .foreach(dispatch)
  }

  private case class OptFromFile(opt: String, onlyForLang: Option[String])

  private def dispatch(confFile: File): Unit = {
    val bufSource = io.Source.fromFile(confFile)
    val source    = bufSource.mkString
    bufSource.close

    val optsFromFile: List[OptFromFile] = getOptsFromFile(confFile, source)
    if (optsFromFile.exists(_.opt == "--skip-gen4tests")) {
      skip(confFile)
    }
    else {
      val onlyJavaOpts =
        optsFromFile.filter(_.onlyForLang.contains("java")).map(_.opt)
      val onlyScalaOpts =
        optsFromFile.filter(_.onlyForLang.contains("scala")).map(_.opt)
      if (onlyJavaOpts.nonEmpty && onlyScalaOpts.nonEmpty) {
        error(
          s"Both only for java and only for scala options given: java=$onlyJavaOpts; scala=$onlyScalaOpts"
        )
      }
      else {
        val generalOpts = optsFromFile.filter(_.onlyForLang.isEmpty).map(_.opt)
        val (theOpts, onlyForLang) = if (onlyJavaOpts.nonEmpty) {
          (onlyJavaOpts ++ generalOpts, Some("java"))
        }
        else if (onlyScalaOpts.nonEmpty) {
          (onlyScalaOpts ++ generalOpts, Some("scala"))
        }
        else {
          (generalOpts, None)
        }
        generate(confFile, source, theOpts, onlyForLang)
      }
    }
  }

  private def getOptsFromFile(
      confFile: File,
      source: String,
  ): List[OptFromFile] = {
    val linePat = """\s*//\s*GenOpts:(.*)""".r
    source
      .split("\n")
      .collect { case linePat(xs) => xs.trim }
      .flatMap(_.split("\\s+"))
      .map {
        case opt @ "--scala:2.12" =>
          OptFromFile(opt, Some("scala"))

        case opt @ "--scala:bt" =>
          OptFromFile(opt, Some("scala"))

        case opt @ "--java:getters" =>
          OptFromFile(opt, Some("java"))

        case opt @ "--java:records" =>
          OptFromFile(opt, Some("java"))

        case opt @ "--java:optionals" =>
          OptFromFile(opt, Some("java"))

        case opt @ "--durations" =>
          OptFromFile(opt, None)

        case opt @ "--all-required" =>
          OptFromFile(opt, None)

        case opt @ "--skip-gen4tests" =>
          OptFromFile(opt, None)

        case opt =>
          warn(s"$confFile: ignoring unrecognized GenOpts argument: `$opt'")
          OptFromFile(opt, None)
      }
      .toList
  }

  private def generate(
      confFile: File,
      source: String,
      optsFromFile: List[String],
      onlyForLang: Option[String]
  ): Unit = {
    println(s"gen4tests: $confFile")

    val baseGenOpts: GenOpts = {
      var genOpts = GenOpts("tscfg.example", "?")
      optsFromFile foreach {
        case "--scala:2.12" => genOpts = genOpts.copy(s12 = true)

        case "--scala:bt" => genOpts = genOpts.copy(useBackticks = true)

        case "--java:getters" => genOpts = genOpts.copy(genGetters = true)

        case "--java:records" => genOpts = genOpts.copy(genRecords = true)

        case "--java:optionals" => genOpts = genOpts.copy(useOptionals = true)

        case "--durations" => genOpts = genOpts.copy(useDurations = true)

        case "--all-required" =>
          genOpts = genOpts.copy(assumeAllRequired = true)

        case opt =>
          warn(s"$confFile: ignoring unrecognized GenOpts argument: `$opt'")
      }
      genOpts
    }

    val rootNamespace = new NamespaceMan

    val buildResult =
      ModelBuilder(
        rootNamespace,
        source,
        assumeAllRequired = baseGenOpts.assumeAllRequired
      )
    val objectType = buildResult.objectType

    val name            = confFile.getName
    val (base, _)       = name.span(_ != '.')
    val classNameSuffix = util.upperFirst(base.replace('-', '_')) + "Cfg"

    // why is ci failing to find generated files??
//    val langs: List[String] = onlyForLang match {
//      case Some(lang) => List(lang)
//      case None       => List("scala", "java")
//    }
    val langs: List[String] = List("scala", "java")

    langs foreach { lang =>
      val targetScalaDir = new File("src/test/" + lang + "/tscfg/example")
      targetScalaDir.mkdirs()

      val className = lang.capitalize + classNameSuffix

      val fileName   = className + "." + lang
      val targetFile = new File(targetScalaDir, fileName)

      val genOpts = baseGenOpts.copy(className = className)
      val generator: Generator = lang match {
        case "scala" => new ScalaGen(genOpts, rootNamespace)
        case "java"  => new JavaGen(genOpts, rootNamespace)
      }

      val results = generator.generate(objectType)
      val out     = new PrintWriter(new FileWriter(targetFile), true)
      out.println(results.code)
      val onlyLangStr = onlyForLang.map(l => s" (only $l)").getOrElse("")
      println(fansi.Color.Green(s"  ==> $targetFile$onlyLangStr"))
    }
  }

  private def skip(confFile: File): Unit = {
    println(s"gen4tests: ${fansi.Color.Magenta(s"SKIPPING $confFile")}")
  }

  private def warn(s: String): Unit = {
    println(fansi.Color.LightRed(s"WARNING: $s"))
  }

  private def error(s: String): Unit = {
    println(fansi.Color.LightRed(s"ERROR: $s"))
  }
}
// $COVERAGE-ON$
