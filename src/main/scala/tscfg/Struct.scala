package tscfg

import scala.collection.mutable

/**
  * Abstract definition for a data struct used for later generation of config member classes. It supports nested
  * member definitions utilizing the 'members' field
  *
  * @param name    Name of the config member
  * @param members Nested config definitions
  */
abstract class Struct(val name: String, val members: mutable.HashMap[String, Struct]) {

  def isLeaf: Boolean = members.isEmpty

  // $COVERAGE-OFF$
  def format(indent: String = ""): String = {
    if (members.isEmpty) {
      name
    }
    else {
      s"$name:\n" +
        members.map(e => indent + e._1 + ": " + e._2.format(indent + "    ")).mkString("\n")
    }
  }
  // $COVERAGE-ON$
}

private[tscfg] case object Struct {
  /**
    * [[Struct]] implementation, that is used for defining shared objects, that may be part of an inheritance tree
    *
    * @param name           Name of the config member
    * @param members        Nested config definitions
    * @param abstractObject true, if this object is abstract
    * @param maybeParentId  [[Option]] to the name of a parent object
    */
  final case class SharedObjectStruct(override val name: String,
                                      override val members: mutable.HashMap[String, Struct] = mutable.HashMap.empty,
                                      abstractObject: Boolean,
                                      maybeParentId: Option[String]
                               ) extends Struct(name, members)

  /**
    * [[Struct]] implementation for a predefined / shared object utilising enumeration values
    *
    * @param name    Name of the config member
    * @param members Nested config definitions
    */
  final case class EnumStruct(override val name: String,
                              override val members: mutable.HashMap[String, Struct] = mutable.HashMap.empty
                             ) extends Struct(name, members)

  /**
    * A 'simple' implementation of [[Struct]] that is used for the actual members of the config
    *
    * @param name    Name of the config member
    * @param members Nested config definitions
    */
  final case class MemberStruct(override val name: String,
                                override val members: mutable.HashMap[String, Struct] = mutable.HashMap.empty
                                 ) extends Struct(name, members)

  /**
    * Comprehensive list ov different struct Types
    */
  final case object StructTypes extends Enumeration {
    val Member, Enum, SharedObject = Value
  }
}
