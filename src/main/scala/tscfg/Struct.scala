package tscfg

import scala.collection.mutable

/**
  * Abstract definition for a data struct used for later generation of config member classes. It supports nested
  * member definitions utilizing the 'members' field
  *
  * @param name    Name of the config member
  * @param members Nested config definitions
  */
abstract class Struct(name: String, members: mutable.HashMap[String, _ <: Struct]) {

  def name(): String = name

  def members(): mutable.HashMap[String, _ <: Struct] = members

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
    * [[Struct]] implementation, that is used for defining shared objects
    *
    * @param name       Name of the config member
    * @param members    Nested config definitions
    * @param defineCase Additional information about the shared object attributes
    */
  final case class SharedObjectStruct(override val name: String,
                                      override val members: mutable.HashMap[String, Struct] = mutable.HashMap.empty,
                                      defineCase: DefineCase
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
}
