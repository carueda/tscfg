package tscfg.generators

import java.util.Date

import tscfg.javaUtil._
import tscfg.generator._
import tscfg.specs._
import tscfg.specs.types._

class JavaGenerator(implicit genOpts: GenOpts) extends Generator {

  def generate(objSpec: ObjSpec): GenResult = {

    var results = GenResult()

    def genObjSpec(name: String, objSpec: ObjSpec, indent: String, isRoot: Boolean = false): Code = {
      // <class>
      val className = getClassName(name)
      results = results.copy(classNames = results.classNames + className)

      val staticStr = if (isRoot) "" else " static"
      val code = Code(name, objSpec,
        javaType = className,
        javaId = javaIdentifier(name),
        declaration = indent + "public final " + className + " " + javaIdentifier(name) + ";"
      )

      code.println(indent + s"public$staticStr class $className {")

      // generate for members:
      val orderedNames = objSpec.children.keys.toList.sorted
      val codes = orderedNames map { name =>
        gen(name, objSpec.children(name), indent + IND)
      }

      // member declarations:
      codes.map(_.declaration).filter(_.nonEmpty).foreach(code.println)

      // member definitions:
      val definitions = codes.map(_.definition).filter(_.nonEmpty)
      if (definitions.nonEmpty) {
        code.println("")
        definitions.foreach(code.print)
      }

      // <constructor>
      code.println("")
      code.println(indent + IND + s"public $className($TypesafeConfigClassName c) {")
      codes foreach { memberCode ⇒
        code.println(
          indent + IND + IND + "this." + memberCode.javaId +
            " = " + instance(memberCode.spec, memberCode.name) + ";"
        )
      }
      code.println(indent + IND + "}")
      // </constructor>

      if (isRoot && results.classNames.size > 1) {
        // define __$config:
        val configGetter = s"""
          |private static $TypesafeConfigClassName __$$config($TypesafeConfigClassName c, java.lang.String path) {
          |  return c != null && c.hasPath(path) ? c.getConfig(path) : null;
          |}""".stripMargin
        code.println(configGetter.replaceAll("\n", "\n" + indent + IND))
      }
      code.println(indent + "}")
      // </class>

      code.newClass = true
      code
    }

    def genAtomicSpec(name: String, spec: AtomicSpec, indent: String): Code = {
      val javaId = javaIdentifier(name)
      val javaType = getJavaType(spec, javaId)
      Code(name, spec,
        javaType,
        javaId = javaId,
        declaration = indent + "public final " + javaType + " " + javaId + ";")
    }

    def genListSpec(name: String, listSpec: ListSpec, indent: String): Code = {
      val elemName = getClassName(name+ "Element_")
      val elemCode = gen(elemName, listSpec.elemSpec, indent)
      val objType = toObjectType(elemCode.spec, elemName)
      val javaType = s"java.util.List<$objType>"
      val javaId = javaIdentifier(name)
      val code = Code(name, listSpec, javaType, javaId)

      if (elemCode.newClass)
        code.println(elemCode.definition)

      code.declaration = indent + "public final " + javaType + " " + javaId + ";"
      code
    }

    def gen(name: String, spec: Spec, indent: String = ""): Code = spec match {
      case spec: AtomicSpec    ⇒ genAtomicSpec(name, spec, indent)
      case spec: ObjSpec       ⇒ genObjSpec(name, spec, indent)
      case spec: ListSpec      ⇒ genListSpec(name, spec, indent)
    }

    val header = new StringBuilder()
    header.append(s"// generated by tscfg $version on ${new Date()}\n")
//    genOpts.preamble foreach { p =>
//      header.append(s"// ${p.replace("\n", "\n// ")}\n\n")
//    }
    header.append(s"package ${genOpts.packageName};\n\n")

    // main class:
    val elemSpec = genObjSpec(genOpts.className, objSpec, "", isRoot = true)

    results = results.copy(code = header.toString() + elemSpec.definition)

    results
  }

