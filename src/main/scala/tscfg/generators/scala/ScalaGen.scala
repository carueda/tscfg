package tscfg.generators.scala

import tscfg.{ModelBuilder, Namespace, model, util}
import tscfg.generators._
import tscfg.model._
import tscfg.util.escapeString
import tscfg.codeDefs.scalaDef

class ScalaGen(genOpts: GenOpts) extends Generator(genOpts) {
  val accessors = new Accessors

  import defs._

  implicit val methodNames: MethodNames = MethodNames()
  val getter: Getter                    = Getter(genOpts, hasPath, accessors)

  import methodNames._

  val scalaUtil: ScalaUtil = new ScalaUtil(useBackticks = genOpts.useBackticks)

  import scalaUtil.{scalaIdentifier, getClassName}

  def padScalaIdLength(implicit symbols: List[String]): Int =
    if (symbols.isEmpty) 0
    else
      symbols.map(scalaIdentifier).maxBy(_.length).length

  def padId(id: String)(implicit symbols: List[String]): String =
    id + (" " * (padScalaIdLength - id.length))

  def generate(objectType: ObjectType): GenResult = {
    genResults = GenResult()

    checkUserSymbol(className)
    val res = generateForObj(objectType, className = className, isRoot = true)

    val packageStr = s"package ${genOpts.packageName}\n\n"

    val definition = (packageStr + res.definition).trim
    genResults.copy(code = {
      if (util.doFormatting)
        formatter.format(genOpts.packageName, definition)
      else
        definition
    })
  }

  private def generate(
      typ: Type,
      classNamesPrefix: List[String],
      className: String,
      parentClassName: Option[String] = None,
      parentClassMembers: Option[Map[String, model.AnnType]] = None,
  ): Res = typ match {

    case et: EnumObjectType => generateForEnum(et, classNamesPrefix, className)

    case ot: ObjectType =>
      generateForObj(
        ot,
        classNamesPrefix,
        className,
        parentClassName = parentClassName,
        parentClassMembers = parentClassMembers
      )

    case aot: AbstractObjectType =>
      generateForAbstractObj(
        aot,
        classNamesPrefix,
        className,
        parentClassName,
        parentClassMembers
      );

    case ort: ObjectRefType => generateForObjRef(ort, classNamesPrefix)

    case lt: ListType => generateForList(lt, classNamesPrefix, className)

    case bt: BasicType => generateForBasic(bt)
  }

  def buildClassMembersString(
      classData: List[(String, Res, AnnType, Boolean)],
      padId: String => String,
      isAbstractClass: Boolean = false
  ): String = {
    classData
      .flatMap { case (symbol, res, a, isChildClass) =>
        if (a.isDefine) None
        else
          Some {
            val memberType = res.scalaType
            val typ =
              if (a.optional && a.default.isEmpty) s"scala.Option[$memberType]"
              else memberType
            val scalaId = scalaIdentifier(symbol)
            val modifiers = (isChildClass, isAbstractClass) match {
              case (true, _) =>
                /* The field will be overridden, therefore it needs override modifier and "val" keyword */
                "override val "
              case (false, true) =>
                /* The field does not override anything, but is part of abstract class => needs "val" keyword */
                "val "
              case _ => ""
            }
            val type_ = a.t match {
              case ot: ObjectRefType =>
                getClassNameForObjectRefType(ot)
              case _ => typ
            }
            genResults = genResults
              .copy(fields = genResults.fields + (scalaId -> type_.toString))
            modifiers + padId(scalaId) + " : " + type_ + dbg("E")
          }
      }
      .mkString(",\n  ")
  }

