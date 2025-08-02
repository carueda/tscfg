enablePlugins(BuildInfoPlugin)

organization       := "com.github.carueda"
name               := "tscfg"
scalaVersion       := "3.3.6"
crossScalaVersions := Seq("2.13.16", "3.3.6")

buildInfoKeys    := Seq[BuildInfoKey](version)
buildInfoPackage := "tscfg"

libraryDependencies ++= Seq(
  "com.outr"            %% "scribe"    % "3.17.0",
  "com.typesafe"         % "config"    % "1.4.3",
  "com.lihaoyi"         %% "pprint"    % "0.9.1",
  "org.scalatest"       %% "scalatest" % "3.2.19" % Test,
  "com.google.code.gson" % "gson"      % "2.10.1"
)

scalafmtOnCompile := true

scalacOptions ++= Seq("-deprecation", "-feature")

(assembly / mainClass) := Some("tscfg.Main")

(assembly / assemblyJarName) := s"tscfg-${version.value}.jar"

coverageExcludedPackages := "tscfg.example.*;tscfg.Main"
coverageMinimumStmtTotal := 80
coverageFailOnMinimum    := false
coverageHighlighting     := { scalaBinaryVersion.value == "2.11" }

lazy val codeDefs = taskKey[Unit]("Copies code definitions to resources/")
codeDefs := {
  for (ext <- Seq("java", "scala")) {
    val src = s"src/main/$ext/tscfg/codeDefs/resources/"
    val dst = s"src/main/resources/codeDefs/"
    println(s"Copying $src to $dst")
    IO.copyDirectory(file(src), file(dst))
  }
}
(Compile / compile) := ((Compile / compile) dependsOn codeDefs).value

lazy val genCode = taskKey[Unit]("Generate classes for tests")
fullRunTask(genCode, Compile, "tscfg.gen4tests")
(genCode / fork) := false

(Test / testOnly) := ((Test / testOnly) dependsOn (codeDefs, genCode)).evaluated
(Test / test)     := ((Test / test) dependsOn (codeDefs, genCode)).value

Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oF")

addCommandAlias("fmt", "; scalafmtAll; scalafmtSbt")
addCommandAlias("fmtCheck", "; scalafmtCheckAll; scalafmtSbtCheck")
addCommandAlias("a", "; clean; fmt; test")
addCommandAlias("c", "; compile")

(Test / publishArtifact) := false

pomIncludeRepository := { _ => false }
homepage             := Some(url("https://github.com/carueda/tscfg"))
licenses             := Seq(
  "Apache 2.0" -> url("https://www.opensource.org/licenses/Apache-2.0")
)
scmInfo := Some(
  ScmInfo(
    url("https://github.com/carueda/tscfg"),
    "scm:git@github.com:carueda/tscfg.git"
  )
)
pomExtra :=
  <developers>
    <developer>
      <id>carueda</id>
      <name>Carlos Rueda</name>
      <url>https://carueda.info</url>
    </developer>
  </developers>

useGpgPinentry := true
