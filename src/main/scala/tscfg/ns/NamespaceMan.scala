package tscfg.ns

import tscfg.generators.java.javaUtil
import tscfg.model.Type

class NamespaceMan {
  private val namespaces = collection.mutable.Map.empty[String, Namespace]

  val root: Namespace =
    create("", None, collection.mutable.HashMap[String, Type]())

  def resolve(namespace: String): Namespace = namespaces(namespace)

  def create(
      simpleName: String,
      parent: Option[Namespace],
      allDefines: collection.mutable.HashMap[String, Type]
  ): Namespace = {
    scribe.debug(
      s"RootNamespace.create: simpleName='$simpleName' parent=${parent
          .map(p => "'" + p.getPathString + "'")}"
    )
    val ns = new Namespace(this, simpleName, parent, allDefines)
    namespaces.put(ns.getPathString, ns)
    ns
  }
}

object NamespaceMan {
  // Checks that it is empty or a period-separated list of java identifiers
  // TODO could probably be more sophisticated
  def validName(namespace: String): Boolean = {
    namespace.isEmpty ||
    namespace.split('.').toList.forall(javaUtil.isJavaIdentifier)
  }
}
