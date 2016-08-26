package tscfg

import tscfg.generator.GenOpts

class ScalaAccessorSpec extends BaseAccessorSpec {

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

    """"long" and all non-optional variations be Long""" in {
      ScalaAccessor("long").`type` must_== "Long"
      ScalaAccessor("Long|2").`type` must_== "Long"
      ScalaAccessor("LONG").`type` must_== "Long"
    }
    """"long?" and variations be Option[Long]""" in {
      ScalaAccessor("long?").`type` must_== "Option[Long]"
      ScalaAccessor("Long?").`type` must_== "Option[Long]"
      ScalaAccessor("LONG?").`type` must_== "Option[Long]"
    }

    """"duration" be Long""" in {
      ScalaAccessor("duration").`type` must_== "Long"
    }
    """"duration | 2" be Long""" in {
      ScalaAccessor("duration | 5.0").`type` must_== "Long"
    }
    """"duration?" be Option[Long]""" in {
      ScalaAccessor("duration?").`type` must_== "Option[Long]"
    }
  }

  """"field access code"""" should {
    """have direct get* call for for type with required value""" in {
      ScalaAccessor("string")
        .instance("path") must_== """c.get.getString("path")"""
      ScalaAccessor("int")
        .instance("path") must_== """c.get.getInt("path")"""
      ScalaAccessor("double")
        .instance("path") must_== """c.get.getDouble("path")"""
      ScalaAccessor("boolean")
        .instance("path") must_== """c.get.getBoolean("path")"""
      ScalaAccessor("duration")
        .instance("path") must_== """c.map(c => c.getDuration("path", java.util.concurrent.TimeUnit.MILLISECONDS)).get"""
    }

    """have hasPathOrNull condition for type with default value""" in {
      ScalaAccessor("string | hello world")
        .instance("path") must_== """c.map(c => if(c.hasPathOrNull("path")) c.getString("path") else "hello world").get"""
      ScalaAccessor("string?")
        .instance("path") must_== """c.map(c => if(c.hasPathOrNull("path")) Some(c.getString("path")) else None).get"""

      ScalaAccessor("int | 1")
        .instance("path") must_== """c.map(c => if(c.hasPathOrNull("path")) c.getInt("path") else 1).get"""
      ScalaAccessor("int?")
        .instance("path") must_== """c.map(c => if(c.hasPathOrNull("path")) Some(c.getInt("path")) else None).get"""

      ScalaAccessor("long | 1")
        .instance("path") must_== """c.map(c => if(c.hasPathOrNull("path")) c.getLong("path") else 1).get"""
      ScalaAccessor("long?")
        .instance("path") must_== """c.map(c => if(c.hasPathOrNull("path")) Some(c.getLong("path")) else None).get"""

      ScalaAccessor("double | 1")
        .instance("path") must_== """c.map(c => if(c.hasPathOrNull("path")) c.getDouble("path") else 1).get"""
      ScalaAccessor("double?")
        .instance("path") must_== """c.map(c => if(c.hasPathOrNull("path")) Some(c.getDouble("path")) else None).get"""

      ScalaAccessor("boolean | true")
        .instance("path") must_== """c.map(c => if(c.hasPathOrNull("path")) c.getBoolean("path") else true).get"""
      ScalaAccessor("boolean?")
        .instance("path") must_== """c.map(c => if(c.hasPathOrNull("path")) Some(c.getBoolean("path")) else None).get"""

      ScalaAccessor("duration | 2")
        .instance("path") must_== """c.map(c => if(c.hasPathOrNull("path")) c.getDuration("path", java.util.concurrent.TimeUnit.MILLISECONDS) else 2).get"""
      ScalaAccessor("duration:h | 1d")
        .instance("path") must_== """c.map(c => if(c.hasPathOrNull("path")) c.getDuration("path", java.util.concurrent.TimeUnit.HOURS) else 24).get"""
      ScalaAccessor("duration?")
        .instance("path") must_== """c.map(c => if(c.hasPathOrNull("path")) Some(c.getDuration("path", java.util.concurrent.TimeUnit.MILLISECONDS)) else None).get"""
    }

    """type inference""" should {
      """map "some string" to String""" in {
        ScalaAccessor("some string").`type` must_== "String"
      }
      """map true to Boolean""" in {
        ScalaAccessor(true).`type` must_== "Boolean"
      }
      """map false to Boolean""" in {
        ScalaAccessor(false).`type` must_== "Boolean"
      }
      """map 123 to Int""" in {
        ScalaAccessor(123).`type` must_== "Int"
      }
      s"""map Integer.MAX_VALUE=${Integer.MAX_VALUE} to Int""" in {
        ScalaAccessor(Integer.MAX_VALUE).`type` must_== "Int"
      }
      s"""map Integer.MIN_VALUE=${Integer.MIN_VALUE} to Int""" in {
        ScalaAccessor(Integer.MIN_VALUE).`type` must_== "Int"
      }
      """map 2147483648 to Long""" in {
        ScalaAccessor(2147483648L).`type` must_== "Long"
      }
      s"""map Long.MaxValue=${Long.MaxValue} to Long""" in {
        ScalaAccessor(Long.MaxValue).`type` must_== "Long"
      }
      s"""map Long.MinValue=${Long.MinValue} to Long""" in {
        ScalaAccessor(Long.MinValue).`type` must_== "Long"
      }
      """map 3.14 to Double""" in {
        ScalaAccessor(3.14).`type` must_== "Double"
      }
    }

  }
}
