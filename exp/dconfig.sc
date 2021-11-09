import $ivy.`com.lihaoyi::sourcecode:0.1.4`
import $ivy.`com.typesafe:config:1.3.0`
import com.typesafe.config._

import scala.collection.JavaConverters._

/** Some ideas to extend the basic mechanisms from
  * https://github.com/Krever/static-config. A possible further (optional)
  * addition into the mix is https://github.com/fthomas/refined/
  */
trait DConfig {
  def config: Config

  def obj(implicit valName: sourcecode.Name): Config =
    config.getConfig(valName.value)

  def string(implicit valName: sourcecode.Name): String =
    config.getString(valName.value)

  def int(implicit valName: sourcecode.Name): Int =
    config.getInt(valName.value)

  def objList[T](implicit
      valName: sourcecode.Name,
      ctor: Config => T
  ): List[T] = {
    config.getObjectList(valName.value).asScala.toList.map(_.toConfig).map(ctor)
  }

  // just other less useful list variants (to be removed):

  def objList1(implicit valName: sourcecode.Name): List[_ <: Config] = {
    config.getObjectList(valName.value).asScala.toList.map(_.toConfig)
  }

  def objList2(
      extractor: Config => String => java.util.List[_ <: ConfigObject]
  )(implicit valName: sourcecode.Name): List[_ <: Config] = {
    extractor(config)(valName.value).asScala.toList.map(_.toConfig)
  }

  def objList3[T](extractor: Config => String => T)(implicit
      valName: sourcecode.Name
  ): T =
    extractor(config)(valName.value)

  class LocalDConfigNode(implicit objName: sourcecode.Name)
      extends DConfigNode(config)(objName)
}

abstract class DConfigNode(parent: Config)(implicit objName: sourcecode.Name)
    extends DConfig {
  def config: Config = parent.getConfig(objName.value)
}

////////////////////////////////////////////////////////////

// Example:

class MyDConfig(val config: Config) extends DConfig {

  object `my-app` extends LocalDConfigNode {

    object kafka extends LocalDConfigNode {
      val http: Http = obj

      val list0: List[Http] = objList

      val list1: List[Http] = objList1.map(new Http(_))
      val list2: List[Http] = objList2(_.getObjectList).map(new Http(_))
      val list3: List[Http] =
        objList3(_.getObjectList).asScala.toList.map(x => new Http(x.toConfig))
    }

    implicit class Http(val config: Config) extends DConfig {
      val interface: String = string
      val port: Int         = int
    }
  }
}

val cfg = new MyDConfig(com.typesafe.config.ConfigFactory.parseString("""
    |my-app {
    |  kafka {
    |    http {
    |      interface = "0.0.0.0"
    |      port = 8080
    |    }
    |    list0 = [{
    |      interface = "0.0.0.0"
    |      port = 8080
    |    }, {
    |      interface = "0.0.0.1"
    |      port = 8081
    |    }]
    |    list1 = [{
    |      interface = "0.0.0.0"
    |      port = 8080
    |    }]
    |    list2 = [{
    |      interface = "0.0.0.0"
    |      port = 8080
    |    }]
    |    list3 = [{
    |      interface = "0.0.0.0"
    |      port = 8080
    |    }]
    |  }
    |}
  """.stripMargin))

println(cfg.`my-app`.kafka.http.interface)
println(cfg.`my-app`.kafka.http.port)
println(cfg.`my-app`.kafka.list0)
println(cfg.`my-app`.kafka.list0(1).interface)
println(cfg.`my-app`.kafka.list1)
println(cfg.`my-app`.kafka.list2)
println(cfg.`my-app`.kafka.list3)
