lazy val tscfgVersion = setVersion("0.9.92")

organization := "com.github.carueda"
name := "tscfg"
version := tscfgVersion

scalaVersion := "2.12.2"

crossScalaVersions := Seq("2.12.2")

libraryDependencies ++= Seq(
  "com.typesafe"          %  "config"         % "1.3.3",
  "org.specs2"           %%  "specs2-core"    % "4.0.2" % "test",
  "org.json4s"           %%  "json4s-native"  % "3.5.0",
  "com.google.code.gson"  %  "gson"           % "2.8.0"
)
scalacOptions in Test ++= Seq("-Yrangepos")  // per specs2-core

mainClass in assembly := Some("tscfg.Main")

assemblyJarName in assembly := s"tscfg-$tscfgVersion.jar"

coverageExcludedPackages := "tscfg.example.*;tscfg.Main"
coverageMinimum := 80
coverageFailOnMinimum := false
coverageHighlighting := { scalaBinaryVersion.value == "2.11" }

lazy val codeTemplates = taskKey[Unit]("Copies CodeTemplate sources to resources/")
codeTemplates := {
  println(s"Copying code templates")
  IO.write(file("src/main/resources/codeTemplates/JavaCodeTemplates"),
    IO.read(file("src/main/java/tscfg/codeTemplates/JavaCodeTemplates.java"))
  )
  IO.write(file("src/main/resources/codeTemplates/ScalaCodeTemplates"),
    IO.read(file("src/main/scala/tscfg/codeTemplates/ScalaCodeTemplates.scala"))
  )
}
(compile in Compile) <<= (compile in Compile) dependsOn codeTemplates

lazy val genCode = taskKey[Unit]("Generate classes for tests")
fullRunTask(genCode, Compile, "tscfg.gen4tests")
fork in genCode := true

(testOnly in Test) <<= (testOnly in Test) dependsOn genCode
(test in Test)     <<= (test in Test)     dependsOn genCode

publishMavenStyle := true
publishArtifact in Test := false
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}
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
  </developers>;

sonatypeProfileName := "com.github.carueda"

def setVersion(version: String): String = {
  println(s"setting version $version")
  IO.write(file("src/main/resources/application.conf"),
           s"tscfg.version = $version\n")
  version
}
