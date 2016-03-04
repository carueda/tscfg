package tscfg

import tscfg.generator.GenOpts

class JavaAccessorSpec extends BaseAccessorSpec {

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

    """"duration" be long""" in {
      JavaAccessor("duration").`type` must_== "long"
    }
    """"duration | 2" be long""" in {
      JavaAccessor("duration | 5.0").`type` must_== "long"
    }
    """"duration?" be Long""" in {
      JavaAccessor("duration?").`type` must_== "Long"
    }
  }

  """"field access code"""" should {
    """have direct get* call for for type with required value""" in {
      JavaAccessor("string")
        .instance("path") must_== """c.getString("path")"""
      JavaAccessor("int")
        .instance("path") must_== """c.getInt("path")"""
      JavaAccessor("long")
        .instance("path") must_== """c.getLong("path")"""
      JavaAccessor("double")
        .instance("path") must_== """c.getDouble("path")"""
      JavaAccessor("boolean")
        .instance("path") must_== """c.getBoolean("path")"""
      JavaAccessor("duration")
        .instance("path") must_== """c.getDuration("path", java.util.concurrent.TimeUnit.MILLISECONDS)"""
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

      JavaAccessor("long | 1")
        .instance("path") must_== """c.hasPathOrNull("path") ? c.getLong("path") : 1"""
      JavaAccessor("long?")
        .instance("path") must_== """c.hasPathOrNull("path") ? Long.valueOf(c.getLong("path")) : null"""

      JavaAccessor("double | 1")
        .instance("path") must_== """c.hasPathOrNull("path") ? c.getDouble("path") : 1"""
      JavaAccessor("double?")
        .instance("path") must_== """c.hasPathOrNull("path") ? Double.valueOf(c.getDouble("path")) : null"""

      JavaAccessor("boolean | true")
        .instance("path") must_== """c.hasPathOrNull("path") ? c.getBoolean("path") : true"""
      JavaAccessor("boolean?")
        .instance("path") must_== """c.hasPathOrNull("path") ? Boolean.valueOf(c.getBoolean("path")) : null"""

      JavaAccessor("duration | 2")
        .instance("path") must_== """c.hasPathOrNull("path") ? c.getDuration("path", java.util.concurrent.TimeUnit.MILLISECONDS) : 2"""
      JavaAccessor("duration:h | 1d")
        .instance("path") must_== """c.hasPathOrNull("path") ? c.getDuration("path", java.util.concurrent.TimeUnit.HOURS) : 24"""
      JavaAccessor("duration?")
        .instance("path") must_== """c.hasPathOrNull("path") ? c.getDuration("path", java.util.concurrent.TimeUnit.MILLISECONDS) : null"""
    }
  }

  """type inference""" should {
    """map "some string" to String""" in {
      JavaAccessor("some string").`type` must_== "String"
    }
    """map true to boolean""" in {
      JavaAccessor(true).`type` must_== "boolean"
    }
    """map false to boolean""" in {
      JavaAccessor(false).`type` must_== "boolean"
    }
    """map 123 to int""" in {
      JavaAccessor(123).`type` must_== "int"
    }
    s"""map Integer.MAX_VALUE=${Integer.MAX_VALUE} to int""" in {
      JavaAccessor(Integer.MAX_VALUE).`type` must_== "int"
    }
    s"""map Integer.MIN_VALUE=${Integer.MIN_VALUE} to int""" in {
      JavaAccessor(Integer.MIN_VALUE).`type` must_== "int"
    }
    """map 2147483648 to long""" in {
      JavaAccessor(2147483648L).`type` must_== "long"
    }
    s"""map Long.MaxValue=${Long.MaxValue} to long""" in {
      JavaAccessor(Long.MaxValue).`type` must_== "long"
    }
    s"""map Long.MinValue=${Long.MinValue} to long""" in {
      JavaAccessor(Long.MinValue).`type` must_== "long"
    }
    """map 3.14 to double""" in {
      JavaAccessor(3.14).`type` must_== "double"
    }
  }

}
