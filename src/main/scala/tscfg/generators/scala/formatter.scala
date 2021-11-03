package tscfg.generators.scala

import java.io.{OutputStream, PrintStream}
import java.nio.file._

import org.scalafmt.interfaces.Scalafmt

object formatter {
  private val scalaFmtConfig =
    """version = 2.7.5
      |align.preset = more
      |maxColumn = 100
      |""".stripMargin

  // - scalafmt requires a file with the configuration. We just create that as temporary file.
  // - scalafmt writes to stderr when loading the config (something like `parsed config (v2.7.5): ..`).
  //   This is noisy both during development (the line is marked `[error]` by sbt),
  //   and also as regular output for the user. So, we "hide" that output.

  private lazy val devNull = {
    val os = new OutputStream() {
      def write(b: Int): Unit = ()
    }
    new PrintStream(os, true)
  }

  private lazy val config = {
    val configPath = Files.createTempFile("_tscfg_gen_scalafmt", ".conf")
    Files.write(
      configPath,
      scalaFmtConfig.getBytes(java.nio.charset.StandardCharsets.UTF_8)
    )
    configPath
  }

  private lazy val scalaFmt = Scalafmt.create(this.getClass.getClassLoader)

  def format(packageName: String, source: String): String = {
    val originalStdErr = java.lang.System.err
    java.lang.System.setErr(devNull)

    val filename = s"${packageName.replace(".", "/")}/Dummy.scala"
    val res      = scalaFmt.format(config, Paths.get(filename), source).trim

    java.lang.System.setErr(originalStdErr)

    res
  }
}
