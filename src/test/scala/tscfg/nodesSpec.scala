package tscfg

import com.typesafe.config.ConfigFactory
import org.specs2.mutable.Specification
import tscfg.generator.GenOpts

class nodesSpec extends Specification {

  implicit val genOpts = GenOpts()

  def getNode(key: Key): Option[nodes.Node] = nodes.nodeMap.get(key)

  val config = ConfigFactory.parseString(
    """
      |foo {
      |  bar = {
      |    baz: int
      |  }
      |}
    """.stripMargin
  ).resolve()
  val root = nodes.createAllNodes(config)

  val fooOpt = getNode(Key("foo"))
  val barOpt = getNode(Key("foo.bar"))
  val bazOpt = getNode(Key("foo.bar.baz"))

  "nodes" should {
    "satisfy various conditions" in {
      root.key must_== Key.root
      Some(root) === nodes.nodeMap.get(Key.root)
      root must beAnInstanceOf[nodes.BranchNode]

      fooOpt must beSome
      barOpt must beSome
      bazOpt must beSome

      val foo = fooOpt.get
      val bar = barOpt.get
      val baz = bazOpt.get

      foo.key.parent === Key.root
      bar.key.parent === foo.key
      baz.key.parent === bar.key

      foo must beAnInstanceOf[nodes.BranchNode]
      bar must beAnInstanceOf[nodes.BranchNode]
      baz must beAnInstanceOf[nodes.LeafNode]

      val fooBn = foo.asInstanceOf[nodes.BranchNode]
      val barBn = bar.asInstanceOf[nodes.BranchNode]

      fooBn.map must beDefinedAt("bar")
      barBn.map must beDefinedAt("baz")
    }
  }
}