  private def generateForObj(
      ot: ObjectType,
      classNamesPrefix: List[String] = List.empty,
      className: String,
      isRoot: Boolean = false,
      parentClassName: Option[String] = None,
      parentClassMembers: Option[Map[String, model.AnnType]] = None
  ): Res = {

    genResults = genResults.copy(classNames = genResults.classNames + className)

    implicit val symbols: List[String] = ot.members.keys.toList.sorted
    symbols.foreach(checkUserSymbol)

    val results = symbols.map { symbol =>
      val a = ot.members(symbol)
      val res = generate(
        a.t,
        classNamesPrefix = className + "." :: classNamesPrefix,
        className = getClassName(symbol),
        a.abstractClass,
        a.parentClassMembers,
      )
      (symbol, res, a, false)
    }

    val parentClassMemberResults = parentClassMembers
      .map(_.keys.toList.sorted)
      .map(parentSymbols => {
        parentSymbols.foreach(checkUserSymbol)
        parentSymbols.map { symbol =>
          val a = parentClassMembers.get(symbol)
          val res = generate(
            a.t,
            classNamesPrefix = className + "." :: classNamesPrefix,
            className = getClassName(symbol),
            a.abstractClass,
            a.parentClassMembers,
          )
          (symbol, res, a, true)
        }
      })
      .getOrElse(List.empty)

    val classMembersStr =
      buildClassMembersString(parentClassMemberResults ++ results, padId)

    val parentClassString = parentClassName
      .map(
        " extends " + _ + "(" +
          parentClassMemberResults.map(_._1).mkString(",") + ")"
      )
      .getOrElse("")

    val classStr =
      s"""final case class $className(
         |  $classMembersStr
         |)$parentClassString
         |""".stripMargin

    implicit val listAccessors =
      collection.mutable.LinkedHashMap[String, String]()

    val objectMembersStr = (results ++ parentClassMemberResults)
      .flatMap { case (symbol, res, a, parentOv) =>
        if (a.isDefine) None
        else
          Some {
            val scalaId = scalaIdentifier(symbol)
            padId(scalaId) + " = " + getter.instance(
              a,
              res,
              path = escapeString(symbol)
            )
          }
      }
      .mkString(",\n      ")

    val innerClassesStr = {
      val defs = results.map(_._2.definition).filter(_.nonEmpty)
      if (defs.isEmpty) ""
      else {
        "\n  " + defs.mkString("\n").replaceAll("\n", "\n  ")
      }
    }

    val elemAccessorsStr = {
      val objOnes =
        if (listAccessors.isEmpty) ""
        else {
          "\n" + listAccessors.keys.toList.sorted
            .map { methodName =>
              listAccessors(methodName)
            }
            .mkString("\n")
        }
      val rootOnes =
        if (!isRoot) ""
        else {
          if (accessors.rootListAccessors.isEmpty) ""
          else {
            "\n\n" + accessors.rootListAccessors.keys.toList.sorted
              .map { methodName =>
                accessors.rootListAccessors(methodName)
              }
              .mkString("\n")
          }
        }
      objOnes + rootOnes
    }

    val rootAuxClasses = if (isRoot) {
      scalaDef("$TsCfgValidator")
    }
    else ""

    val (ctorParams, errHandlingDecl, errHandlingDispatch) = if (isRoot) {
      (
        "c: com.typesafe.config.Config",
        """val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
          |    val parentPath: java.lang.String = ""
          |    val $result = """.stripMargin,
        s"""
           |    $$tsCfgValidator.validate()
           |    $$result""".stripMargin
      )
    }
    else
      (
        "c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator",
        "",
        ""
      )

    val fullClassName = classNamesPrefix.reverse.mkString + className
    val objectString = {
      s"""object $className {$innerClassesStr
         |  def apply($ctorParams): $fullClassName = {
         |    $errHandlingDecl$fullClassName(
         |      $objectMembersStr
         |    )$errHandlingDispatch
         |  }$elemAccessorsStr
         |$rootAuxClasses}
      """.stripMargin
    }

    val baseType = classNamesPrefix.reverse.mkString + className
    Res(
      ot,
      scalaType = BaseScalaType(baseType),
      definition = classStr + objectString
    )
  }

