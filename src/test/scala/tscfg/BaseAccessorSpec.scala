package tscfg

import com.typesafe.config.{ConfigFactory, ConfigValue}
import org.specs2.mutable.Specification


abstract class BaseAccessorSpec extends Specification {

  protected implicit def string2configValue(s: String): ConfigValue =
    ConfigFactory.parseString(s"""k = "$s"""").getValue("k")

  protected implicit def anyVal2configValue(r: AnyVal): ConfigValue =
    ConfigFactory.parseString(s"""k = $r""").getValue("k")

}
