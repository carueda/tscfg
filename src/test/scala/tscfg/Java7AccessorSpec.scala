package tscfg

import tscfg.generator.GenOpts

class Java7AccessorSpec extends BaseAccessorSpec {

  implicit val genOpts = GenOpts(language = "java", j7 = true)

  """"field access code for j7 (ie., Typesafe Config <= 1.2.1)""" should {
      JavaAccessor("string")
    """have hasPath condition for type with default value"""" in {
      JavaAccessor("string | hello world")
        .instance("path") must_== """c.hasPath("path") ? c.getString("path") : "hello world""""
      JavaAccessor("string?")
        .instance("path") must_== """c.hasPath("path") ? c.getString("path") : null"""

      JavaAccessor("int | 1")
        .instance("path") must_== """c.hasPath("path") ? c.getInt("path") : 1"""
      JavaAccessor("int?")
        .instance("path") must_== """c.hasPath("path") ? Integer.valueOf(c.getInt("path")) : null"""

      JavaAccessor("double | 1")
        .instance("path") must_== """c.hasPath("path") ? c.getDouble("path") : 1"""
      JavaAccessor("double?")
        .instance("path") must_== """c.hasPath("path") ? Double.valueOf(c.getDouble("path")) : null"""

      JavaAccessor("boolean | true")
        .instance("path") must_== """c.hasPath("path") ? c.getBoolean("path") : true"""
      JavaAccessor("boolean?")
        .instance("path") must_== """c.hasPath("path") ? Boolean.valueOf(c.getBoolean("path")) : null"""
    }
  }
}
