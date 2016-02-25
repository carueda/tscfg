package tscfg

import java.io.PrintWriter
import com.typesafe.config.ConfigOrigin
import tscfg.generator._
import scala.collection.JavaConversions._

object templateGenerator {

  sealed abstract class What
  case object genBase extends What
  case object genLocal extends What
  case object genAll extends What

  def generate(what: What, node: Node, out: PrintWriter): Unit = {

    node match {
      case bn: BranchNode =>
        val orderedNames = bn.map.keys.toList.sorted
        orderedNames foreach { name => gen(bn.map(name)) }

      case _ => throw new AssertionError()
    }

    def gen(n: Node, indent: String = ""): Unit = {
      val simple = n.key.simple
      require(simple != "/")
      val symbol = simple

      n match {
        case ln: LeafNode  => genForLeaf(ln)
        case n: BranchNode => genForBranch(n)
      }

      def genForLeaf(ln: LeafNode): Unit = {
        val (realComments, annotations) = CommentInfo.processComments(ln.value.origin())

        what match {
          case templateGenerator.genBase =>
            if (ln.type_.value.isDefined) {
              out.println()
              out.println(s"$indent# '$symbol': ${ln.type_.description}")
              CommentInfo.outComments(indent, realComments, out)
              out.println(s"$indent$symbol = ${ln.type_.value.get}")
            }

          case templateGenerator.genLocal =>
            if (ln.type_.required || ln.type_.value.isEmpty || CommentInfo.includeInLocal(annotations)) {
              out.println()
              out.println(s"$indent# '$symbol': ${ln.type_.description}")
              CommentInfo.outComments(indent, realComments, out)
              if (ln.type_.required)
                out.println(s"$indent$symbol =")
              else
                out.println(s"$indent#$symbol =")
            }

          case templateGenerator.genAll =>
            out.println()
            out.println(s"$indent# '$symbol': ${ln.type_.description}")
            CommentInfo.outComments(indent, realComments, out)
            if (ln.type_.required)
              out.println(s"$indent$symbol =")
            else if (ln.type_.value.isDefined)
              out.println(s"$indent$symbol = ${ln.type_.value.get}")
            else
              out.println(s"$indent#$symbol =")
        }
      }

      def genForBranch(bn: BranchNode): Unit = {
        val (realComments, annotations) = CommentInfo.processComments(bn.conf.origin())
        out.println()
        if (CommentInfo.optionalSection(annotations)) {
          out.println(s"$indent# '$symbol': Optional section.")
        }
        CommentInfo.outComments(indent, realComments, out)
        out.println(s"$indent$symbol {")

        val orderedNames = bn.map.keys.toList.sorted

        // generate for members:
        orderedNames foreach { name => gen(bn.map(name), indent + "  ") }

        out.println(s"$indent}")
      }
    }
  }
}

object CommentInfo {
  def processComments(bn: ConfigOrigin): (List[String],List[String]) = {
    val comments: List[String] = asScalaBuffer(bn.comments()).toList
    val (realComments, annotations) = comments.partition(!_.toString.startsWith("@"))
    (realComments, annotations)
  }

  def includeInLocal(annotations: List[String]): Boolean =
    annotations.contains("@local")

  def optionalSection(annotations: List[String]): Boolean =
    annotations.contains("@optional")

  def outComments(indent: String, comments: List[String], out: PrintWriter): Unit = {
    if (comments.nonEmpty) {
      out.println(s"$indent#" + comments.mkString(s"\n$indent#"))
    }
  }
}
