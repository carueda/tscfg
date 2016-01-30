package tscfg.example

import java.io.File

import com.typesafe.config.{ConfigRenderOptions, ConfigFactory, Config}

/*
 * sbt> runMain tscfg.example.scalaUse example/example.conf
 */
object scalaUse {

  def main(args: Array[String]): Unit = {
    val configFile: File = new File(args(0))

    // usual Typesafe Config mechanism to load the file
    val tsConfig: Config = ConfigFactory.parseFile(configFile).resolve

    // but, instead of:
    val endpoint: Config = tsConfig.getConfig("endpoint")
    val path: String = endpoint.getString("path")
    val url: String = if (endpoint.hasPathOrNull("url")) endpoint.getString("url") else "http://example.net"
    val serial: Option[Int] = if (endpoint.hasPathOrNull("serial")) Some(endpoint.getInt("serial")) else None
    val port: Int = if (endpoint.hasPathOrNull("port")) endpoint.getInt("interface.port") else 8080
    val `type`: Option[String] = if (endpoint.hasPathOrNull("type")) Some(endpoint.getString("type")) else None

    // you can:
    val cfg: ScalaExampleCfg = ScalaExampleCfg(tsConfig)
    val path2: String = cfg.endpoint.path
    val url2: String = cfg.endpoint.url
    val serial2: Option[Int] = cfg.endpoint.serial
    val port2: Int = cfg.endpoint.interface.port
    val type2: Option[String] = cfg.endpoint.interface.`type`

    println("\n*** TsCfg: *** ")
    println(cfg.toString)

    val options: ConfigRenderOptions = ConfigRenderOptions.defaults
      .setFormatted(true).setComments(true).setOriginComments(false)
    println("\n*** tsConfig: ***")
    println(tsConfig.root.render(options))
  }

}
