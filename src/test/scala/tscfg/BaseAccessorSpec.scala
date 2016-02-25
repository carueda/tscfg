package tscfg

import com.typesafe.config.{ConfigValue, ConfigFactory}
import org.specs2.mutable.Specification


abstract class BaseAccessorSpec extends Specification {

  protected implicit def string2configValue(s: String): ConfigValue =
    ConfigFactory.parseString(s"""k = "$s"""").getValue("k")

  protected implicit def anyVal2configValue(r: AnyVal): ConfigValue =
    ConfigFactory.parseString(s"""k = $r""").getValue("k")

  protected implicit def string2type(s: String): Type =
    Type(ConfigFactory.parseString(s"""k = "$s"""").getValue("k"))

  protected implicit def anyVal2type(r: AnyVal): Type =
    Type(ConfigFactory.parseString(s"""k = $r""").getValue("k"))

}
