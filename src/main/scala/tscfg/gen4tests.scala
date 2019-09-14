package tscfg

import java.io.{File, FileWriter, PrintWriter}

import tscfg.generators.java.JavaGen
import tscfg.generators.{GenOpts, Generator}
import tscfg.generators.scala.ScalaGen

object gen4tests {
  def main(args: Array[String]): Unit = {
    val sourceDir = new File("src/main/tscfg/example")
    sourceDir.listFiles().filter(_.getName.endsWith(".spec.conf")) foreach generate
  }

  private def generate(confFile: File): Unit = {
    //println(s"gen4tests: confFile=$confFile")
    val source = io.Source.fromFile(confFile).mkString.trim

    val baseGenOpts: GenOpts = {
      var genOpts = GenOpts("tscfg.example", "?")
      val opts = {
        val linePat = """\s*//\s*GenOpts:(.*)""".r
        source.split("\n")
          .collect { case linePat(xs) ⇒ xs.trim }
          .flatMap(_.split("\\s+"))
      }

      opts foreach {
        case "--scala:bt"      ⇒ genOpts = genOpts.copy(useBackticks = true)
        case "--java:getters"  ⇒ genOpts = genOpts.copy(genGetters = true)
        case "--java:optionals"  ⇒ genOpts = genOpts.copy(useOptionals = true)
        case "--durations"     ⇒ genOpts = genOpts.copy(useDurations = true)
        case "--all-required"  ⇒ genOpts = genOpts.copy(assumeAllRequired = true)

        // $COVERAGE-OFF$
        case opt ⇒ println(s"WARN: $confFile: unrecognized GenOpts argument: `$opt'")
        // $COVERAGE-ON$
      }
      genOpts
    }

    val buildResult = ModelBuilder(source, assumeAllRequired = baseGenOpts.assumeAllRequired)
    val objectType = buildResult.objectType

    val name = confFile.getName
    val (base, _) = name.span(_ != '.')
    val classNameSuffix = util.upperFirst(base.replace('-', '_')) + "Cfg"

    List("Scala", "Java") foreach { lang ⇒
      val targetScalaDir = new File("src/test/" + lang.toLowerCase + "/tscfg/example")
      targetScalaDir.mkdirs()

      val className = lang + classNameSuffix

      val fileName = className + "." + lang.toLowerCase
      val targetFile = new File(targetScalaDir, fileName)
      // $COVERAGE-OFF$
      if (true||confFile.lastModified >= targetFile.lastModified) {
        val genOpts = baseGenOpts.copy(className = className)
        //println(s"generating for $name -> $fileName")
        val generator: Generator = lang match {
          case "Scala" ⇒ new ScalaGen(genOpts)
          case "Java" ⇒  new JavaGen(genOpts)
        }

        val results = generator.generate(objectType)
        val out = new PrintWriter(new FileWriter(targetFile), true)
        out.println(results.code)
      }
      // $COVERAGE-ON$
    }
  }
}
