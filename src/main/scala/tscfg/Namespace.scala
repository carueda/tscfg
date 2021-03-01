package tscfg

import tscfg.generators.java.javaUtil
import tscfg.model.{AbstractObjectType, ObjectRefType, Type}

object Namespace {
  /**
    * Returns a new, empty root namespace.
    * This also means a "session reinitialization" in terms of created namespaces.
    */
  def root: Namespace = {
    namespaces.clear()
    create("", None, collection.mutable.HashMap[String, Type]())
  }
  // Checks that it is empty or a period-separated list of java identifiers
  // TODO could probably be more sophisticated
  def validName(namespace: String): Boolean = {
    namespace.isEmpty ||
    namespace.split('.').toList.forall(javaUtil.isJavaIdentifier)
  }

  def resolve(namespace: String): Namespace = namespaces(namespace)

  private def create(simpleName: String,
                     parent: Option[Namespace],
                     allDefines: collection.mutable.HashMap[String, Type]
                    ): Namespace = {
    scribe.debug(
      s"Namespace.create: simpleName='$simpleName' parent=${parent.map(p => "'" + p.getPathString + "'")}"
    )
    val ns = new Namespace(simpleName, parent, allDefines)
    namespaces.put(ns.getPathString, ns)
    ns
  }

  private[this] val namespaces = collection.mutable.Map.empty[String, Namespace]
}

class Namespace private(simpleName: String, parent: Option[Namespace],
                        allDefines: collection.mutable.HashMap[String, Type]) {

  def getAllDefines: Map[String, Type] = allDefines.toMap

  def getDefine(defineName: String): Option[Type] = allDefines.toMap.get(defineName)

  def getAbstractDefine(defineName: String): Option[AbstractObjectType] =
    getDefine(defineName) match {
      case Some(aot: AbstractObjectType) => Some(aot)
      case _ => None
    }

  def isAbstractClassDefine(parentName: String): Boolean =
    getAbstractDefine(parentName).isDefined

  def getPath: Seq[String] = parent match {
    case None => Seq.empty
    case Some(ns) => ns.getPath ++ Seq(simpleName)
  }

  def getPathString: String = getPath.mkString(".")

  def extend(simpleName: String): Namespace = Namespace.create(simpleName, Some(this), allDefines)

  private val defineNames = collection.mutable.HashSet[String]()
  private val defineAbstractClassNames = collection.mutable.HashSet[String]()

  def addDefine(simpleName: String, t: Type, isAbstract: Boolean = false): Unit = {
    scribe.debug(s"addDefine: simpleName='$simpleName' t=$t")
    /* sanity check */
    assert(!simpleName.contains("."))
    assert(simpleName.nonEmpty)

    if (defineNames.contains(simpleName) || defineAbstractClassNames.contains(simpleName)) {
      val ns = if (getPath.nonEmpty) s"'$getPathString'" else "(root)"
      println(s"WARN: duplicate @define '$simpleName' in namespace $ns. Ignoring previous entry")
      // TODO include in build warnings
    }

    if (isAbstract)
      defineAbstractClassNames.add(simpleName)
    defineNames.add(simpleName)

    allDefines.update(resolvedFullPath(simpleName), t)
  }

  private def resolvedFullPath(simpleName: String): String = parent match {
    case None => simpleName
    case Some(_) => s"$getPathString.$simpleName"
  }

  def resolveDefine(name: String): Option[ObjectRefType] = {
    if (name.startsWith("Shared")) {
      scribe.debug(s"resolveDefine: name='$name'")
      scribe.debug(s"resolveDefine: defineNames=$defineNames\n")
    }
    if (defineNames.contains(name) && !defineAbstractClassNames.contains(name))
      Some(ObjectRefType(simpleName, name))
    else
      parent.flatMap(_.resolveDefine(name))
  }
}
