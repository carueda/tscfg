lazy val tscfgVersion = setVersion("0.9.994")

organization := "com.github.carueda"
name := "tscfg"
version := tscfgVersion

scalaVersion := "2.13.6"

crossScalaVersions := Seq("2.12.15", "2.13.6")

libraryDependencies ++= Seq(
  "com.outr"               %% "scribe"                  % "3.6.0",
  "com.typesafe"           %  "config"                  % "1.4.1",
  "org.specs2"             %%  "specs2-core"            % "4.13.0" % "test",
  "org.scala-lang.modules" %% "scala-collection-compat" % "2.5.0",
  "org.json4s"             %%  "json4s-native"          % "4.0.3",
  "org.scalameta"          %%  "scalafmt-dynamic"       % "3.0.7",
  "com.google.googlejavaformat" % "google-java-format"  % "1.7", // note: 1.8: "The minimum supported runtime version is now JDK 11"
  "com.google.code.gson"   %  "gson"                    % "2.8.8"
)

scalacOptions ++= Seq("-deprecation", "-feature")

(Test / scalacOptions) ++= Seq("-Yrangepos")  // per specs2-core

(assembly / mainClass) := Some("tscfg.Main")

(assembly / assemblyJarName) := s"tscfg-$tscfgVersion.jar"

coverageExcludedPackages := "tscfg.example.*;tscfg.Main"
coverageMinimumStmtTotal := 80
coverageFailOnMinimum := false
coverageHighlighting := { scalaBinaryVersion.value == "2.11" }

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
(genCode / fork) := true

(Test / testOnly) := ((Test / testOnly) dependsOn (codeDefs, genCode)).evaluated
(Test / test)     := ((Test / test)     dependsOn (codeDefs, genCode)).value

publishMavenStyle := true
(Test / publishArtifact) := false

publishTo := sonatypePublishToBundle.value
/*
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}
*/
pomIncludeRepository := { _ => false }
homepage := Some(url("https://github.com/carueda/tscfg"))
licenses := Seq("Apache 2.0" -> url("http://www.opensource.org/licenses/Apache-2.0"))
scmInfo := Some(ScmInfo(url("http://github.com/carueda/tscfg"), "scm:git@github.com:carueda/tscfg.git"))
pomExtra :=
  <developers>
    <developer>
      <id>carueda</id>
      <name>Carlos Rueda</name>
      <url>http://carueda.info</url>
    </developer>
  </developers>

sonatypeProfileName := "com.github.carueda"

def setVersion(version: String): String = {
  println(s"setting version $version")
  IO.write(file("src/main/resources/application.conf"),
           s"tscfg.version = $version\n")
  version
}
