Ref <https://scalacenter.github.io/scala-3-migration-guide/docs/tooling/scala-3-migrate-plugin.html>

```
sbt:tscfg> migrate-libs tscfg
[info] Starting to migrate libDependencies for tscfg
[info]
[info] X             : Cannot be updated to scala 3
[info] Valid         : Already a valid version for Scala 3
[info] To be updated : Need to be updated to the following version
[info]
[info] "org.scala-lang.modules" %% "scala-collection-compat" % "2.5.0"                -> Valid
[info] "org.scalatest" %% "scalatest" % "3.2.10" % "test"                             -> Valid
[info] "org.scalameta" %% "scalafmt-dynamic" % "3.0.8" cross CrossVersion.for3Use2_13 -> Valid
[info] "com.outr" %% "scribe" % "3.6.3"                                               -> Valid
[info] "org.json4s" %% "json4s-native" % "4.0.3"                                      -> Valid
[info] "com.google.googlejavaformat" % "google-java-format" % "1.7"                   -> Valid : Java libraries are compatible.
[info] "com.google.code.gson" % "gson" % "2.8.9"                                      -> Valid : Java libraries are compatible.
[info] "com.typesafe" % "config" % "1.4.1"                                            -> Valid : Java libraries are compatible.
```

```
sbt:tscfg> migrate-scalacOptions tscfg
[info]
[info] Starting to migrate the scalacOptions in tscfg / [Compile,Test]
[info] Some scalacOptions are set by sbt plugins and don't need to be modified, removed or added.
[info] The sbt plugin should adapt its own scalacOptions for Scala 3
[info]
[info] X       : the option is not available is Scala 3
[info] Renamed : the option has been renamed
[info] Valid   : the option is still valid
[info] Plugin  : the option is related to a plugin, previously handled by migrate-libs
[info]
[info] -deprecation -> Valid
[info] -feature     -> Valid
[info]
[info] In configuration Compile:
[info] -Yrangepos -> X
[info] -Xplugin:/Users/carueda/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scalameta/semanticdb-scalac_2.13.6/4.4.21/semanticdb-scalac_2.13.6-4.4.21.jar -> Plugin
[info] -P:semanticdb:synthetics:on                                                                                                                                       -> Plugin
[info] -P:semanticdb:targetroot:/Users/carueda/github/carueda/tscfg/target/scala-2.13/meta                                                                               -> Plugin
[info]
[info] In configuration Test:
[info] -Yrangepos -> X
[info] -Xplugin:/Users/carueda/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scalameta/semanticdb-scalac_2.13.6/4.4.21/semanticdb-scalac_2.13.6-4.4.21.jar -> Plugin
[info] -P:semanticdb:synthetics:on                                                                                                                                       -> Plugin
[info] -P:semanticdb:targetroot:/Users/carueda/github/carueda/tscfg/target/scala-2.13/test-meta                                                                          -> Plugin
```

---

```
sbt:upsilon> migrate-syntax root
```
==> no changes.