  case class Code(name: String,
                  spec: Spec,
                  javaType: String,
                  javaId: String,
                  var declaration: String = ""
                 ) {
    def println(str: String): Unit = defn.append(str).append('\n')

    def print(str: String): Unit = defn.append(str)

    def definition = defn.toString

    var newClass: Boolean = false

    private val defn = new StringBuilder()
  }

  private val IND = "    "

  private def getJavaType(spec: Spec, javaId: String): String = {
    spec.typ match {
      case atomicType: AtomicType ⇒ atomicType match {
        case STRING   ⇒ "java.lang.String"
        case INTEGER  ⇒ if (spec.isOptional) "java.lang.Integer" else "int"
        case LONG     ⇒ if (spec.isOptional) "java.lang.Long"    else "long"
        case DOUBLE   ⇒ if (spec.isOptional) "java.lang.Double"  else "double"
        case BOOLEAN  ⇒ if (spec.isOptional) "java.lang.Boolean" else "boolean"
        case DURATION ⇒ if (spec.isOptional) "java.lang.Long"    else "long"
      }
      case ObjectType  ⇒ javaId
      case ListType    ⇒ "SomeList"
    }
  }

  private def toObjectType(spec: Spec, javaId: String): String = {
    spec.typ match {
      case atomicType: AtomicType ⇒ atomicType match {
        case STRING   ⇒ "java.lang.String"
        case INTEGER  ⇒ "java.lang.Integer"
        case LONG     ⇒ "java.lang.Long"
        case DOUBLE   ⇒ "java.lang.Double"
        case BOOLEAN  ⇒ "java.lang.Boolean"
        case DURATION ⇒ "java.lang.Long"
      }
      case ObjectType  ⇒ javaId
      case ListType    ⇒ "java.util.List<SomeCLASS>"  // TODO
    }
  }

  private def instance(spec: Spec, path: String): String = {
    spec.typ match {
      case atomicType: AtomicType ⇒ atomicType match {
        case STRING   ⇒
          if (spec.defaultValue.isDefined) {
            val value = spec.defaultValue.get
            s"""c != null && c.hasPath("$path") ? c.getString("$path") : "$value""""
          }
          else if (spec.isOptional) {
            s"""c != null && c.hasPath("$path") ? c.getString("$path") : null"""
          }
          else s"""c.getString("$path")"""

        case INTEGER  ⇒
          if (spec.defaultValue.isDefined) {
            val value = spec.defaultValue.get
            s"""c != null && c.hasPath("$path") ? c.getInt("$path") : $value"""
          }
          else if (spec.isOptional) {
            s"""c != null && c.hasPath("$path") ? c.getInt("$path") : null"""
          }
          else s"""c.getInt("$path")"""

        case LONG     ⇒
          if (spec.defaultValue.isDefined) {
            val value = spec.defaultValue.get
            s"""c != null && c.hasPath("$path") ? c.getLong("$path") : $value"""
          }
          else if (spec.isOptional) {
            s"""c != null && c.hasPath("$path") ? c.getLong("$path") : null"""
          }
          else s"""c.getLong("$path")"""

        case DOUBLE   ⇒
          if (spec.defaultValue.isDefined) {
            val value = spec.defaultValue.get
            s"""c != null && c.hasPath("$path") ? c.getDouble("$path") : $value"""
          }
          else if (spec.isOptional) {
            s"""c != null && c.hasPath("$path") ? c.getDouble("$path") : null"""
          }
          else s"""c.getDouble("$path")"""

        case BOOLEAN  ⇒
          if (spec.defaultValue.isDefined) {
            val value = spec.defaultValue.get
            s"""c != null && c.hasPath("$path") ? c.getBoolean("$path") : $value"""
          }
          else if (spec.isOptional) {
            s"""c != null && c.hasPath("$path") ? c.getBoolean("$path") : null"""
          }
          else s"""c.getBoolean("$path")"""


        case DURATION ⇒ s"""TODO_getDuration("$path")"""
      }

      case ObjectType  ⇒
        s"""new ${getClassName(path)}(__$$config(c, "$path"))"""

      case ListType    ⇒
        s"""TODO_getListType("$path")"""
    }
  }
}
