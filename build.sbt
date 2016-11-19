name := "tscfg"

val tscfgVersion = setVersion("0.3.4")

version := tscfgVersion

scalaVersion := "2.11.7"

libraryDependencies += "com.typesafe" % "config" % "1.3.0"

libraryDependencies += "org.specs2" %% "specs2-core" % "3.8.4" % "test"

mainClass in assembly := Some("tscfg.Main")

assemblyJarName in assembly := s"tscfg-$tscfgVersion.jar"

coverageExcludedPackages := "tscfg.example.*;tscfg.Main;tscfg.GenExamples"

coverageMinimum := 70

coverageFailOnMinimum := false

coverageHighlighting := { scalaBinaryVersion.value == "2.11" }

// saves version in application.conf (if older than this file), and returns it
def setVersion(version: String): String = {
  import java.nio.file.Files._
  import java.nio.file.Paths
  val thisPath = Paths.get("build.sbt")
  val appConf = Paths.get("src/main/resources/application.conf")
  val writeConf = if (exists(appConf))
    getLastModifiedTime(thisPath).compareTo(getLastModifiedTime(appConf)) > 0
  else {
    createDirectories(appConf.getParent)
    true
  }
  if (writeConf) {
    val contents = s"tscfg.version = $version\n"
    println(s"updating $appConf with contents=$contents")
    import java.nio.file.StandardOpenOption._
    write(appConf,
      contents.getBytes(java.nio.charset.StandardCharsets.UTF_8),
      CREATE, TRUNCATE_EXISTING, WRITE)
  }
  version
}