  private def generateForAbstractObj(
      aot: AbstractObjectType,
      classNamesPrefix: List[String],
      className: String,
      parentClassName: Option[String] = None,
      parentClassMembers: Option[Map[String, model.AnnType]] = None
  ): Res = {

    genResults = genResults.copy(classNames = genResults.classNames + className)

    implicit val symbols: List[String] = aot.members.keys.toList.sorted
    symbols.foreach(checkUserSymbol)

    val results = symbols.map { symbol =>
      val a = aot.members(symbol)
      val res = generate(
        a.t,
        classNamesPrefix = className + "." :: classNamesPrefix,
        className = getClassName(symbol),
        a.abstractClass,
        a.parentClassMembers,
      )
      (symbol, res, a, false)
    }

    /* Consider parent abstract classes */
    val parentClassMemberResults = parentClassMembers
      .map(_.keys.toList.sorted)
      .map(parentSymbols => {
        parentSymbols.foreach(checkUserSymbol)
        parentSymbols.map { symbol =>
          val a = parentClassMembers.get(symbol)
          val res = generate(
            a.t,
            classNamesPrefix = className + "." :: classNamesPrefix,
            className = getClassName(symbol),
            a.abstractClass,
            a.parentClassMembers,
          )
          (symbol, res, a, true)
        }
      })
      .getOrElse(List.empty)

    val abstractClassMembersStr = buildClassMembersString(
      parentClassMemberResults ++ results,
      padId,
      isAbstractClass = true
    )

    val parentClassString = parentClassName
      .map(
        " extends " + _ + "(" +
          parentClassMemberResults.map(_._1).mkString(",") + ")"
      )
      .getOrElse("")

    val abstractClassStr =
      s"""sealed abstract class $className (
         | $abstractClassMembersStr
         |)$parentClassString
         |""".stripMargin

    val baseType = classNamesPrefix.reverse.mkString + className

    Res(aot, scalaType = BaseScalaType(baseType), definition = abstractClassStr)

  }

  private def generateForObjRef(
      ort: ObjectRefType,
      classNamesPrefix: List[String]
  ): Res = {

    val className = getClassName(ort.simpleName)
    genResults = genResults.copy(classNames = genResults.classNames + className)

    val fullScalaName = getClassNameForObjectRefType(ort)

    Res(ort, scalaType = BaseScalaType(fullScalaName + dbg("<X>")))
  }

  private def getClassNameForObjectRefType(ot: ObjectRefType): String = {
    val className = getClassName(ot.simpleName)
    val namespace = Namespace.resolve(ot.namespace)
    val fullScalaName =
      if (namespace.isRoot)
        s"${genOpts.className}.$className"
      else
        (namespace.getPath.map(getClassName) ++ Seq(className)).mkString(".")

    scribe.debug(
      s"getClassNameForObjectRefType:" +
        s" simpleName=${ot.simpleName}" +
        s" className=$className fullScalaName=$fullScalaName"
    )

    fullScalaName
  }

  private def generateForList(
      lt: ListType,
      classNamesPrefix: List[String],
      className: String
  ): Res = {
    val className2 =
      className + (if (className.endsWith("$Elm")) "" else "$Elm")
    val elem = generate(lt.t, classNamesPrefix, className2)
    Res(
      lt,
      scalaType = ListScalaType(elem.scalaType),
      definition = elem.definition
    )
  }

  private def generateForBasic(b: BasicType): Res = {
    Res(
      b,
      scalaType = BaseScalaType(name = b match {
        case STRING  => "java.lang.String"
        case INTEGER => "scala.Int"
        case LONG    => "scala.Long"
        case DOUBLE  => "scala.Double"
        case BOOLEAN => "scala.Boolean"
        case SIZE    => "scala.Long"
        case DURATION(_) =>
          if (genOpts.useDurations) "java.time.Duration" else "scala.Long"
      })
    )
  }

  private def generateForEnum(
      et: EnumObjectType,
      classNamesPrefix: List[String] = List.empty,
      className: String,
  ): Res = {
    scribe.debug(
      s"generateForEnum: className=$className classNamesPrefix=$classNamesPrefix"
    )
    genResults = genResults.copy(classNames = genResults.classNames + className)

    // / Example:
    //  sealed trait FruitType
    //  object FruitType {
    //    object apple extends FruitType
    //    object banana extends FruitType
    //    object pineapple extends FruitType
    //    def $resEnum(name: java.lang.String, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): FruitType = name match {
    //      case "apple" => FruitType.apple
    //      case "banana" => FruitType.banana
    //      case "pineapple" => FruitType.pineapple
    //      case v => $tsCfgValidator.addInvalidEnumValue(path, v, "FruitType")
    //                null
    //    }
    //  }

    val resolve =
      s"""def $$resEnum(name: java.lang.String, path: java.lang.String, $$tsCfgValidator: $$TsCfgValidator): $className = name match {
         |  ${et.members
        .map(m => s"""case "$m" => $className.$m""")
        .mkString("\n  ")}
         |  case v => $$tsCfgValidator.addInvalidEnumValue(path, v, "$className")
         |            null
         |}""".stripMargin

    val str =
      s"""|sealed trait $className
         |object $className {
         |  ${et.members
        .map(m => s"object $m extends $className")
        .mkString("\n  ")}
         |  ${resolve.replaceAll("\n", "\n  ")}
         |}""".stripMargin

    val baseType = classNamesPrefix.reverse.mkString + className
    Res(et, scalaType = BaseScalaType(baseType + dbg("<X>")), definition = str)
  }
}

