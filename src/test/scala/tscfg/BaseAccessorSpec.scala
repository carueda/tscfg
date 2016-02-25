package tscfg

import com.typesafe.config.{ConfigFactory, ConfigValue}
import org.specs2.mutable.Specification
import tscfg.generator.GenOpts


abstract class BaseAccessorSpec extends Specification {

  protected implicit def string2configValue(s: String): Type =
    Type(ConfigFactory.parseString(s"""k = "$s"""").getValue("k"))

  protected implicit def anyVal2configValue(r: AnyVal): Type =
    Type(ConfigFactory.parseString(s"""k = $r""").getValue("k"))

}
