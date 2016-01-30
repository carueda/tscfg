package tscfg

import java.io.{PrintWriter, Writer}

import com.typesafe.config.{Config, ConfigFactory, ConfigValue}

import scala.collection.JavaConversions._
import scala.collection.mutable


object generator {
  val version = ConfigFactory.load().getString("tscfg.version")

  val defaultPackageName = "tscfg.example"
  val defaultClassName   = "ExampleCfg"

  /**
    * Generation options
    *
    * @param packageName  package name
    * @param className    class name
    * @param j7           true to generate code for Typesafe Config v &lt;= 1.2.1
    * @param preamble     preamble to include in generated code
    * @param language     target language, default "java"
    */
  case class GenOpts(packageName: String = defaultPackageName,
                     className: String   = defaultClassName,
                     j7: Boolean         = false,
                     preamble: Option[String] = None,
                     language: String    = "java"
                    )

  /**
    * Generates code for the given configuration spec.
    *
    * @param config       spec
    * @param out          code is written here
    * @param genOpts      generation options
    */
  def generate(config: Config, out: Writer)
              (implicit genOpts: GenOpts): Unit = {

    createAllNodes(config)

    val pw = out match {
      case w: PrintWriter => w
      case w => new PrintWriter(w)
    }

    genOpts.language match {
      case "java"  => javaGenerator.generate(Node.root, pw)
      case "scala" => scalaGenerator.generate(Node.root, pw)
    }
  }

  // we first traverse the list of elements to build a tree representation.

  sealed abstract class Node {
    val key: Key
  }

  case class LeafNode(key: Key, value: ConfigValue)(implicit genOpts: GenOpts) extends Node {
    val accessor = Accessor.parseValueSpec(value.unwrapped().toString)
  }

  case class BranchNode(key: Key) extends Node {
    val map: mutable.Map[String,Node] = mutable.Map()

    def put(simpleKey: String, node: Node): Unit = {
      map.put(simpleKey, node)
    }
  }

  object Node {
    val root = BranchNode(Key.root)
  }


  object nodes {
    val nodeMap = mutable.HashMap[Key, Node]()

    put(Key.root, Node.root)

    // creates a leaf node
    def createLeaf(key: Key, value: ConfigValue)(implicit genOpts: GenOpts): LeafNode = nodeMap.get(key) match {
      case None =>
        val node = LeafNode(key, value)
        put(key, node)
        node

      case Some(node) => throw new Error(s"Node by key=$key already created")
    }

    // creates a branch node
    def createBranch(key: Key): BranchNode = nodeMap.get(key) match {
      case None =>
        val node = BranchNode(key)
        put(key, node)
        node

      case Some(node) if node.isInstanceOf[BranchNode] => node.asInstanceOf[BranchNode]
      case Some(node) => throw new Error(s"LeafNode by key=$key already created")
    }

    private def put(key: Key, node: Node) = {
      //println(s"nodeMap PUT: $key -> $node")
      require(!nodeMap.contains(key))
      nodeMap.put(key, node)
    }
  }

  private def createAllNodes(conf: Config)(implicit genOpts: GenOpts): Unit = {
    conf.entrySet() foreach { e =>
      val key = Key(e.getKey)
      val value = e.getValue
      val leafNode = nodes.createLeaf(key, value)
      createAncestorsOf(key, leafNode)
    }
  }

  private def createAncestorsOf(childKey: Key, childNode: Node): Unit = {
    createParent(childKey.parent, childNode)

    def createParent(parentKey: Key, child: Node): Unit = {
      val parentNode = nodes.createBranch(parentKey)
      parentNode.put(child.key.simple, child)

      if (parentNode.key != Key.root)
        createParent(parentKey.parent, parentNode)
    }
  }

  private def showNode(node: Node, indentIncr:String = "\t", simpleKey: Boolean = true): Unit = {
    show(node)

    def show(n: Node, indent: String = ""): Unit = {
      val label = if (simpleKey) n.key.simple else n.key.toString

      n match {
        case LeafNode(key, value) =>
          println(s"$indent$label = ${value.render()}")

        case n: BranchNode =>
          println(s"$indent$label")
          n.map.keys.toList.sorted foreach { key =>
            show(n.map(key), indent + indentIncr)
          }
      }
    }
  }

}