object ScalaGen {

  import _root_.java.io.{File, PrintWriter, FileWriter}

  import tscfg.util

  // $COVERAGE-OFF$
  def generate(
      filename: String,
      j7: Boolean = false,
      assumeAllRequired: Boolean = false,
      showOut: Boolean = false,
      s12: Boolean = false,
      useBackticks: Boolean = false
  ): GenResult = {
    val file      = new File("src/main/tscfg/" + filename)
    val source    = io.Source.fromFile(file)
    val sourceStr = source.mkString.trim
    source.close()

    if (showOut)
      println("source:\n  |" + sourceStr.replaceAll("\n", "\n  |"))

    val className = "Scala" + {
      val noPath = filename.substring(filename.lastIndexOf('/') + 1)
      val noDef  = noPath.replaceAll("""^def\.""", "")
      val symbol = noDef.substring(0, noDef.indexOf('.'))
      util.upperFirst(symbol) + "Cfg"
    }

    val buildResult =
      ModelBuilder(sourceStr, assumeAllRequired = assumeAllRequired)
    val objectType = buildResult.objectType
    if (showOut) {
      println(
        "\nobjectType:\n  |" + model.util
          .format(objectType)
          .replaceAll("\n", "\n  |")
      )
      if (buildResult.warnings.nonEmpty) {
        println("warnings:")
        buildResult.warnings.foreach(w =>
          println(s"   line ${w.line}: ${w.source}: ${w.message}")
        )
      }
    }

    val genOpts = GenOpts(
      "tscfg.example",
      className,
      j7 = j7,
      useBackticks = useBackticks,
      s12 = s12
    )

    val generator = new ScalaGen(genOpts)

    val results = generator.generate(objectType)

    val destFilename = s"src/test/scala/tscfg/example/$className.scala"
    val destFile     = new File(destFilename)
    val out          = new PrintWriter(new FileWriter(destFile), true)
    out.println(results.code)
    results
  }

  def main(args: Array[String]): Unit = {
    val filename = args(0)
    val results  = generate(filename, showOut = true)
    println(s"""classNames: ${results.classNames}
         |fields    : ${results.fields}
      """.stripMargin)
  }

  // $COVERAGE-ON$
}

private[scala] object defs {

  abstract sealed class ScalaType

  case class BaseScalaType(name: String) extends ScalaType {
    override def toString: String = name
  }

  case class ListScalaType(st: ScalaType) extends ScalaType {
    override def toString: String = s"scala.List[$st]"
  }

  case class Res(typ: Type, scalaType: ScalaType, definition: String = "")

}

private[scala] case class MethodNames() {
  val strA        = "$_str"
  val intA        = "$_int"
  val lngA        = "$_lng"
  val dblA        = "$_dbl"
  val blnA        = "$_bln"
  val durA        = "$_dur"
  val sizA        = "$_siz"
  val expE        = "$_expE"
  val listPrefix  = "$_L"
  val requireName = "$_require"

  def checkUserSymbol(symbol: String): Unit = {
    if (symbol.startsWith("$_"))
      println(
        s"""
           |WARNING: Symbol $symbol may cause conflict with generated code.
           |         Avoid the $$_ prefix in your spec's identifiers.
         """.stripMargin
      )
  }

  // definition of methods used to access list's elements of basic type
  val basicElemAccessDefinition: Map[String, String] = {
    List(strA, intA, lngA, dblA, blnA, sizA)
      .map(k => k -> scalaDef(k))
      .toMap
  }

  val expEDef: String = scalaDef(expE)

  val requireDef: String = scalaDef(requireName)
}

