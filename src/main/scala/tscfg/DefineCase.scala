package tscfg

sealed abstract class DefineCase {
  val isAbstract: Boolean = false
  val isEnum: Boolean = false
}

object DefineCase {

  /**
    * Simple shared object type without inheritance or enumeration support
    */
  final case class SimpleSharedObject() extends DefineCase

  /**
    * The targeted shared object ius part of an inheritance structure. This comprises as well the root of an inheritance
    * tree, something intermediate or the leafs of the hierarchy.
    *
    * @param abstractType true. If the targeted shared object is abstract (cannot be instantiated)
    * @param parent       Optional identifier of the referenced parent shared object
    */
  final case class InheritanceSharedObject(abstractType: Boolean, parent: Option[String]) extends DefineCase {
    override val isAbstract: Boolean = abstractType
  }

  final case class EnumDefineCase() extends DefineCase {
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
  def parseDefineCase(commentString: String): Option[DefineCase] = {
    val str = commentString.trim
    val tokens = str.split("\\s+", Int.MaxValue).toList
    val res = tokens match {
      case "@define" :: "abstract" :: Nil =>
        /* This it the root of an inheritance tree FIXME #66 -- Adapt description */
        Some(InheritanceSharedObject(abstractType = true, None))

      case "@define" :: "abstract" :: "extends" :: name :: Nil =>
        /* This is an abstract shared object definition somewhere within the inheritance tree */
        Some(InheritanceSharedObject(abstractType = true, Some(name)))

      case "@define" :: "extends" :: name :: Nil =>
        /* This is an (instantiable) shared object definition somewhere within the inheritance tree */
        Some(InheritanceSharedObject(abstractType = false, Some(name)))

      case "@define" :: "extends" :: Nil =>
        throw new RuntimeException(s"Missing name after `extends`")

      case "@define" :: "enum" :: Nil =>
        Some(EnumDefineCase())

      case "@define" :: Nil =>
        Some(SimpleSharedObject())

      case "@define" :: rest =>
        throw new RuntimeException(s"Unrecognized `@define` construct: unexpected: ${rest.mkString(" ")}")

      case _ =>
        None
    }
    scribe.debug(s"parse: commentString='$commentString' => $res")
    res
  }
}
