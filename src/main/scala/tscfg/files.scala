package tscfg

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}
import scala.util.{Try, Using}

object files {
  def readFile(file: File): Try[String] =
    Using(io.Source.fromFile(file))(_.mkString)

  def writeFile(path: Path, content: String): Try[Unit] = Try {
    Files.writeString(path, content, StandardCharsets.UTF_8)
  }
}
