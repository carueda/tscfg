package tscfg

import java.io.{File, PrintWriter, Writer}

import com.typesafe.config.ConfigFactory
import tscfg.generators.java.JavaGenerator
import tscfg.generators.scala.ScalaGenerator
import tscfg.generators.{GenOpts, GenResult, Generator}
import tscfg.specs.ObjSpec

/**
  * The main program. Run with no arguments to see usage.
  */
object Main {
  val version: String = ConfigFactory.load().getString("tscfg.version")

  val defaultDestDir: String = "/tmp"

  val usage: String = s"""
             |tscfg $version
             |Usage:  tscfg.Main --spec inputFile [options]
             |Options (default):
             |  --pn <packageName>                                     (${generators.defaults.packageName})
             |  --cn <className>                                       (${generators.defaults.className})
             |  --dd <destDir>                                         ($defaultDestDir)
             |  --j7                  generate code for java <= 7      (8)
             |  --scala               generate scala code              (java)
             |  --java                generate java code               (the default)
             |Output is written to $$destDir/$$className.ext
    """.stripMargin

  case class CmdLineOpts(inputFilename: Option[String] = None,
                         packageName: String = generators.defaults.packageName,
                         className: String =   generators.defaults.className,
                         destDir: String = defaultDestDir,
                         j7: Boolean = false,
                         language: String = "java"
                 )

  def main(args: Array[String]): Unit = {
    generate(getOpts(args.toList))
  }

  private def getOpts(args: List[String]): CmdLineOpts = {
    if (args.isEmpty) {
      println(usage)
      sys.exit(0)
    }
    def traverseList(list: List[String], opts: CmdLineOpts = CmdLineOpts()): CmdLineOpts = {
      def chkVal(v: String): String = {
        if (!v.startsWith("-")) v
        else {
          println( s"""error: argument looks like a switch: $v""")
          sys.exit(0)
        }
      }
      list match {
        case "--spec" :: filename :: rest =>
          traverseList(rest, opts.copy(inputFilename = Some(chkVal(filename))))

        case "--pn" :: packageName :: rest =>
          traverseList(rest, opts.copy(packageName = chkVal(packageName)))

        case "--cn" :: className :: rest =>
          traverseList(rest, opts.copy(className = chkVal(className)))

        case "--dd" :: destDir :: rest =>
          traverseList(rest, opts.copy(destDir = chkVal(destDir)))

        case "--j7" :: rest =>
          traverseList(rest, opts.copy(j7 = true))

        case "--scala" :: rest =>
          traverseList(rest, opts.copy(language = "scala"))

        case "--java" :: rest =>
          traverseList(rest, opts.copy(language = "java"))

        case opt :: nil =>
          println( s"""missing argument or unknown option: $opt""")
          sys.exit(0)

        case nil => opts
      }
    }

    val opts = traverseList(args)

    opts.inputFilename.getOrElse {
      println("--spec not given")
      sys.exit(1)
    }
    opts
  }

  private def generate(opts: CmdLineOpts): Unit = {
    require(opts.inputFilename.isDefined)

    val ext = opts.language

    val inputFilename = opts.inputFilename.get
    val destFilename  = s"${opts.destDir}/${opts.className}.$ext"
    val destFile = new File(destFilename)
    val out = new PrintWriter(destFile)

    implicit val genOpts = GenOpts(opts.packageName, opts.className, opts.j7,
      preamble = Some(s"source: $inputFilename"),
      language = opts.language
    )

    println(s"parsing: $inputFilename")
    val config = ConfigFactory.parseFile(new File(inputFilename)).resolve()

    val className = genOpts.className
    val objSpec = new SpecBuilder(Key(className)).fromConfig(config)
    //println("\nobjSpec:\n  |" + objSpec.format().replaceAll("\n", "\n  |"))

    println(s"generating: $destFile")
    generate(objSpec, out)
    out.close()

/*
    opts.templates foreach { genTemplate =>
      val destFile = new File(genTemplate.filename)
      printf("%10s: %s\n", genTemplate.what, destFile.getAbsolutePath)
      val out = new PrintWriter(destFile)
      templateGenerator.generate(genTemplate.what, root, out)
      out.close()
    }
*/
  }

  /**
    * Generates code for the given configuration spec.
    *
    * @param objSpec      specification
    * @param out          code is written here
    * @param genOpts      generation options
    */
  private def generate(objSpec: ObjSpec, out: Writer)
              (implicit genOpts: GenOpts): GenResult = {

    val pw = out match {
      case w: PrintWriter => w
      case w => new PrintWriter(w)
    }

    val generator: Generator = genOpts.language match {
      case "java"  => new JavaGenerator(genOpts)
      case "scala" => new ScalaGenerator(genOpts)
    }

    val results = generator.generate(objSpec)
    pw.println(results.code)
    results
  }
}
