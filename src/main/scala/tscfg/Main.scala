package tscfg

import java.io.{File, PrintWriter}

import com.typesafe.config.ConfigFactory
import tscfg.generator.GenOpts

/**
  * The main program. Run with no arguments to see usage.
  */
object Main {
  val defaultDestDir     = "/tmp"

  val usage = s"""
             |tscfg ${generator.version}
             |Usage:  tscfg.Main --spec inputFile [options]
             |Options (default):
             |  --pn <packageName>                                     (${generator.defaultPackageName})
             |  --cn <className>                                       (${generator.defaultClassName})
             |  --dd <destDir>                                         ($defaultDestDir)
             |  --j7                  generate code for java <= 7      (8)
             |  --scala               generate scala code              (java)
             |  --java                generate java code               (the default)
             |  --noToString          do not generate toString method  (generated)
             |  --toPropString        generate toPropString method     (not generated)
             |  --tpl <type> <filename>  generate configuration template;  <type> = base, local, all
             |Output is written to $$destDir/$$className.ext
    """.stripMargin

  case class GenTemplate(what: templateGenerator.What, filename: String)

  case class CmdLineOpts(inputFilename: Option[String] = None,
                         packageName: String = generator.defaultPackageName,
                         className: String =   generator.defaultClassName,
                         destDir: String = defaultDestDir,
                         j7: Boolean = false,
                         language: String = "java",
                         templates: List[GenTemplate] = List(),
                         genToString: Boolean = true,
                         genToPropString: Boolean = false
                 )

  def main(args: Array[String]): Unit = {
    generate(getOpts(args.toList))
  }

  def getOpts(args: List[String]): CmdLineOpts = {
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
        case "--noToString" :: rest =>
          traverseList(rest, opts.copy(genToString = false))
        case "--toPropString" :: rest =>
          traverseList(rest, opts.copy(genToPropString = true))
        case "--scala" :: rest =>
          traverseList(rest, opts.copy(language = "scala"))
        case "--java" :: rest =>
          traverseList(rest, opts.copy(language = "java"))
        case "--tpl" :: "base" :: filename :: rest =>
          traverseList(rest, opts.copy(templates = GenTemplate(templateGenerator.genBase, filename) :: opts.templates))
        case "--tpl" :: "local" :: filename :: rest =>
          traverseList(rest, opts.copy(templates = GenTemplate(templateGenerator.genLocal, filename) :: opts.templates))
        case "--tpl" :: "all" :: filename :: rest =>
          traverseList(rest, opts.copy(templates = GenTemplate(templateGenerator.genAll, filename) :: opts.templates))
        case "--tpl" :: rest =>
          println( s"""invalid or missing --tpl arguments""")
          sys.exit(0)
        case opt :: nil =>
          println( s"""missing argument or unknown option: $opt""")
          sys.exit(0)
        case nil => opts
      }
    }

    val opts = traverseList(args.toList)

    opts.inputFilename.getOrElse {
      println("--spec not given")
      sys.exit(1)
    }
    opts
  }

  def generate(opts: CmdLineOpts): Unit = {
    require(opts.inputFilename.isDefined)

    val ext = opts.language

    val inputFilename = opts.inputFilename.get
    val destFilename  = s"${opts.destDir}/${opts.className}.$ext"
    val destFile = new File(destFilename)
    val out = new PrintWriter(destFile)
    implicit val genOpts = GenOpts(opts.packageName, opts.className, opts.j7,
      preamble = Some(s"source: $inputFilename"),
      language = opts.language,
      genToString = opts.genToString,
      genToPropString = opts.genToPropString)

    println(s"parsing: $inputFilename")
    val config = ConfigFactory.parseFile(new File(inputFilename)).resolve()
    val root = nodes.createAllNodes(config)

    println(s"generating: $destFile")
    generator.generate(root, out)
    out.close()

    opts.templates foreach { genTemplate =>
      val destFile = new File(genTemplate.filename)
      printf("%10s: %s\n", genTemplate.what, destFile.getAbsolutePath)
      val out = new PrintWriter(destFile)
      templateGenerator.generate(genTemplate.what, root, out)
      out.close()
    }
  }
}
