package tscfg
import Accessor._
import org.specs2.mutable.Specification

class AccessorSpec extends Specification {

  """"type correspondences to java"""" should {
    """"string" (and all variations) be String""" in {
      parseValueSpec("string").javaType must_== "String"
      parseValueSpec("string?").javaType must_== "String"
      parseValueSpec("string|foo").javaType must_== "String"
      parseValueSpec("STRING").javaType must_== "String"
    }
    """"int" be int""" in {
      parseValueSpec("int").javaType must_== "int"
    }
    """"int | 5" be int""" in {
      parseValueSpec("int | 5").javaType must_== "int"
    }
    """"int?" be Integer""" in {
      parseValueSpec("int?").javaType must_== "Integer"
    }
    """"double" be double""" in {
      parseValueSpec("double").javaType must_== "double"
    }
    """"double | 5.0" be int""" in {
      parseValueSpec("double | 5.0").javaType must_== "double"
    }
    """"double?" be Integer""" in {
      parseValueSpec("double?").javaType must_== "Double"
    }
    """"boolean" be boolean""" in {
      parseValueSpec("boolean").javaType must_== "boolean"
    }
    """"boolean | true" be boolean""" in {
      parseValueSpec("boolean | 5.0").javaType must_== "boolean"
    }
    """"boolean?" be Boolean""" in {
      parseValueSpec("boolean?").javaType must_== "Boolean"
    }
  }

  """"field access code"""" should {
    """have direct get* call for for type with required value""" in {
      parseValueSpec("string")
        .instance("path") must_== """c.getString("path")"""
      parseValueSpec("int")
        .instance("path") must_== """c.getInt("path")"""
      parseValueSpec("double")
        .instance("path") must_== """c.getDouble("path")"""
      parseValueSpec("boolean")
        .instance("path") must_== """c.getBoolean("path")"""
    }
    """have hasPath condition for type with default value"""" in {
      parseValueSpec("string | hello world")
        .instance("path") must_== """c.hasPath("path") ? c.getString("path") : "hello world""""
      parseValueSpec("string?")
        .instance("path") must_== """c.hasPath("path") ? c.getString("path") : null"""

      parseValueSpec("int | 1")
        .instance("path") must_== """c.hasPath("path") ? c.getInt("path") : 1"""
      parseValueSpec("int?")
        .instance("path") must_== """c.hasPath("path") ? Integer.valueOf(c.getInt("path")) : null"""

      parseValueSpec("double | 1")
        .instance("path") must_== """c.hasPath("path") ? c.getDouble("path") : 1"""
      parseValueSpec("double?")
        .instance("path") must_== """c.hasPath("path") ? Double.valueOf(c.getDouble("path")) : null"""

      parseValueSpec("boolean | true")
        .instance("path") must_== """c.hasPath("path") ? c.getBoolean("path") : true"""
      parseValueSpec("boolean?")
        .instance("path") must_== """c.hasPath("path") ? Boolean.valueOf(c.getBoolean("path")) : null"""
    }
  }
}
