package tscfg.generators.java

import com.typesafe.config.ConfigFactory
import org.scalatest.wordspec.AnyWordSpec
import tscfg.example.JavaExampleCfg

class JavaExampleSpec extends AnyWordSpec {

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
      assert(cfg.endpoint.path === "/var/www")
      assert(cfg.endpoint.intReq === 12)
    }

    "capture given optional values" in {
      assert(cfg.endpoint.interface_.port === 9191)
      assert(cfg.endpoint.interface_.`type` === "foo")
    }

    "capture default values" in {
      assert(cfg.endpoint.url === "https://example.net")
      assert(cfg.endpoint.serial === null)
    }
  }

  """JavaExampleCfg with input having missing required entries""" should {
    val config = ConfigFactory.parseString("")

    "throw exception in constructor" in {
      assertThrows[Exception] {
        new JavaExampleCfg(config)
      }
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
      assertThrows[Exception] {
        new JavaExampleCfg(config)
      }
    }
  }
}
