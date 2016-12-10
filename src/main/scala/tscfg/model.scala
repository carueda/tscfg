package tscfg

import tscfg.model.durations._

object model {

  object implicits {
    import scala.language.implicitConversions

    implicit def type2ann(t: Type): AnnType = AnnType(t)

    implicit class RichString(s: String) {
      def :=(a: AnnType): (String, AnnType) = s → a

      def %(a: AnnType): AnnType = a.copy(comments = Some(s + a.comments.getOrElse("")))

      def %(t: Type): AnnType = AnnType(t, comments = Some(s))
    }
  }

  object durations {
    sealed abstract class Qualification

    case object ns      extends Qualification
    case object us      extends Qualification
    case object ms      extends Qualification
    case object second  extends Qualification
    case object minute  extends Qualification
    case object hour    extends Qualification
    case object day     extends Qualification
  }

  sealed abstract class Type

  sealed abstract class BasicType extends Type

  case object STRING    extends BasicType
  case object INTEGER   extends BasicType
  case object LONG      extends BasicType
  case object DOUBLE    extends BasicType
  case object BOOLEAN   extends BasicType
  case class DURATION(q: Qualification) extends BasicType

  val recognizedAtomic: Map[String, BasicType] = Map(
    "string"    → STRING,
    "int"       → INTEGER,
    "integer"   → INTEGER,
    "long"      → LONG,
    "double"    → DOUBLE,
    "boolean"   → BOOLEAN,
    "duration"  → DURATION(ms)
  )

  case class ListType(t: Type) extends Type

  case class AnnType(t: Type,
                     optional:      Boolean = false,
                     default:       Option[String] = None,
                     comments:      Option[String] = None
                    ) {

    def isOptional: Boolean = optional || default.isDefined

    def |(d: String): AnnType = copy(default = Some(d))

    def unary_~ : AnnType = copy(optional = true)
  }

  case class ObjectType(members: Map[String, AnnType] = Map.empty) extends Type

  object ObjectType {
    def apply(elems: (String, AnnType)*): ObjectType = {
      val x = elems.groupBy(_._1).mapValues(_.length).filter(_._2 > 1)
      if (x.nonEmpty) throw new RuntimeException(s"key repeated in object: ${x.head}")
      ObjectType(Map(elems : _*))
    }
  }

  object util {
    val IND = "    "

    def format(typ: Type, ind: String = ""): String = typ match {
      case b:BasicType ⇒ b.toString

      case ListType(t) ⇒ s"[ ${format(t, ind)} ]"

      case o:ObjectType ⇒
        val symbols = o.members.keys.toList.sorted
        val membersStr = symbols.map { symbol ⇒
          val a = o.members(symbol)

          val cmn = if (a.comments.isEmpty) "" else {
            val lines = a.comments.get.split("\n")
            val noOptComments = lines.filterNot(_.trim.startsWith("@optional"))
            if (noOptComments.isEmpty) "" else {
              val x = noOptComments.mkString(s"\n$ind$IND#")
              s"#$x\n$ind$IND"
            }
          }

          val opt = if (a.optional) "optional " else ""
          val typ = format(a.t, ind + IND)
          val dfl = if (a.default.nonEmpty) s" default='${a.default.get}'" else ""

          cmn +
          symbol + ": " + opt + typ + dfl
        }.mkString("\n" + ind + IND)

        s"""{
           |$ind$IND$membersStr
           |$ind}""".stripMargin
    }
  }
}

object modelMain {
  import model._
  import model.implicits._

  def main(args: Array[String]): Unit = {

    val objectType = ObjectType(
      "positions" := "Position information" % ListType(ObjectType(
        "lat" := DOUBLE,
        "lon" := DOUBLE,
        "attrs" := ListType(ObjectType(
          "b" := BOOLEAN,
          "d" := DURATION(hour)
        ))
      )),
      "baz" := "comments for baz..." % ~ObjectType(
        "b" := ~ STRING | "some value",
        "a" := LONG | "99999999",
        "i" := "i, an integer" % INTEGER
      ),
      "xyz" := "other string xyz" % ~STRING
    )

    println(model.util.format(objectType))
  }
}
