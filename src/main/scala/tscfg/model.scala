package tscfg

object model {

  object implicits {
    import scala.language.implicitConversions

    implicit def type2ann(t: Type): AnnType = AnnType(t)

    implicit class RichString(s: String) {
      def :=(a: AnnType): (String, AnnType) = s → a

      def %(a: AnnType): AnnType =
        a.copy(comments = if (a.comments.isEmpty) s else a.comments + "\n" + s)

      def %(t: Type): AnnType = AnnType(t, comments = s)
    }
  }

  sealed abstract class Type

  sealed abstract class BasicType extends Type

  case object STRING    extends BasicType
  case object INTEGER   extends BasicType
  case object LONG      extends BasicType
  case object DOUBLE    extends BasicType
  case object BOOLEAN   extends BasicType
  case object DURATION  extends BasicType

  case class ListType(t: Type) extends Type

  case class AnnType(t: Type,
                     optional: Boolean = false,
                     default: String = "",
                     comments: String = ""
                    ) {

    def |(d: String): AnnType = copy(default = d)

    def unary_~ : AnnType = copy(optional = true)
  }

  case class ObjectType(members: Map[String, AnnType]) extends Type

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

      case o:ObjectType            ⇒
        val symbols = o.members.keys.toList//.sorted
        val membersStr = symbols.map { symbol ⇒
          val a = o.members(symbol)

          val cmn = if (a.comments.isEmpty) "" else {
            val x = a.comments.replaceAll("\n", s"\n$ind$IND# ")
            s"# $x\n$ind$IND"
          }

          val opt = if (a.optional) "optional " else ""
          val typ = format(a.t, ind + IND)
          val dfl = if (a.default.nonEmpty) s" default='${a.default}'" else ""

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
          "d" := DURATION
        ))
      )),
      "baz" := "comments for baz..." % ObjectType(
        "b" := ~ STRING | "some value",
        "a" := LONG | "99999999",
        "i" := "i, an integer" % INTEGER
      ),
      "xyz" := "other string xyz" % ~STRING
    )

    println(model.util.format(objectType))
  }
}
