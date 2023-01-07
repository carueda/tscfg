enablePlugins(BuildInfoPlugin)

organization       := "com.github.carueda"
name               := "tscfg"
version            := "1.0.2"
scalaVersion       := "3.2.0"
crossScalaVersions := Seq("2.13.9", "3.2.0")

buildInfoKeys    := Seq[BuildInfoKey](version)
buildInfoPackage := "tscfg"

libraryDependencies ++= Seq(
  "com.outr"            %% "scribe"    % "3.10.4",
  "com.typesafe"         % "config"    % "1.4.2",
  "com.lihaoyi"         %% "pprint"    % "0.8.0",
  "org.scalatest"       %% "scalatest" % "3.2.14" % Test,
  "com.google.code.gson" % "gson"      % "2.10.1"
)

scalafmtOnCompile := true

// workaround for problem compiling records, in our case under scala3.
// (https://github.com/scala/bug/issues/11908 - issue was closed on Jul 7,
// but apparently a workaround like this is still needed.)
compileOrder := CompileOrder.JavaThenScala

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
// well, this continues to be unreliable as a way to guarantee genCode is completed
// before testing. So, for now, we will need to explicitly call `genCode;test`.

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

sonatypeProfileName := "com.github.carueda"
