package tscfg.generators.java

import com.typesafe.config.ConfigFactory
import org.specs2.mutable.Specification
import tscfg.example.JavaExampleCfg

class JavaExampleSpec extends Specification {

  """JavaExampleCfg with good input""" should {
    val config = ConfigFactory.parseString("""
      |endpoint {
      |  path = "/var/www"
      |  intReq = 12
      |  name = "Calvin"
      |  interface.port = 9191
      |  interface.type = "foo"
      |}
      |""".stripMargin)
    val cfg: JavaExampleCfg = new JavaExampleCfg(config)

    "capture given required values" in {
      cfg.endpoint.path must_== "/var/www"
      cfg.endpoint.intReq must_== 12
    }

    "capture given optional values" in {
      cfg.endpoint.interface_.port must_== 9191
      cfg.endpoint.interface_.`type` must_== "foo"
    }

    "capture default values" in {
      cfg.endpoint.url must_== "http://example.net"
      cfg.endpoint.serial must_== null
    }
  }

  """JavaExampleCfg with input having missing required entries""" should {
    val config = ConfigFactory.parseString("")

    "throw exception in constructor" in {
      def a: Unit = {
        new JavaExampleCfg(config)
      }
      a must throwA[Exception]
    }
  }

  """JavaExampleCfg with null given to a field""" should {
    val config = ConfigFactory.parseString("""
      |endpoint {
      |  path = "/var/www"
      |  intReq = 12
      |  interface.port = null
      |}
      |""".stripMargin)

    "throw exception in constructor" in {
      def a: Unit = {
        new JavaExampleCfg(config)
      }
      a must throwA[Exception]
    }
  }
}
