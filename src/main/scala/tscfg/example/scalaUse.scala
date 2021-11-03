package tscfg.example

import java.io.File

import com.typesafe.config.{ConfigRenderOptions, ConfigFactory}

/*
 * sbt> runMain tscfg.example.scalaUse src/main/tscfg/example/example.conf
 */
object scalaUse {

  def main(args: Array[String]): Unit = {
    val configFilename =
      args.headOption.getOrElse("src/main/tscfg/example/example.conf")
    println(s"Loading $configFilename")
    val configFile = new File(configFilename)

    // usual Typesafe Config mechanism to load the file
    val tsConfig = ConfigFactory.parseFile(configFile).resolve

    // create instance of the tscfg generated main class. This will
    // perform all validations according to required properties and types:
    val cfg = ScalaExampleCfg(tsConfig)

    // access the configuration properties in a type-safe fashion while also
    // enjoying your IDE features for code completion, navigation, etc:
    val path: String        = cfg.endpoint.path
    val url: String         = cfg.endpoint.url
    val serial: Option[Int] = cfg.endpoint.serial
    val port: Int           = cfg.endpoint.interface.port
    val typ: Option[String] = cfg.endpoint.interface.`type`

    println("\n*** tscfg case class structure: *** ")
    println("  " + cfg.toString.replaceAll("\n", "\n  "))
    println("\n  *** in JSON format: *** ")
    println("  " + toJson(cfg).toString.replaceAll("\n", "\n  "))

    println("\n*** Typesafe rendering of input Config object: *** ")
    val options: ConfigRenderOptions = ConfigRenderOptions.defaults
      .setFormatted(true)
      .setComments(true)
      .setOriginComments(false)
    println("  " + tsConfig.root.render(options).replaceAll("\n", "\n  "))
  }

  def toJson(cfg: ScalaExampleCfg): String = {
    import org.json4s._
    import org.json4s.native.Serialization
    import org.json4s.native.Serialization.writePretty
    implicit val formats = Serialization.formats(NoTypeHints)
    writePretty(cfg)
  }
}
