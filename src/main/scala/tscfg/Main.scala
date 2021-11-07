package tscfg

import com.typesafe.config.ConfigFactory
import tscfg.generators.java.JavaGen
import tscfg.generators.scala.ScalaGen
import tscfg.generators.{GenOpts, Generator, TemplateGenerator, TemplateOpts}
import tscfg.ns.NamespaceMan

import java.io.{File, PrintWriter}
import java.util.Date

/** The main program. Run with no arguments to see usage.
  */
object Main {
  val version: String = ConfigFactory.load().getString("tscfg.version")

  val defaultGenOpts: GenOpts = GenOpts(
    packageName = "tscfg.example",
    className = "ExampleCfg"
  )

  val defaultDestDir: String = "/tmp"

  var templateOpts: TemplateOpts = TemplateOpts()

  val usage: String =
    s"""
       |tscfg $version
       |Usage:  tscfg.Main --spec inputFile [options]
       |Options (default):
       |  --pn <packageName>                                     (${defaultGenOpts.packageName})
       |  --cn <className>                                       (${defaultGenOpts.className})
       |  --dd <destDir>                                         ($defaultDestDir)
       |  --java                generate java code               (the default)
       |  --j7                  generate code for java <= 7      (8)
       |  --java:getters        generate getters (see #31)       (false)
       |  --java:records        generate records                 (false)
       |  --java:optionals      use optionals                    (false)
       |  --scala               generate scala code              (java)
       |  --scala:2.12          generate code for scala 2.12     (2.13)
       |  --scala:bt            use backticks (see #30)          (false)
       |  --durations           use java.time.Duration           (false)
       |  --all-required        assume all properties are required (see #47)
       |  --tpl <filename>      generate config template         (no default)
       |  --tpl.ind <string>    template indentation string      ("${templateOpts.indent}")
       |  --tpl.cp <string>     prefix for template comments     ("${templateOpts.commentPrefix}")
       |  --withoutTimestamp    generate header w/out timestamp  (false)
       |Output is written to $$destDir/$$className.ext
       |
       |More information at https://github.com/carueda/tscfg
    """.stripMargin

  case class CmdLineOpts(
      inputFilename: Option[String] = None,
      packageName: String = defaultGenOpts.packageName,
      className: String = defaultGenOpts.className,
      destDir: String = defaultDestDir,
      assumeAllRequired: Boolean = false,
      j7: Boolean = false,
      language: String = "java",
      s12: Boolean = false,
      useBackticks: Boolean = false,
      genGetters: Boolean = false,
      genRecords: Boolean = false,
      useOptionals: Boolean = false,
      useDurations: Boolean = false,
      tplFilename: Option[String] = None,
      generateWithoutTimestamp: Boolean = false
  )

  def main(args: Array[String]): Unit = {
    val argList = args.toList match {
      case "--log" :: rest =>
        tscfg.util.setLogMinLevel(
          // name = Some("tscfg.generators.scala.ScalaGen")
        )
        rest
      case list =>
        list
    }
    generate(getOpts(argList))
  }

  private def getOpts(args: List[String]): CmdLineOpts = {
    if (args.isEmpty) {
      println(usage)
      sys.exit(0)
    }

    def traverseList(
        list: List[String],
        opts: CmdLineOpts = CmdLineOpts()
    ): CmdLineOpts = {
      def chkVal(v: String): String = {
        if (!v.startsWith("-")) v
        else {
          println(s"""error: argument looks like a switch: $v""")
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

        case "--all-required" :: rest =>
          traverseList(rest, opts.copy(assumeAllRequired = true))

        case "--j7" :: rest =>
          traverseList(rest, opts.copy(j7 = true))

        case "--scala" :: rest =>
          traverseList(rest, opts.copy(language = "scala"))

        case "--scala:2.12" :: rest =>
          traverseList(rest, opts.copy(s12 = true))

        case "--scala:bt" :: rest =>
          traverseList(rest, opts.copy(useBackticks = true))

        case "--java" :: rest =>
          traverseList(rest, opts.copy(language = "java"))

        case "--java:getters" :: rest =>
          traverseList(rest, opts.copy(genGetters = true))

        case "--java:records" :: rest =>
          traverseList(rest, opts.copy(genRecords = true))

        case "--java:optionals" :: rest =>
          traverseList(rest, opts.copy(useOptionals = true))

        case "--durations" :: rest =>
          traverseList(rest, opts.copy(useDurations = true))

        case "--tpl" :: filename :: rest =>
          traverseList(rest, opts.copy(tplFilename = Some(filename)))

        case "--tpl.ind" :: indent :: rest =>
          templateOpts = templateOpts.copy(indent = indent)
          traverseList(rest, opts)

        case "--tpl.cp" :: prefixComment :: rest =>
          templateOpts = templateOpts.copy(commentPrefix = prefixComment)
          traverseList(rest, opts)

        case "--scala:fp" :: rest =>
          println("ignoring obsolete option: --scala:fp")
          traverseList(rest, opts)

        case "--withoutTimestamp" :: rest =>
          traverseList(rest, opts.copy(generateWithoutTimestamp = true))

        case opt :: _ =>
          println(s"""missing argument or unknown option: $opt""")
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
    val destFile      = new File(destFilename)
    val out           = new PrintWriter(destFile)

    val genOpts = GenOpts(
      opts.packageName,
      opts.className,
      assumeAllRequired = opts.assumeAllRequired,
      j7 = opts.j7,
      s12 = opts.s12,
      useBackticks = opts.useBackticks,
      genGetters = opts.genGetters,
      genRecords = opts.genRecords,
      useOptionals = opts.useOptionals,
      useDurations = opts.useDurations
    )

    println(s"parsing: $inputFilename")
    val source    = io.Source.fromFile(new File(inputFilename))
    val sourceStr = source.mkString.trim

    source.close()

    val rootNamespace = new NamespaceMan

    val buildResult =
      ModelBuilder(
        rootNamespace,
        sourceStr,
        assumeAllRequired = opts.assumeAllRequired
      )
    val objectType = buildResult.objectType

    // println("\nobjectType:\n  |" + objectType.format().replaceAll("\n", "\n  |"))

    if (buildResult.warnings.nonEmpty) {
      println("WARNINGS:")
      buildResult.warnings.foreach(w =>
        println(s"   line ${w.line}: ${w.source}: ${w.message}")
      )
    }

    println(s"generating: $destFile")
    val generator: Generator = opts.language match {
      case "java"  => new JavaGen(genOpts, rootNamespace)
      case "scala" => new ScalaGen(genOpts, rootNamespace)
    }
    val results = generator.generate(objectType)

    val generateWithTimeStamp = !opts.generateWithoutTimestamp

    out.println(
      s"""// generated by tscfg $version${if (generateWithTimeStamp)
        s" on ${new Date()}"
      else ""}
         |// source: $inputFilename
         |
         |${results.code}""".stripMargin
    )

    out.close()

    opts.tplFilename foreach { filename =>
      println(s"generating template $filename")
      val destFile  = new File(filename)
      val out       = new PrintWriter(destFile)
      val templater = new TemplateGenerator(templateOpts)
      val template  = templater.generate(objectType)
      out.println(template)
      out.close()
    }
  }
}
