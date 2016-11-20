package tscfg
import com.typesafe.config.{Config, ConfigValue}
import tscfg.generator.GenOpts

import scala.collection.JavaConversions._
import scala.collection.{Iterable, mutable}

object nodes {

  object implicits {
    import scala.language.implicitConversions

    class LnWithAccessor(ln: LeafNode)(implicit genOpts: GenOpts) {
      val accessor = Accessor(ln.type_)
    }

    @inline implicit def lnWithAccessor(ln: LeafNode)(implicit genOpts: GenOpts): LnWithAccessor = new LnWithAccessor(ln)
  }

  sealed abstract class Node {
    val key: Key
  }

  case class LeafNode(key: Key, value: ConfigValue) extends Node {
    val type_ = Type(value)
  }

  case class BranchNode(key: Key, conf: Config) extends Node {

    def keys(): Iterable[String] = children.keys

    def get(simpleKey: String): Option[Node] = children.get(simpleKey)

    def apply(simpleKey: String): Node = children(simpleKey)

    def get(key: Key): Option[Node] = byKey.get(key)

    def putChild(simpleKey: String, node: Node): Unit = children.put(simpleKey, node)

    def putByKey(key: Key, node: Node) = {
      require(!byKey.contains(key))
      byKey.put(key, node)
    }

    private val children = mutable.Map[String,Node]()
    private val byKey = mutable.HashMap[Key, Node]()
  }

  def createAllNodes(conf: Config): BranchNode = {
    val root = BranchNode(Key.root, conf)
    root.putByKey(Key.root, root)

    def getBranchNode(parentKey: Key, parentConf: Config): BranchNode = root.get(parentKey) match {
      case None =>
        val node = BranchNode(parentKey, parentConf)
        root.putByKey(parentKey, node)
        node

      case Some(node) if node.isInstanceOf[BranchNode] => node.asInstanceOf[BranchNode]
      case Some(node) => throw new Error(s"LeafNode by key=$parentKey already created")
    }

    def createAncestorsOf(childKey: Key, childNode: Node): Unit = {
      createParent(childKey.parent, childNode)

      def createParent(parentKey: Key, child: Node): Unit = {
        val parentConf = if (parentKey == Key.root) conf
        else conf.getConfig(parentKey.toString)

        val parentNode = getBranchNode(parentKey, parentConf)
        parentNode.putChild(child.key.simple, child)

        if (parentNode.key != Key.root) {
          createParent(parentKey.parent, parentNode)
        }
      }
    }

    conf.entrySet() foreach { e =>
      val key = Key(e.getKey)
      val value = e.getValue
      val leafNode = LeafNode(key, value)
      root.putByKey(key, leafNode)
      createAncestorsOf(key, leafNode)
    }

    root
  }
}
