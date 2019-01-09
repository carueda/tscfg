package tscfg

import java.io.{File, PrintWriter}
import java.util.Date

import com.typesafe.config.ConfigFactory
import tscfg.generators.java.JavaGen
import tscfg.generators.scala.ScalaGen
import tscfg.generators.{GenOpts, Generator, TemplateOpts, TemplateGenerator}

/**
  * The main program. Run with no arguments to see usage.
  */
object Main {
  val version: String = ConfigFactory.load().getString("tscfg.version")

  val defaultGenOpts = GenOpts(
    packageName = "tscfg.example",
    className = "ExampleCfg",
    j7 = false
  )

  val defaultDestDir: String = "/tmp"

  var templateOpts = TemplateOpts()

  val usage: String =
    s"""
       |tscfg $version
       |Usage:  tscfg.Main --spec inputFile [options]
       |Options (default):
       |  --pn <packageName>                                     (${defaultGenOpts.packageName})
       |  --cn <className>                                       (${defaultGenOpts.className})
       |  --dd <destDir>                                         ($defaultDestDir)
       |  --j7                  generate code for java <= 7      (8)
       |  --scala               generate scala code              (java)
       |  --scala:bt            use backticks (see #30)          (false)
       |  --scala:fp            report full path (see #36)       (false)
       |  --java                generate java code               (the default)
       |  --java:getters        generate getters (see #31)       (false)
       |  --java:optionals      use optionals                    (false)
       |  --tpl <filename>      generate config template         (no default)
       |  --tpl.ind <string>    template indentation string      ("${templateOpts.indent}")
       |  --tpl.cp <string>     prefix for template comments     ("${templateOpts.commentPrefix}")
       |Output is written to $$destDir/$$className.ext
       |
       |More information at https://github.com/carueda/tscfg
    """.stripMargin

  case class CmdLineOpts(inputFilename: Option[String] = None,
                         packageName: String = defaultGenOpts.packageName,
                         className: String =   defaultGenOpts.className,
                         destDir: String = defaultDestDir,
                         j7: Boolean = false,
                         language: String = "java",
                         reportFullPath: Boolean = false,
                         useBackticks: Boolean = false,
                         genGetters: Boolean = false,
                         useOptionals: Boolean = false,
                         tplFilename: Option[String] = None
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

        case "--scala:fp" :: rest =>
          traverseList(rest, opts.copy(reportFullPath = true))

        case "--scala:bt" :: rest =>
          traverseList(rest, opts.copy(useBackticks = true))

        case "--java" :: rest =>
          traverseList(rest, opts.copy(language = "java"))

        case "--java:getters" :: rest =>
          traverseList(rest, opts.copy(genGetters = true))

        case "--java:optionals" ::rest =>
          traverseList(rest, opts.copy(useOptionals = true))

        case "--tpl" :: filename :: rest =>
          traverseList(rest, opts.copy(tplFilename = Some(filename)))

        case "--tpl.ind" :: indent :: rest =>
          templateOpts = templateOpts.copy(indent = indent)
          traverseList(rest, opts)

        case "--tpl.cp" :: prefixComment :: rest =>
          templateOpts = templateOpts.copy(commentPrefix = prefixComment)
          traverseList(rest, opts)

        case opt :: _ =>
          println( s"""missing argument or unknown option: $opt""")
          sys.exit(0)

        case Nil => opts
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

    val genOpts = GenOpts(opts.packageName, opts.className, opts.j7,
                          reportFullPath = opts.reportFullPath,
                          useBackticks = opts.useBackticks,
                          genGetters = opts.genGetters,
                          useOptionals = opts.useOptionals)

    println(s"parsing: $inputFilename")
    val source = io.Source.fromFile(new File(inputFilename)).mkString.trim

    val buildResult = ModelBuilder(source)
    val objectType = buildResult.objectType

    //println("\nobjectType:\n  |" + objectType.format().replaceAll("\n", "\n  |"))

    if (buildResult.warnings.nonEmpty) {
      println("WARNINGS:")
      buildResult.warnings.foreach(w ⇒ println(s"   line ${w.line}: ${w.source}: ${w.message}"))
    }

    println(s"generating: $destFile")
    val pw = out match {
      case w: PrintWriter => w
      case w => new PrintWriter(w)
    }
    val generator: Generator = opts.language match {
      case "java"  => new JavaGen(genOpts)
      case "scala" => new ScalaGen(genOpts)
    }
    val results = generator.generate(objectType)

    pw.println(
      s"""// generated by tscfg $version on ${new Date()}
         |// source: $inputFilename
         |
         |${results.code}
      """.stripMargin
    )

    out.close()

    opts.tplFilename foreach { filename ⇒
      println(s"generating template $filename")
      val destFile = new File(filename)
      val out = new PrintWriter(destFile)
      val templater = new TemplateGenerator(templateOpts)
      val template = templater.generate(objectType)
      out.println(template)
      out.close()
    }
  }
}