private[scala] case class Getter(
    genOpts: GenOpts,
    hasPath: String,
    accessors: Accessors
)(implicit val methodNames: MethodNames) {

  import defs._

  def instance(a: AnnType, res: Res, path: String)(implicit
      listAccessors: collection.mutable.LinkedHashMap[String, String]
  ): String = {

    val objRefResolution: Option[String] = a.t match {
      case ort: ObjectRefType => objectRefInstance(ort, res, path)
      case _                  => None
    }

    objRefResolution.getOrElse {
      a.t match {
        case bt: BasicType    => basicInstance(a, bt, path)
        case _: ObjectAbsType => objectInstance(a, res, path)
        case lt: ListType     => listInstance(a, lt, res, path)
      }
    }
  }

  private def objectRefInstance(
      ort: ObjectRefType,
      res: Res,
      path: String
  ): Option[String] = {
    val namespace = Namespace.resolve(ort.namespace)
    namespace.getDefine(ort.simpleName) flatMap { t =>
      t match {
        case _: EnumObjectType => Some(enumInstance(res, path))
        case _                 => None
      }
    }
  }

  private def enumInstance(res: Res, path: String): String = {
    val className = res.scalaType.toString

    // // Example:
    // fruit = FruitType.$resEnum(c.getString("fruit"), parentPath + "fruit", $tsCfgValidator)

    s"""$className.$$resEnum(c.getString("$path"), parentPath + "$path", $$tsCfgValidator)"""
  }

  private def objectInstance(a: AnnType, res: Res, path: String)(implicit
      listAccessors: collection.mutable.Map[String, String]
  ): String = {
    val className = res.scalaType.toString

    val ppArg = s""", parentPath + "$path.", $$tsCfgValidator"""

    def reqConfigCall = {
      val methodName = "$_reqConfig"
      listAccessors += methodName -> scalaDef(methodName)
      s"""$methodName(parentPath, c, "$path", $$tsCfgValidator)"""
    }

    if (genOpts.assumeAllRequired)
      s"""$className($reqConfigCall$ppArg)"""
    else if (a.optional) {
      s"""if(c.$hasPath("$path")) scala.Some($className(c.getConfig("$path")$ppArg)) else None"""
    }
    else {
      // TODO revisit #33 handling of object as always optional
      s"""$className(if(c.$hasPath("$path")) c.getConfig("$path") else com.typesafe.config.ConfigFactory.parseString("$path{}")$ppArg)"""
    }
  }

  private def listInstance(a: AnnType, lt: ListType, res: Res, path: String)(
      implicit listAccessors: collection.mutable.Map[String, String]
  ): String = {
    val scalaType: ListScalaType = res.scalaType.asInstanceOf[ListScalaType]
    val base = accessors.listMethodName(scalaType, lt, path, genOpts.s12)
    if (a.optional) {
      s"""if(c.$hasPath("$path")) scala.Some($base) else None"""
    }
    else base
  }

  private def basicInstance(a: AnnType, bt: BasicType, path: String)(implicit
      listAccessors: collection.mutable.Map[String, String]
  ): String = {
    val getter =
      tsConfigUtil.basicGetter(bt, path, genOpts.useDurations, forScala = true)

    a.default match {
      case Some(v) =>
        val value =
          tsConfigUtil.basicValue(a.t, v, useDurations = genOpts.useDurations)
        (bt, value) match {
          case (BOOLEAN, "true")  => s"""!c.$hasPath("$path") || c.$getter"""
          case (BOOLEAN, "false") => s"""c.$hasPath("$path") && c.$getter"""
          case (DURATION(qs), duration) if genOpts.useDurations =>
            s"""if(c.$hasPath("$path")) c.$getter else java.time.Duration.parse("$duration")"""
          case _ => s"""if(c.$hasPath("$path")) c.$getter else $value"""
        }

      case None if a.optional =>
        s"""if(c.$hasPath("$path")) Some(c.$getter) else None"""

      case _ =>
        bt match {
          case DURATION(_) => s"""c.$getter"""
          case _ =>
            val (methodName, methodCall) =
              tsConfigUtil.basicRequiredGetter(bt, path, genOpts.useDurations)
            listAccessors += methodName -> scalaDef(methodName)
            methodCall
        }
    }
  }
}

