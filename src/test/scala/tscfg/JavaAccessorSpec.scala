package tscfg
import org.specs2.mutable.Specification
import tscfg.generator.GenOpts

class JavaAccessorSpec extends Specification {

  implicit val genOpts = GenOpts(language = "java")

  """type correspondences to java""" should {
    """"string" (and all variations) be String""" in {
      JavaAccessor("string").`type` must_== "String"
      JavaAccessor("string?").`type` must_== "String"
      JavaAccessor("string|foo").`type` must_== "String"
      JavaAccessor("STRING").`type` must_== "String"
    }
    """"int" be int""" in {
      JavaAccessor("int").`type` must_== "int"
    }
    """"int | 5" be int""" in {
      JavaAccessor("int | 5").`type` must_== "int"
    }
    """"int?" be Integer""" in {
      JavaAccessor("int?").`type` must_== "Integer"
    }
    """"double" be double""" in {
      JavaAccessor("double").`type` must_== "double"
    }
    """"double | 5.0" be int""" in {
      JavaAccessor("double | 5.0").`type` must_== "double"
    }
    """"double?" be Double""" in {
      JavaAccessor("double?").`type` must_== "Double"
    }
    """"boolean" be boolean""" in {
      JavaAccessor("boolean").`type` must_== "boolean"
    }
    """"boolean | true" be boolean""" in {
      JavaAccessor("boolean | 5.0").`type` must_== "boolean"
    }
    """"boolean?" be Boolean""" in {
      JavaAccessor("boolean?").`type` must_== "Boolean"
    }
  }

  """"field access code"""" should {
    """have direct get* call for for type with required value""" in {
      JavaAccessor("string")
        .instance("path") must_== """c.getString("path")"""
      JavaAccessor("int")
        .instance("path") must_== """c.getInt("path")"""
      JavaAccessor("double")
        .instance("path") must_== """c.getDouble("path")"""
      JavaAccessor("boolean")
        .instance("path") must_== """c.getBoolean("path")"""
    }
    """have hasPathOrNull condition for type with default value"""" in {
      JavaAccessor("string | hello world")
        .instance("path") must_== """c.hasPathOrNull("path") ? c.getString("path") : "hello world""""
      JavaAccessor("string?")
        .instance("path") must_== """c.hasPathOrNull("path") ? c.getString("path") : null"""

      JavaAccessor("int | 1")
        .instance("path") must_== """c.hasPathOrNull("path") ? c.getInt("path") : 1"""
      JavaAccessor("int?")
        .instance("path") must_== """c.hasPathOrNull("path") ? Integer.valueOf(c.getInt("path")) : null"""

      JavaAccessor("double | 1")
        .instance("path") must_== """c.hasPathOrNull("path") ? c.getDouble("path") : 1"""
      JavaAccessor("double?")
        .instance("path") must_== """c.hasPathOrNull("path") ? Double.valueOf(c.getDouble("path")) : null"""

      JavaAccessor("boolean | true")
        .instance("path") must_== """c.hasPathOrNull("path") ? c.getBoolean("path") : true"""
      JavaAccessor("boolean?")
        .instance("path") must_== """c.hasPathOrNull("path") ? Boolean.valueOf(c.getBoolean("path")) : null"""
    }
  }
}
