package tscfg

import org.specs2.mutable.Specification

class KeySpec extends Specification {

  """Key("/")""" should {
    "be Key.root" in {
      Key("/") must_== Key.root
    }
    """be "/" as string""" in {
      Key("/").toString must_== "/"
    }
  }
  """Key("foo")""" should {
    """have Key.root as parent""" in {
      Key("foo").parent must_== Key.root
    }
  }
  """Key("foo.baz")""" should {
    """be "foo.baz as string""" in {
      Key("foo.baz").toString must_== "foo.baz"
    }
    """have Key("foo") as parent""" in {
      Key("foo.baz").parent must_== Key("foo")
    }
    """be "baz" in simple form""" in {
      Key("foo.baz").simple must_== "baz"
    }
  }
}
