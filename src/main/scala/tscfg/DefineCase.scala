package tscfg

sealed abstract class DefineCase {
  val isParent: Boolean = false
  val isEnum: Boolean = false
}

object DefineCase {
  case class SimpleDefineCase() extends DefineCase

  case class AbstractDefineCase() extends DefineCase {
    override val isParent: Boolean = true
  }

  case class ExtendsDefineCase(name: String) extends DefineCase

  case class EnumDefineCase() extends DefineCase {
    override val isEnum: Boolean = true
  }

  def getDefineCase(commentString: String): Option[DefineCase] = {
    val str = commentString.trim
    val tokens = str.split("\\s+", Int.MaxValue).toList
    tokens match {
      case "@define" :: "abstract" :: Nil =>
        Some(AbstractDefineCase())

      case "@define" :: "extends" :: name :: Nil =>
        Some(ExtendsDefineCase(name))

      case "@define" :: "extends" :: Nil =>
        throw new RuntimeException(s"Missing name after `extends`")

      case "@define" :: "enum" :: Nil =>
        Some(EnumDefineCase())

      case "@define" :: Nil =>
        Some(SimpleDefineCase())

      case "@define" :: _ =>
        throw new RuntimeException(s"Unrecognized `@define` construct")

      case _ =>
        None
    }
  }
}
