package tscfg.generators

import tscfg.generator.GenResult
import tscfg.specs.ObjSpec

abstract class Generator {

  def generate(objSpec: ObjSpec): GenResult

}
