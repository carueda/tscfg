package tscfg

import org.specs2.mutable.Specification
import tscfg.generator.GenOpts

class ScalaAccessorSpec extends Specification {

  implicit val genOpts = GenOpts(language = "scala")

  """type correspondences to scala""" should {
    """"string" and all non-optional variations be String""" in {
      ScalaAccessor("string").`type` must_== "String"
      ScalaAccessor("string|foo").`type` must_== "String"
      ScalaAccessor("STRING").`type` must_== "String"
    }
    """"string?" be Option[String]""" in {
      ScalaAccessor("string?").`type` must_== "Option[String]"
    }

    """"int" and all non-optional variations be Int""" in {
      ScalaAccessor("int").`type` must_== "Int"
      ScalaAccessor("Int|2").`type` must_== "Int"
      ScalaAccessor("INT").`type` must_== "Int"
    }
    """"int?" and variations be Option[Int]""" in {
      ScalaAccessor("int?").`type` must_== "Option[Int]"
      ScalaAccessor("Int?").`type` must_== "Option[Int]"
      ScalaAccessor("INT?").`type` must_== "Option[Int]"
    }
  }

  """"field access code"""" should {
    """have direct get* call for for type with required value""" in {
      ScalaAccessor("string")
        .instance("path") must_== """c.getString("path")"""
      ScalaAccessor("int")
        .instance("path") must_== """c.getInt("path")"""
      ScalaAccessor("double")
        .instance("path") must_== """c.getDouble("path")"""
      ScalaAccessor("boolean")
        .instance("path") must_== """c.getBoolean("path")"""
    }

    """have hasPathOrNull condition for type with default value""" in {
      ScalaAccessor("string | hello world")
        .instance("path") must_== """if(c.hasPathOrNull("path")) c.getString("path") else "hello world""""
      ScalaAccessor("string?")
        .instance("path") must_== """if(c.hasPathOrNull("path")) Some(c.getString("path")) else None"""

      ScalaAccessor("int | 1")
        .instance("path") must_== """if(c.hasPathOrNull("path")) c.getInt("path") else 1"""
      ScalaAccessor("int?")
        .instance("path") must_== """if(c.hasPathOrNull("path")) Some(c.getInt("path")) else None"""

      ScalaAccessor("double | 1")
        .instance("path") must_== """if(c.hasPathOrNull("path")) c.getDouble("path") else 1"""
      ScalaAccessor("double?")
        .instance("path") must_== """if(c.hasPathOrNull("path")) Some(c.getDouble("path")) else None"""

      ScalaAccessor("boolean | true")
        .instance("path") must_== """if(c.hasPathOrNull("path")) c.getBoolean("path") else true"""
      ScalaAccessor("boolean?")
        .instance("path") must_== """if(c.hasPathOrNull("path")) Some(c.getBoolean("path")) else None"""
    }
  }
}
