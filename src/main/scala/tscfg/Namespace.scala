package tscfg

import tscfg.model.{ObjectRefType, Type}

object Namespace {
  val root: Namespace = new Namespace("", None)

  def getAllDefines: Map[String, Type] = {
    allDefines.toMap
  }

  private def addToAllDefines(defineFullPath: String, t: Type): Unit = {
    allDefines.update(defineFullPath, t)
  }

  private val allDefines = collection.mutable.HashMap[String, Type]()
}

class Namespace private(simpleName: String, parent: Option[Namespace]) {
  import Namespace.addToAllDefines

  def getPath: Seq[String] = parent match {
    case None ⇒ Seq.empty
    case Some(ns) ⇒ ns.getPath ++ Seq(simpleName)
  }

  def getPathString: String = getPath.mkString(".")

  def extend(simpleName: String): Namespace = new Namespace(simpleName, Some(this))

  private val defineNames = collection.mutable.HashSet[String]()

  def addDefine(simpleName: String, t: Type): Unit = {
    assert(!simpleName.contains("."))
    assert(simpleName.nonEmpty)

    if (defineNames.contains(simpleName)) {
      println(s"WARN: duplicate @define '$simpleName' in namespace $getPathString. Ignoring previous entry")
      // TODO include in build warnings
    }

    defineNames.add(simpleName)

    addToAllDefines(resolvedFullPath(simpleName), t)
  }

  private def resolvedFullPath(simpleName: String): String = parent match {
    case None ⇒ simpleName
    case Some(_) ⇒ s"$getPathString.$simpleName"
  }

  def resolveDefine(name: String): Option[ObjectRefType] = {
    if (defineNames.contains(name))
      Some(ObjectRefType(this, name))
    else
      parent.flatMap(_.resolveDefine(name))
  }
}
