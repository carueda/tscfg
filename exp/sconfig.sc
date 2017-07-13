import $ivy.`com.lihaoyi::sourcecode:0.1.4`
import $ivy.`com.typesafe:config:1.3.0`
import com.typesafe.config._

/**
  * From https://github.com/Krever/static-config
  */
trait SConfig {
  def config: Config

  def configEntry[T](extractor: Config ⇒ String ⇒ T)(implicit valName: sourcecode.Name): T =
    extractor(config)(valName.value)

  class LocalSConfigNode(implicit objName: sourcecode.Name) extends DConfigNode(config)(objName)
}

abstract class DConfigNode(parent: Config)(implicit objName: sourcecode.Name) extends SConfig {
  def config: Config = parent.getConfig(objName.value)
}

////////////////////////////////////////////////////////////

// Example:

class MySConfig(val config: Config) extends SConfig {

  object `my-app` extends LocalSConfigNode {
    val `app-name`: String = configEntry(_.getString)

    object kafka extends LocalSConfigNode {
      val brokers: java.util.List[String] = configEntry(_.getStringList)
    }

    object http extends LocalSConfigNode {
      val interface: String = configEntry(_.getString)
      val port:      Int    = configEntry(_.getInt)
    }
  }
}

val cfg = new MySConfig(com.typesafe.config.ConfigFactory.parseString(
  """
    |my-app {
    |  app-name = "My Fancy App"
    |  kafka {
    |    brokers = ["broker1", "broker2"]
    |  }
    |  http {
    |    interface = "0.0.0.0"
    |    port = 8080
    |  }
    |}
  """.stripMargin))

println(cfg.`my-app`.kafka.brokers)
