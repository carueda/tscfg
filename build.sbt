name := "tscfg"

val tscfgVersion = setVersion("0.7.1")

version := tscfgVersion

scalaVersion := "2.11.8"

libraryDependencies += "com.typesafe" % "config" % "1.3.0"

libraryDependencies += "org.specs2" %% "specs2-core" % "3.8.4" % "test"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.5.0"

libraryDependencies += "com.google.code.gson" % "gson" % "2.8.0"

mainClass in assembly := Some("tscfg.Main")

assemblyJarName in assembly := s"tscfg-$tscfgVersion.jar"

coverageExcludedPackages := "tscfg.example.*;tscfg.Main;tscfg.GenExamples"

coverageMinimum := 70

coverageFailOnMinimum := false

coverageHighlighting := { scalaBinaryVersion.value == "2.11" }

def setVersion(version: String): String = {
  import java.nio.file.Files._
  import java.nio.file.Paths
  import java.nio.file.StandardOpenOption._
  val appConf = Paths.get("src/main/resources/application.conf")
  val contents = s"tscfg.version = $version\n"
  println(s"setting version $version")
  write(appConf,
    contents.getBytes(java.nio.charset.StandardCharsets.UTF_8),
    CREATE, TRUNCATE_EXISTING, WRITE
  )
  version
}
