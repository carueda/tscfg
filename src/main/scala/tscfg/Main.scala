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
             |  --pn packageName  (${generator.defaultPackageName})
             |  --cn className    (${generator.defaultClassName})
             |  --dd destDir      ($defaultDestDir)
             |  --j7 generate code for java <= 7 (8)
             |  --scala generate scala code (java)
             |Output is written to $$destDir/$$className.java
    """.stripMargin

  case class CmdLineOpts(inputFilename: Option[String] = None,
                         packageName: String = generator.defaultPackageName,
                         className: String =   generator.defaultClassName,
                         destDir: String = defaultDestDir,
                         j7: Boolean = false,
                         scala: Boolean = false
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
        case "--scala" :: rest =>
          traverseList(rest, opts.copy(scala = true))
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
    println(s"CmdLineOpts: $opts")

    val ext = if(opts.scala) "scala" else "java"

    val inputFilename = opts.inputFilename.get
    val destFilename  = s"${opts.destDir}/${opts.className}.$ext"
    val destFile = new File(destFilename)
    val out = new PrintWriter(destFile)
    implicit val genOpts = GenOpts(opts.packageName, opts.className, opts.j7,
      preamble = Some(s"source: $inputFilename"),
      genScala = opts.scala)

    println(s"parsing: $inputFilename")
    val config = ConfigFactory.parseFile(new File(inputFilename)).resolve()

    println(s"generating: ${destFile.getAbsolutePath}")
    generator.generate(config, out)

    out.close()
  }
}
