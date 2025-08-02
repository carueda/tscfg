package tscfg

import tscfg.exceptions.ObjectDefinitionException

sealed abstract class DefineCase {
  val isAbstract: Boolean = false
  val isEnum: Boolean     = false
  val isExternal: Boolean = false
}

object DefineCase {
  case object SimpleDefineCase extends DefineCase

  case object AbstractDefineCase extends DefineCase {
    override val isAbstract: Boolean = true
  }

  case class ExtendsDefineCase(abs: Boolean, nameGiven: String)
      extends DefineCase {
    override val isExternal: Boolean = nameGiven.startsWith("!")
    val name: String = if (isExternal) nameGiven.substring(1) else nameGiven
    override val isAbstract: Boolean = abs
  }

  case class ImplementsDefineCase(abs: Boolean, nameGiven: String)
      extends DefineCase {
    override val isExternal: Boolean = nameGiven.startsWith("!")
    val name: String = if (isExternal) nameGiven.substring(1) else nameGiven
    override val isAbstract: Boolean = abs
  }

  case object EnumDefineCase extends DefineCase {
    override val isEnum: Boolean = true
  }

  /** Extracts the shared objects' additional information given by a preceding
    * comment string. If the string does not start with '@define' (it is no
    * shared object), [[None]] is returned.
    *
    * @param commentString
    *   Comment String to parse for additional information
    * @throws ObjectDefinitionException
    *   If the comment string is malformed
    * @return
    *   An [[Option]] on additional information of the shared object
    */
  def getDefineCase(commentString: String): Option[DefineCase] = {
    val str    = commentString.trim
    val tokens = str.split("\\s+", Int.MaxValue).toList
    val res    = tokens match {
      case "@define" :: "abstract" :: Nil =>
        Some(AbstractDefineCase)

      case "@define" :: "abstract" :: "extends" :: name :: Nil =>
        Some(ExtendsDefineCase(abs = true, name))

      case "@define" :: "extends" :: name :: Nil =>
        Some(ExtendsDefineCase(abs = false, name))

      case "@define" :: "extends" :: Nil =>
        throw ObjectDefinitionException(
          s"Missing name after `extends`: '$commentString'"
        )

      case "@define" :: "abstract" :: "implements" :: name :: Nil =>
        Some(ImplementsDefineCase(abs = true, name))

      case "@define" :: "implements" :: name :: Nil =>
        Some(ImplementsDefineCase(abs = false, name))

      case "@define" :: "implements" :: Nil =>
        throw ObjectDefinitionException(
          s"Missing name after `implements`: '$commentString'"
        )

      case "@define" :: "enum" :: Nil =>
        Some(EnumDefineCase)

      case "@define" :: Nil =>
        Some(SimpleDefineCase)

      case "@define" :: _ =>
        throw ObjectDefinitionException(s"Unrecognized @define construct: $str")

      case _ =>
        None
    }
    scribe.debug(s"getDefineCase: commentString='$commentString' => $res")
    res
  }
}
