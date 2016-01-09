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

  def main(args: Array[String]): Unit = {
    if (args.length == 0) {
      println(
        s"""
          |USAGE: tscfg.Main inputFile [packageName [className [destDir]]]
          |Defaults:
          |  packageName:  $defaultPackageName
          |  className:    $defaultClassName
          |  destDir:      $defaultDestDir
          |Output is written to $$destDir/$$className.java
        """.stripMargin
        )
      return
    }

    val inputFilename = args(0)
    val packageName   = if (args.length > 1) args(1) else defaultPackageName
    val className     = if (args.length > 2) args(2) else defaultClassName
    val destDir       = if (args.length > 3) args(3) else defaultDestDir

    val destFilename  = s"$destDir/$className.java"
    val destFile = new File(destFilename)
    val out = new PrintWriter(destFile)

    println(s"parsing: $inputFilename")
    val config = ConfigFactory.parseFile(new File(inputFilename)).resolve()

    println(s"generating: ${destFile.getAbsolutePath}")

    generator.java(config, packageName, className, out)

    out.close()
  }
}
