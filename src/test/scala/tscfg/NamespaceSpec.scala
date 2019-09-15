package tscfg

import org.specs2.mutable.Specification
import tscfg.model.ObjectType

class NamespaceSpec extends Specification {

  sequential

  val root: Namespace = Namespace.root
  val ns00: Namespace = root.extend("ns00")
  val ns01: Namespace = root.extend("ns01")
  val ns000: Namespace = ns00.extend("ns000")

  private val objectType = ObjectType()

  "root namespace" should {
    "have expected path" in {
      root.getPathString must_== ""
    }

    "add and resolve define" in {
      root.addDefine("RootDef1", objectType)
      root.resolveDefine("RootDef1") must beSome
      Namespace.getAllDefines.keys must_== Set("RootDef1")
    }
  }

  "nested namespace ns00 under root" should {
    "have expected path" in {
      ns00.getPathString must_== "ns00"
    }

    "add and resolve define in own namespace" in {
      ns00.addDefine("n00def1", objectType)
      ns00.resolveDefine("n00def1") must beSome

      Namespace.getAllDefines.keys must_== Set("RootDef1", "ns00.n00def1")
    }

    "resolve define in parent namespace" in {
      ns00.resolveDefine("RootDef1") must beSome
    }
  }

  "nested namespace ns000 under ns000" should {
    "have expected path" in {
      ns000.getPathString must_== "ns00.ns000"
    }

    "add and resolve define in own namespace" in {
      ns000.addDefine("n000def1", objectType)
      ns000.resolveDefine("n000def1") must beSome

      Namespace.getAllDefines.keys must_== Set(
        "RootDef1", "ns00.n00def1", "ns00.ns000.n000def1"
      )
    }
  }

  "nested namespace ns00 under root" should {
    "not resolve nested define" in {
      ns00.resolveDefine("n000def1") must beNone
    }
  }

  "nested namespace ns01 under root" should {
    "have expected path" in {
      ns01.getPathString must_== "ns01"
    }

    "not resolve define outside own namespace" in {
      ns01.resolveDefine("n00def1") must beNone
      ns01.resolveDefine("n000def1") must beNone
    }
  }

  "all defines" should {
    "resolve" in {
      val all = Namespace.getAllDefines
      all.size must_== 3
      all.get("RootDef1") must beSome(objectType)
      all.get("ns00.n00def1") must beSome(objectType)
      all.get("ns00.ns000.n000def1") must beSome(objectType)
    }
  }
}
