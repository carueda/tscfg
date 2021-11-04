lazy val tscfgVersion = setVersion("0.9.994")

organization       := "com.github.carueda"
name               := "tscfg"
version            := tscfgVersion
scalaVersion       := "3.1.0"
crossScalaVersions := Seq("2.13.7", "3.1.0")

libraryDependencies ++= Seq(
  "com.outr"            %% "scribe"        % "3.6.3",
  "com.typesafe"         % "config"        % "1.4.1",
  "com.lihaoyi"         %% "pprint"        % "0.6.6",
  "org.scalatest"       %% "scalatest"     % "3.2.10" % Test,
  "com.google.code.gson" % "gson"          % "2.8.9"
)

scalacOptions ++= Seq("-deprecation", "-feature")

(assembly / mainClass) := Some("tscfg.Main")

(assembly / assemblyJarName) := s"tscfg-$tscfgVersion.jar"

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
(genCode / fork) := true

(Test / testOnly) := ((Test / testOnly) dependsOn (codeDefs, genCode)).evaluated
(Test / test)     := ((Test / test) dependsOn (codeDefs, genCode)).value

publishMavenStyle        := true
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
homepage             := Some(url("https://github.com/carueda/tscfg"))
licenses := Seq(
  "Apache 2.0" -> url("http://www.opensource.org/licenses/Apache-2.0")
)
scmInfo := Some(
  ScmInfo(
    url("http://github.com/carueda/tscfg"),
    "scm:git@github.com:carueda/tscfg.git"
  )
)
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
  IO.write(
    file("src/main/resources/application.conf"),
    s"tscfg.version = $version\n"
  )
  version
}
