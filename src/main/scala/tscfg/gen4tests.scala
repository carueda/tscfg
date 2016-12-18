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
    val source = io.Source.fromFile(confFile).mkString.trim
    val buildResult = ModelBuilder(source)
    val objectType = buildResult.objectType

    val name = confFile.getName
    val (base, _) = name.span(_ != '.')
    val classNameSuffix = util.upperFirst(base.replace('-', '_')) + "Cfg"

    List("Scala", "Java") foreach { lang ⇒
      val targetScalaDir = new File("src/test/" + lang.toLowerCase + "/tscfg/example")
      targetScalaDir.mkdirs()

      val className = lang + classNameSuffix

      val genOpts = GenOpts("tscfg.example", className, j7 = false)

      val fileName = className + "." + lang.toLowerCase
      val targetFile = new File(targetScalaDir, fileName)
      if (confFile.lastModified >= targetFile.lastModified) {
        println(s"generating for $name -> $fileName")
        val generator: Generator = lang match {
          case "Scala" ⇒ new ScalaGen(genOpts)
          case "Java" ⇒  new JavaGen(genOpts)
        }

        val results = generator.generate(objectType)
        val out = new PrintWriter(new FileWriter(targetFile), true)
        out.println(results.code)
      }
    }
  }
}
