package tscfg

sealed abstract class DefineCase {
  val isAbstract: Boolean = false
  val isEnum: Boolean = false
}

object DefineCase {
  case object SimpleDefineCase extends DefineCase

  case object AbstractDefineCase extends DefineCase {
    override val isAbstract: Boolean = true
  }

  case class ExtendsDefineCase(name: String, abs: Boolean) extends DefineCase {
    override val isAbstract: Boolean = abs
  }

  final case object EnumDefineCase extends DefineCase {
    override val isEnum: Boolean = true
  }

  /**
    * Extracts the shared objects' additional information given by a preceding comment string. If the string does not
    * start with '@define' (it is no shared object), [[None]] is returned.
    *
    * @param commentString Comment String to parse for additional information
    * @throws RuntimeException If the comment string is malformed
    * @return An [[Option]] on additional information of the shared object
    */
    @throws[RuntimeException]
  def getDefineCase(commentString: String): Option[DefineCase] = {
    val str = commentString.trim
    val tokens = str.split("\\s+", Int.MaxValue).toList
    val res = tokens match {
      case "@define" :: "abstract" :: Nil =>
        Some(AbstractDefineCase)

      case "@define" :: "abstract" :: "extends" :: name :: Nil =>
        /* This is an abstract shared object definition somewhere within the inheritance tree */
        Some(ExtendsDefineCase(name, abs = true))

      case "@define" :: "extends" :: name :: Nil =>
        Some(ExtendsDefineCase(name, abs = false))

      case "@define" :: "extends" :: Nil =>
        throw new RuntimeException(s"Missing name after `extends`")

      case "@define" :: "enum" :: Nil =>
        Some(EnumDefineCase)

      case "@define" :: Nil =>
        Some(SimpleDefineCase)

      case "@define" :: rest =>
        throw new RuntimeException(s"Unrecognized construct: $str")

      case _ =>
        None
    }
    scribe.debug(s"getDefineCase: commentString='$commentString' => $res")
    res
  }
}
