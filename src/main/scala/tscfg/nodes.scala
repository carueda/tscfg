package tscfg
import com.typesafe.config.{Config, ConfigValue}
import tscfg.generator.GenOpts

import scala.collection.JavaConversions._
import scala.collection.mutable

object nodes {
  sealed abstract class Node {
    val key: Key
  }

  case class LeafNode(key: Key, value: ConfigValue)(implicit genOpts: GenOpts) extends Node {
    val type_ = Type(value)
    val accessor = Accessor(type_)
  }

  case class BranchNode(key: Key, conf: Config) extends Node {
    val map: mutable.Map[String,Node] = mutable.Map()

    def put(simpleKey: String, node: Node): Unit = {
      map.put(simpleKey, node)
    }
  }

  val nodeMap = mutable.HashMap[Key, Node]()

  // creates a leaf node
  def createLeaf(key: Key, value: ConfigValue)(implicit genOpts: GenOpts): LeafNode = nodeMap.get(key) match {
    case None =>
      val node = LeafNode(key, value)
      put(key, node)
      node

    case Some(node) => throw new Error(s"Node by key=$key already created")
  }

  // creates a branch node
  def createBranch(key: Key, parentConf: Config): BranchNode = nodeMap.get(key) match {
    case None =>
      val node = BranchNode(key, parentConf)
      put(key, node)
      node

    case Some(node) if node.isInstanceOf[BranchNode] => node.asInstanceOf[BranchNode]
    case Some(node) => throw new Error(s"LeafNode by key=$key already created")
  }

  def put(key: Key, node: Node) = {
    //println(s"nodeMap PUT: $key -> $node")
    require(!nodeMap.contains(key))
    nodeMap.put(key, node)
  }

  def createAllNodes(conf: Config)(implicit genOpts: GenOpts): Node = {
    val root = BranchNode(Key.root, conf)
    nodes.put(Key.root, root)

    def createAncestorsOf(childKey: Key, childNode: Node): Unit = {
      createParent(childKey.parent, childNode)

      def createParent(parentKey: Key, child: Node): Unit = {
        val parentConf = if (parentKey == Key.root) conf
        else conf.getConfig(parentKey.toString)

        val parentNode = nodes.createBranch(parentKey, parentConf)
        parentNode.put(child.key.simple, child)

        if (parentNode.key != Key.root) {
          createParent(parentKey.parent, parentNode)
        }
      }
    }

    conf.entrySet() foreach { e =>
      val key = Key(e.getKey)
      val value = e.getValue
      val leafNode = nodes.createLeaf(key, value)
      createAncestorsOf(key, leafNode)
    }

    root
  }
}
