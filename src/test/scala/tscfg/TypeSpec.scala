package tscfg

import tscfg.generator.GenOpts

class TypeSpec extends BaseAccessorSpec {

  implicit val genOpts = GenOpts()

  val string = Type("string")
  s""""${string.spec}"""" should {
    "have base string" in {
      string.base must_== "string"
    }
    "be required" in {
      string.required must_== true
    }
    "have value None" in {
      string.value must_== None
    }
  }

  val stringOpt = Type("string?")
  s""""${stringOpt.spec}"""" should {
    "have base string" in {
      stringOpt.base must_== "string"
    }
    "not be required" in {
      stringOpt.required must_== false
    }
    """have value None""" in {
      stringOpt.value must_== None
    }
  }

  val stringOptVal = Type("string | hello-world")
  s""""${stringOptVal.spec}"""" should {
    "have base string" in {
      stringOptVal.base must_== "string"
    }
    "not be required" in {
      stringOptVal.required must_== false
    }
    """have value Some("hello-word")""" in {
      stringOptVal.value must_== Some(""""hello-world"""")
    }
  }

  val stringInferred = Type("hello-world")
  s""""${stringInferred.spec}"""" should {
    "have base string" in {
      stringInferred.base must_== "string"
    }
    "not be required" in {
      stringInferred.required must_== false
    }
    """have value Some("hello-word")""" in {
      stringInferred.value must_== Some(""""hello-world"""")
    }
  }

  val integer = Type("int")
  s""""${integer.spec}"""" should {
    "have base int" in {
      integer.base must_== "int"
    }
    "be required" in {
      integer.required must_== true
    }
    "have value None" in {
      integer.value must_== None
    }
  }

  val integerOpt = Type("int?")
  s""""${integerOpt.spec}"""" should {
    "have base int" in {
      integerOpt.base must_== "int"
    }
    "not be required" in {
      integerOpt.required must_== false
    }
    """have value None""" in {
      integerOpt.value must_== None
    }
  }

  val integerOptVal = Type("int | 2")
  s""""${integerOptVal.spec}"""" should {
    "have base int" in {
      integerOptVal.base must_== "int"
    }
    "not be required" in {
      integerOptVal.required must_== false
    }
    """have value Some("2")""" in {
      integerOptVal.value must_== Some("2")
    }
  }

  val integerInferred = Type(2)
  s""""${integerInferred.spec}"""" should {
    "have base int" in {
      integerInferred.base must_== "int"
    }
    "not be required" in {
      integerInferred.required must_== false
    }
    """have value Some("2")""" in {
      println(s"====== ${integerInferred.value}")
      integerInferred.value must_== Some("2")
    }
  }
}
