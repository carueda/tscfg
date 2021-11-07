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
      .filter { _.getName.endsWith(".spec.conf") }
      .foreach(dispatch)
  }

  private def dispatch(confFile: File): Unit = {
    val bufSource = io.Source.fromFile(confFile)
    val source    = bufSource.mkString
    bufSource.close

    val optsFromFile: List[String] = {
      val linePat = """\s*//\s*GenOpts:(.*)""".r
      source
        .split("\n")
        .collect { case linePat(xs) => xs.trim }
        .flatMap(_.split("\\s+"))
        .toList
    }
    if (optsFromFile.contains("--skip-gen4tests")) {
      println(s"gen4tests: ${fansi.Color.Magenta(s"SKIPPING $confFile")}")
    }
    else {
      generate(confFile, source, optsFromFile)
    }
  }

  private def generate(
      confFile: File,
      source: String,
      optsFromFile: List[String]
  ): Unit = {
    println(s"gen4tests: $confFile")

    val baseGenOpts: GenOpts = {
      var genOpts = GenOpts("tscfg.example", "?")
      optsFromFile foreach {
        case "--scala:2.12"     => genOpts = genOpts.copy(s12 = true)
        case "--scala:bt"       => genOpts = genOpts.copy(useBackticks = true)
        case "--java:getters"   => genOpts = genOpts.copy(genGetters = true)
        case "--java:records"   => genOpts = genOpts.copy(genRecords = true)
        case "--java:optionals" => genOpts = genOpts.copy(useOptionals = true)
        case "--durations"      => genOpts = genOpts.copy(useDurations = true)
        case "--all-required" =>
          genOpts = genOpts.copy(assumeAllRequired = true)

        case opt =>
          println(s"WARN: $confFile: unrecognized GenOpts argument: `$opt'")
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

    List("Scala", "Java") foreach { lang =>
      val targetScalaDir =
        new File("src/test/" + lang.toLowerCase + "/tscfg/example")
      targetScalaDir.mkdirs()

      val className = lang + classNameSuffix

      val fileName   = className + "." + lang.toLowerCase
      val targetFile = new File(targetScalaDir, fileName)
      if (true || confFile.lastModified >= targetFile.lastModified) {
        val genOpts = baseGenOpts.copy(className = className)
        // println(s"generating for $name -> $fileName")
        val generator: Generator = lang match {
          case "Scala" => new ScalaGen(genOpts, rootNamespace)
          case "Java"  => new JavaGen(genOpts, rootNamespace)
        }

        val results = generator.generate(objectType)
        val out     = new PrintWriter(new FileWriter(targetFile), true)
        out.println(results.code)
      }
    }
  }
}
// $COVERAGE-ON$
