name := "tscfg"

val tscfgVersion = "0.1.1"

version := tscfgVersion

scalaVersion := "2.11.7"

libraryDependencies += "com.typesafe" % "config" % "1.2.1"

libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "3.7" % "test")

mainClass in assembly := Some("tscfg.Main")

assemblyJarName in assembly := s"tscfg-$tscfgVersion.jar"
