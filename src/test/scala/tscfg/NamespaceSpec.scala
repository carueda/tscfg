package tscfg

import org.scalatest.wordspec.AnyWordSpec
import tscfg.model.ObjectType

class NamespaceSpec extends AnyWordSpec {

  val root: Namespace  = Namespace.root
  val ns00: Namespace  = root.extend("ns00")
  val ns01: Namespace  = root.extend("ns01")
  val ns000: Namespace = ns00.extend("ns000")

  private val objectType = ObjectType()

  "root namespace" should {
    "have expected path" in {
      assert(root.getPathString === "")
    }

    "add and resolve define" in {
      root.addDefine("RootDef1", objectType)
      assert(root.resolveDefine("RootDef1").isDefined)
      assert(root.getAllDefines.keys === Set("RootDef1"))
    }
  }

  "nested namespace ns00 under root" should {
    "have expected path" in {
      assert(ns00.getPathString === "ns00")
    }

    "add and resolve define in own namespace" in {
      ns00.addDefine("n00def1", objectType)
      assert(ns00.resolveDefine("n00def1").isDefined)

      assert(root.getAllDefines.keys === Set("RootDef1", "ns00.n00def1"))
    }

    "resolve define in parent namespace" in {
      assert(ns00.resolveDefine("RootDef1").isDefined)
    }
  }

  "nested namespace ns000 under ns000" should {
    "have expected path" in {
      assert(ns000.getPathString === "ns00.ns000")
    }

    "add and resolve define in own namespace" in {
      ns000.addDefine("n000def1", objectType)
      assert(ns000.resolveDefine("n000def1").isDefined)

      assert(
        root.getAllDefines.keys === Set(
          "RootDef1",
          "ns00.n00def1",
          "ns00.ns000.n000def1"
        )
      )
    }
  }

  "nested namespace ns00 under root" should {
    "not resolve nested define" in {
      assert(ns00.resolveDefine("n000def1").isEmpty)
    }
  }

  "nested namespace ns01 under root" should {
    "have expected path" in {
      assert(ns01.getPathString === "ns01")
    }

    "not resolve define outside own namespace" in {
      assert(ns01.resolveDefine("n00def1").isEmpty)
      assert(ns01.resolveDefine("n000def1").isEmpty)
    }
  }

  "all defines" should {
    "resolve" in {
      val all = root.getAllDefines
      assert(all.size === 3)
      assert(all.get("RootDef1") contains objectType)
      assert(all.get("ns00.n00def1") contains objectType)
      assert(all.get("ns00.ns000.n000def1") contains objectType)
    }
  }
}
