package tscfg.ns

import tscfg.DefineCase
import tscfg.DefineCase.SimpleDefineCase
import tscfg.model.{AbstractObjectType, ObjectRealType, ObjectRefType, Type}

class Namespace private[ns] (
    rootNamespace: NamespaceMan,
    simpleName: String,
    parent: Option[Namespace],
    allDefines: collection.mutable.HashMap[String, Type]
) {

  val isRoot: Boolean = parent.isEmpty

  def getAllDefines: Map[String, Type] = allDefines.toMap

  def getDefine(defineName: String): Option[Type] =
    allDefines.toMap.get(defineName)

  def getRealDefine(defineName: String): Option[ObjectRealType] = {
    val res = getDefine(defineName) match {
      case Some(o: ObjectRealType) => Some(o)
      case _                       => None
    }
    scribe.debug(s"getRealDefine: defineName='$defineName' => $res")
    scribe.debug(s"allDefines=$allDefines")
    res
  }

  def getAbstractDefine(defineName: String): Option[AbstractObjectType] =
    getDefine(defineName) match {
      case Some(aot: AbstractObjectType) => Some(aot)
      case _                             => None
    }

  def isAbstractClassDefine(parentName: String): Boolean =
    getAbstractDefine(parentName).isDefined

  def getPath: Seq[String] = parent match {
    case None     => Seq.empty
    case Some(ns) => ns.getPath ++ Seq(simpleName)
  }

  def getPathString: String = getPath.mkString(".")

  def extend(simpleName: String): Namespace =
    rootNamespace.create(simpleName, Some(this), allDefines)

  private val defineNames              = collection.mutable.HashSet[String]()
  private val defineAbstractClassNames = collection.mutable.HashSet[String]()

  private var okDuplicates: Option[Set[String]] = None

  // to facilitate handling of 2nd pass
  def setOkDuplicates(names: Set[String]): Unit =
    okDuplicates = Some(names)

  def addDefine(
      simpleName: String,
      t: Type,
      defineCase: DefineCase = SimpleDefineCase
  ): Unit = {
    scribe.debug(
      s"addDefine: simpleName='$simpleName' t=$t  defineCase=$defineCase"
    )
    /* sanity check */
    assert(!simpleName.contains("."))
    assert(simpleName.nonEmpty)

    val isDuplicate = defineNames.contains(simpleName) ||
      defineAbstractClassNames.contains(simpleName)
    if (isDuplicate && !okDuplicates.exists(_.contains(simpleName))) {
      val ns = if (getPath.nonEmpty) s"'$getPathString'" else "(root)"
      println(
        s"WARN: duplicate @define '$simpleName' in namespace $ns. Ignoring previous entry"
      )
      // TODO include in build warnings
    }

    val isAbstract = defineCase.isAbstract
    if (isAbstract)
      defineAbstractClassNames.add(simpleName)
    defineNames.add(simpleName)

    allDefines.update(resolvedFullPath(simpleName), t)
  }

  private def resolvedFullPath(simpleName: String): String = parent match {
    case None    => simpleName
    case Some(_) => s"$getPathString.$simpleName"
  }

  def resolveDefine(name: String): Option[ObjectRefType] = {
    if (defineNames.contains(name) && !defineAbstractClassNames.contains(name))
      Some(ObjectRefType(simpleName, name))
    else
      parent.flatMap(_.resolveDefine(name))
  }
}
