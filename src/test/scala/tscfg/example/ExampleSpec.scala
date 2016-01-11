package tscfg.example

import com.typesafe.config.ConfigFactory
import org.specs2.mutable.Specification

class ExampleSpec extends Specification {

//  """ExampleCfg with good input""" should {
//    val config = ConfigFactory.parseString("""
//      |endpoint {
//      |  path = "/var/www"
//      |  name = "Calvin"
//      |  interface.port = 9191
//      |}
//      |""".stripMargin)
//    val cfg: ExampleCfg = new ExampleCfg(config)
//
//    "capture given values" in {
//      cfg.endpoint.path must_== "/var/www"
//      cfg.endpoint.url must_== "http://example.net"
//      cfg.endpoint.serial must_== null
//      cfg.endpoint.interface_.port must_== 9191
//    }
//    "capture default values" in {
//      cfg.endpoint.serial must_== null
//    }
//  }
//
//  """ExampleCfg with input having missing required entries""" should {
//    val config = ConfigFactory.parseString("")
//
//    "throw exception in constructor" in {
//      def a: Unit = {
//        new ExampleCfg(config)
//      }
//      a must throwA[Exception]
//    }
//  }
//
//  """ExampleCfg with input having missing required entries""" should {
//    val config = ConfigFactory.parseString("""
//      |endpoint {
//      |  path = "/var/www"
//      |  name = "Calvin"
//      |  interface.port = null
//      |}
//      |""".stripMargin)
//
//    "throw exception in constructor due to null given to primitive" in {
////      def a: Unit = {
//        val cfg = new ExampleCfg(config)
//        cfg.endpoint.interface_.port must_== null
////      }
////      a must throwA[Exception]
//    }
//  }
}
