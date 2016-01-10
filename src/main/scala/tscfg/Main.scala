package tscfg

import java.io.{File, PrintWriter}

import com.typesafe.config.ConfigFactory

/**
  * The main program. Run with no arguments to see usage.
  */
object Main {
  val defaultPackageName = "example"
  val defaultClassName   = "ExampleCfg"
  val defaultDestDir     = "/tmp"

  val usage = s"""
             |tscfg ${generator.version}
             |USAGE:
             |  tscfg.Main --spec inputFile [--packageName pn] [--className cn] [--destDir dd]
             |  Defaults:
             |    packageName:  $defaultPackageName
             |    className:    $defaultClassName
             |    destDir:      $defaultDestDir
             |Output is written to $$destDir/$$className.java
    """.stripMargin

  case class Opts(inputFilename: Option[String] = None,
                  packageName: String = defaultPackageName,
                  className: String = defaultClassName,
                  destDir: String = defaultDestDir
                 )

  def main(args: Array[String]): Unit = {
    val opts = getOpts(args.toList)
    generate(opts)
  }

  def getOpts(args: List[String]): Opts = {
    if (args.isEmpty) {
      println(usage)
      sys.exit(0)
    }
    def traverseList(list: List[String], opts: Opts = Opts()): Opts = {
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
        case "--packageName" :: packageName :: rest =>
          traverseList(rest, opts.copy(packageName = chkVal(packageName)))
        case "--className" :: className :: rest =>
          traverseList(rest, opts.copy(className = chkVal(className)))
        case "--destDir" :: destDir :: rest =>
          traverseList(rest, opts.copy(destDir = chkVal(destDir)))
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

  def generate(opts: Opts): Unit = {
    require(opts.inputFilename.isDefined)
    val inputFilename = opts.inputFilename.get
    val destFilename  = s"${opts.destDir}/${opts.className}.java"
    val destFile = new File(destFilename)
    val out = new PrintWriter(destFile)

    println(s"parsing: $inputFilename")
    val config = ConfigFactory.parseFile(new File(inputFilename)).resolve()

    println(s"generating: ${destFile.getAbsolutePath}")
    generator.java(config, opts.packageName, opts.className, out)

    out.close()
  }
}