private[scala] class Accessors {

  import defs._

  val rootListAccessors: collection.mutable.LinkedHashMap[String, String] =
    collection.mutable.LinkedHashMap()

  def listMethodName(
      scalaType: ListScalaType,
      lt: ListType,
      path: String,
      s12: Boolean
  )(implicit
      listAccessors: collection.mutable.Map[String, String],
      methodNames: MethodNames
  ): String = {

    val (_, methodName) = rec(scalaType, lt, "", s12)
    methodName + s"""(c.getList("$path"), parentPath, $$tsCfgValidator)"""
  }

  private def rec(
      lst: ListScalaType,
      lt: ListType,
      prefix: String,
      s12: Boolean
  )(implicit
      listAccessors: collection.mutable.Map[String, String],
      methodNames: MethodNames
  ): (Boolean, String) = {

    val (isBasic, elemMethodName) = lst.st match {
      case bst: BaseScalaType =>
        val basic      = lt.t.isInstanceOf[BasicType]
        val methodName = baseName(lt.t, bst.toString)
        if (basic) {
          rootListAccessors += methodName -> methodNames
            .basicElemAccessDefinition(methodName)
          rootListAccessors += methodNames.expE -> methodNames.expEDef
        }
        (basic, methodName)

      case lst: ListScalaType =>
        rec(
          lst,
          lt.t.asInstanceOf[ListType],
          prefix + methodNames.listPrefix,
          s12
        )
    }

    val (methodName, methodBody) =
      listMethodDefinition(elemMethodName, lst.st, s12, lt)

    if (isBasic)
      rootListAccessors += methodName -> methodBody
    else
      listAccessors += methodName -> methodBody

    (isBasic, methodName)
  }

  private def baseName(t: Type, name: String)(implicit
      methodNames: MethodNames
  ): String = t match {
    case STRING      => methodNames.strA
    case INTEGER     => methodNames.intA
    case LONG        => methodNames.lngA
    case DOUBLE      => methodNames.dblA
    case BOOLEAN     => methodNames.blnA
    case SIZE        => methodNames.sizA
    case DURATION(_) => methodNames.durA

    case _: ObjectAbsType => name.replace('.', '_')

    case _: ListType => throw new AssertionError()
  }

  def listMethodDefinition(
      elemMethodName: String,
      scalaType: ScalaType,
      s12: Boolean,
      lt: ListType
  )(implicit methodNames: MethodNames): (String, String) = {

    val elem = if (elemMethodName.startsWith(methodNames.listPrefix)) {
      s"$elemMethodName(cv.asInstanceOf[com.typesafe.config.ConfigList], parentPath, $$tsCfgValidator)"
    }
    else if (elemMethodName.startsWith("$")) {
      s"$elemMethodName(cv)"
    }
    else {
      val adjusted = elemMethodName.replace("_", ".")
      val objRefResolution = lt.t match {
        case ort: ObjectRefType =>
          val namespace = Namespace.resolve(ort.namespace)
          namespace.getDefine(ort.simpleName) flatMap { t =>
            t match {
              case _: EnumObjectType =>
                // TODO some more useful path (for now just "?" below)
                Some(
                  s"""$adjusted.$$resEnum(cv.unwrapped().toString, "?", $$tsCfgValidator)"""
                )
              case _ => None
            }
          }

        case _ => None
      }
      objRefResolution.getOrElse {
        s"$adjusted(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig, parentPath, $$tsCfgValidator)"
      }
    }

    val methodName = methodNames.listPrefix + elemMethodName
    val scalaCollectionConverter =
      if (s12) "scala.collection.JavaConverters._"
      else "scala.jdk.CollectionConverters._"
    val methodDef =
      s"""  private def $methodName(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $$tsCfgValidator: $$TsCfgValidator): scala.List[$scalaType] = {
         |    import $scalaCollectionConverter
         |    cl.asScala.map(cv => $elem).toList
         |  }""".stripMargin
    (methodName, methodDef)
  }
}
