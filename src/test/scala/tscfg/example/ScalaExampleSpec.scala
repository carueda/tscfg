package tscfg.example

import com.typesafe.config.ConfigFactory
import org.specs2.mutable.Specification

class ScalaExampleSpec extends Specification {

  """ScalaExampleCfg with good input""" should {
    val config = ConfigFactory.parseString("""
      |endpoint {
      |  path = "/var/www"
      |  intReq = 12
      |  interface.port = 9191
      |  interface.type = "foo"
      |}
      |""".stripMargin)
    val cfg = ScalaExampleCfg(config)

    "capture given required values" in {
      cfg.endpoint.path must_== "/var/www"
      cfg.endpoint.intReq must_== 12
    }

    "capture given optional values" in {
      cfg.endpoint.interface.port must_== 9191
      cfg.endpoint.interface.`type` must_== Some("foo")
    }

    "capture default values" in {
      cfg.endpoint.url must_== "http://example.net"
      cfg.endpoint.serial must_== None
    }
  }

  """ScalaExampleCfg with input having missing required entries""" should {
    val config = ConfigFactory.parseString("")

    "throw exception in constructor" in {
      def a: Unit = {
        ScalaExampleCfg(config)
      }
      a must throwA[Exception]
    }
  }

  """ScalaExampleCfg with null given to a field""" should {
    val config = ConfigFactory.parseString("""
      |endpoint {
      |  path = "/var/www"
      |  intReq = 12
      |  interface.port = null
      |}
      |""".stripMargin)

    "throw exception in constructor" in {
      def a: Unit = {
        ScalaExampleCfg(config)
      }
      a must throwA[Exception]
    }
  }
}
