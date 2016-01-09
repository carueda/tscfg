name := "tscfg"

val tscfgVersion = "0.0.1"

version := tscfgVersion

scalaVersion := "2.11.7"

libraryDependencies += "com.typesafe" % "config" % "1.2.1"

mainClass in assembly := Some("tscfg.Main")

assemblyJarName in assembly := s"tscfg-$tscfgVersion.jar"
