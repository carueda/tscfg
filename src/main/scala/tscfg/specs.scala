package tscfg

object specs {

  object types {
    sealed abstract class AtomicType

    case object STRING    extends AtomicType
    case object INTEGER   extends AtomicType
    case object LONG      extends AtomicType
    case object DOUBLE    extends AtomicType
    case object BOOLEAN   extends AtomicType
    case object DURATION  extends AtomicType

    val recognizedAtomic: Map[String, AtomicType] = Map(
      "string"    → STRING,
      "int"       → INTEGER,
      "integer"   → INTEGER,
      "long"      → LONG,
      "double"    → DOUBLE,
      "boolean"   → BOOLEAN,
      "duration"  → DURATION
    )
  }
  import types._

  sealed abstract class Spec {
    val comments: List[String]

    lazy val (annotations, realComments) = comments.partition(_.startsWith("@"))

    def isOptional: Boolean = annotations.contains("@optional")

    def defaultValue: Option[String] = None
    def qualification: Option[String] = None

    def format(indent: String = ""): String

    // $COVERAGE-OFF$
    protected def commentsString(indent: String = ""): String = {
      if (comments.nonEmpty) {
        val i2 = "\n" + indent + "#"
        i2 + comments.mkString(i2) + "\n"
      }
      else ""
    }
    // $COVERAGE-ON$
  }

  case class AtomicSpec(typ: AtomicType,
                        comments: List[String] = List.empty,
                        optional: Boolean = false,
                        override val defaultValue: Option[String] = None,
                        override val qualification: Option[String] = None
                       ) extends Spec {

    override def isOptional: Boolean = optional || super.isOptional

    // $COVERAGE-OFF$
    override def format(indent: String): String = {
      typ.toString + {
        (if (isOptional) " optional" else "") +
        (if (defaultValue.isDefined) s" default=${defaultValue.get}" else "") +
        (if (qualification.isDefined) s" qualification=${qualification.get}" else "") +
        commentsString(indent + "    ")
      }
    }
    // $COVERAGE-ON$
  }

  case class ObjSpec(key: Key,
                     children: Map[String, Spec],
                     comments: List[String] = List.empty
                    ) extends Spec {

    val orderedNames: List[String] = children.keys.toList.sorted

    // $COVERAGE-OFF$
    override def format(indent: String): String = {
      val commentsString = if (comments.nonEmpty) {
        indent + "  #" + comments.mkString("\n" + indent + "  #") + "\n\n"
      }
      else ""

      val symbols = orderedNames
      val childrenStr = symbols.map { symbol ⇒
        symbol + ": " + children(symbol).format(indent + "  ")
      }.mkString("\n" + indent + "  ")

      (if (isOptional) "optional " else "") +
      key.toString + "{\n" +
        commentsString +
        indent + "  " + childrenStr + "\n" +
        indent + "}"
    }
    // $COVERAGE-ON$
  }

  case class ListSpec(elemSpec: Spec,
                      comments: List[String] = List.empty
                     ) extends Spec {

    // $COVERAGE-OFF$
    override def format(indent: String): String = {
      (if (isOptional) "optional " else "") +
      s"ListOf(${elemSpec.format(indent)})" + commentsString(indent + "    ")
    }
    // $COVERAGE-ON$
  }
}
