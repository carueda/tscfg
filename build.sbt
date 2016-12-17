name := "tscfg"

val tscfgVersion = setVersion("0.7.2")

version := tscfgVersion

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe"          %  "config"         % "1.3.0",
  "org.specs2"           %%  "specs2-core"    % "3.8.4" % "test",
  "org.json4s"           %%  "json4s-native"  % "3.5.0",
  "com.google.code.gson"  %  "gson"           % "2.8.0"
)

mainClass in assembly := Some("tscfg.Main")

assemblyJarName in assembly := s"tscfg-$tscfgVersion.jar"

coverageExcludedPackages := "tscfg.example.*;tscfg.Main"

coverageMinimum := 80

coverageFailOnMinimum := false

coverageHighlighting := { scalaBinaryVersion.value == "2.11" }

lazy val genCode = taskKey[Unit]("Generate classes for tests")
fullRunTask(genCode, Compile, "tscfg.gen4tests")
fork in genCode := true

(testOnly in Test) <<= (testOnly in Test) dependsOn genCode
(test in Test)     <<= (test in Test)     dependsOn genCode

def setVersion(version: String): String = {
  println(s"setting version $version")
  IO.write(file("src/main/resources/application.conf"),
           s"tscfg.version = $version\n")
  version
}
