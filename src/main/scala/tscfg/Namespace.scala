package tscfg

import tscfg.model.{AbstractObjectType, ObjectRefType, Type}

object Namespace {
  /** Returns a new, empty root namespace. */
  def root: Namespace = new Namespace("", None,
    collection.mutable.HashMap[String, Type]())
}

class Namespace private(simpleName: String, parent: Option[Namespace],
                        allDefines: collection.mutable.HashMap[String, Type]) {

  def getAllDefines: Map[String, Type] = allDefines.toMap

  def getDefine(defineName: String): Option[Type] = allDefines.toMap.get(defineName)

  def isAbstractClassDefine(parentName: String): Boolean = {
    allDefines.get(parentName) match {
      case Some(modelType) => modelType match {
        case _:AbstractObjectType => true
        case _ => false
      }
      case None => false
    }
  }

  def getPath: Seq[String] = parent match {
    case None => Seq.empty
    case Some(ns) => ns.getPath ++ Seq(simpleName)
  }

  def getPathString: String = getPath.mkString(".")

  def extend(simpleName: String): Namespace = new Namespace(simpleName, Some(this), allDefines)

  private val defineNames = collection.mutable.HashSet[String]()
  private val defineAbstractClassNames = collection.mutable.HashSet[String]()

  def addDefine(simpleName: String, t: Type, isParent: Boolean = false): Unit = {

    /* sanity check */
    assert(!simpleName.contains("."))
    assert(simpleName.nonEmpty)

    if (defineNames.contains(simpleName) || defineAbstractClassNames.contains(simpleName)) {
      val ns = if (getPath.nonEmpty) s"'$getPathString'" else "(root)"
      println(s"WARN: duplicate @define '$simpleName' in namespace $ns. Ignoring previous entry")
      // TODO include in build warnings
    }

    if (isParent)
      defineAbstractClassNames.add(simpleName)
    defineNames.add(simpleName)

    allDefines.update(resolvedFullPath(simpleName), t)
  }

  private def resolvedFullPath(simpleName: String): String = parent match {
    case None => simpleName
    case Some(_) => s"$getPathString.$simpleName"
  }

  def resolveDefine(name: String): Option[ObjectRefType] = {
    if(defineAbstractClassNames.contains(name))
        None // if the string nam is an abstract class no ObjectRefType is needed
    else if (defineNames.contains(name))
      Some(ObjectRefType(this, name))
    else
      parent.flatMap(_.resolveDefine(name))
  }
}
