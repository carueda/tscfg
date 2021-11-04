package tscfg.generators.scala

import com.typesafe.config.ConfigFactory
import org.scalatest.wordspec.AnyWordSpec
import tscfg.example.ScalaExampleCfg

class ScalaExampleSpec extends AnyWordSpec {

  """ScalaExampleCfg""" when {
    val config = ConfigFactory.parseString("""
        |endpoint {
        |  path = "/var/www"
        |  intReq = 12
        |  interface.port = 9191
        |  interface.type = "foo"
        |}
        |""".stripMargin)
    val cfg = ScalaExampleCfg(config)

    "with good input" should {
      "capture given required values" in {
        assert(cfg.endpoint.path === "/var/www")
        assert(cfg.endpoint.intReq === 12)
      }
      "capture given optional values" in {
        assert(cfg.endpoint.interface.port === 9191)
        assert(cfg.endpoint.interface.`type` contains "foo")
      }

      "capture default values" in {
        assert(cfg.endpoint.url === "http://example.net")
        assert(cfg.endpoint.serial.isEmpty)
      }
    }
  }

  """ScalaExampleCfg with input having missing required entries""" should {
    val config = ConfigFactory.parseString("")

    "throw exception in constructor" in {
      assertThrows[Exception] {
        ScalaExampleCfg(config)
      }
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
      assertThrows[Exception] {
        ScalaExampleCfg(config)
      }
    }
  }
}
