package tscfg

import tscfg.generator.GenOpts

object ScalaAccessor {

  def apply(type_ : Type)(implicit genOpts: GenOpts): Accessor = {
    val baseType = type_.baseType
    val required = type_.required
    val value = type_.value

    baseType.base match {
      case "string"    => if (required) GetString() else if (value.isDefined) GetStringOr(value.get) else GetOptString()
      case "int"       => if (required) GetInt()    else if (value.isDefined) GetIntOr(value.get) else GetOptInt()
      case "long"      => if (required) GetLong()   else if (value.isDefined) GetLongOr(value.get) else GetOptLong()
      case "double"    => if (required) GetDouble() else if (value.isDefined) GetDoubleOr(value.get) else GetOptDouble()
      case "boolean"   => if (required) GetBoolean()else if (value.isDefined) GetBooleanOr(value.get) else GetOptBoolean()
      case "duration"  =>
        if (required) GetDuration(baseType)
        else if (value.isDefined) GetDurationOr(baseType, value.get)
        else GetOptDuration(baseType)

      case _ => throw new AssertionError()
    }
  }

  abstract class StringAccessor()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "java.lang.String"
  }

  case class GetString()(implicit genOpts: GenOpts) extends StringAccessor {
    def instance(path: String) = s"""c.get.getString("$path")"""
  }
  case class GetOptString()(implicit genOpts: GenOpts) extends StringAccessor with HasPath {
    override def `type` = "scala.Option[java.lang.String]"
    def instance(path: String) = s"""c.map(c => if(c.$hasPath("$path")) Some(c.getString("$path")) else None).get"""
  }
  case class GetStringOr(value: String)(implicit genOpts: GenOpts) extends StringAccessor with HasPath {
    def instance(path: String) = s"""c.map(c => if(c.$hasPath("$path")) c.getString("$path") else $value).get"""
  }

  case class GetInt()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "scala.Int"
    def instance(path: String) = s"""c.get.getInt("$path")"""
  }
  case class GetOptInt()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "scala.Option[scala.Int]"
    def instance(path: String) = s"""c.map(c => if(c.$hasPath("$path")) Some(c.getInt("$path")) else None).get"""
  }
  case class GetIntOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "scala.Int"
    def instance(path: String) = s"""c.map(c => if(c.$hasPath("$path")) c.getInt("$path") else $value).get"""
  }

  case class GetLong()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "scala.Long"
    def instance(path: String) = s"""c.get.getLong("$path")"""
  }
  case class GetOptLong()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "scala.Option[scala.Long]"
    def instance(path: String) = s"""c.map(c => if(c.$hasPath("$path")) Some(c.getLong("$path")) else None).get"""
  }
  case class GetLongOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "scala.Long"
    def instance(path: String) = s"""c.map(c => if(c.$hasPath("$path")) c.getLong("$path") else $value).get"""
  }

  case class GetDouble()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "scala.Double"
    def instance(path: String) = s"""c.get.getDouble("$path")"""
  }
  case class GetOptDouble()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "scala.Option[scala.Double]"
    def instance(path: String) = s"""c.map(c => if(c.$hasPath("$path")) Some(c.getDouble("$path")) else None).get"""
  }
  case class GetDoubleOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "scala.Double"
    def instance(path: String) = s"""c.map(c => if(c.$hasPath("$path")) c.getDouble("$path") else $value).get"""
  }

  case class GetBoolean()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "scala.Boolean"
    def instance(path: String) = s"""c.get.getBoolean("$path")"""
  }
  case class GetOptBoolean()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "scala.Option[scala.Boolean]"
    def instance(path: String) = s"""c.map(c => if(c.$hasPath("$path")) Some(c.getBoolean("$path")) else None).get"""
  }
  case class GetBooleanOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "scala.Boolean"
    def instance(path: String) = s"""c.map(c => if(c.$hasPath("$path")) c.getBoolean("$path") else $value).get"""
  }

  case class GetDuration(baseType: BaseType)(implicit genOpts: GenOpts) extends DurationAccessor {
    def `type` = "scala.Long"
    def instance(path: String) = s"""c.map(c => ${durationGetter(path, baseType)}).get"""
  }
  case class GetOptDuration(baseType: BaseType)(implicit genOpts: GenOpts) extends DurationAccessor with HasPath {
    def `type` = "scala.Option[scala.Long]"
    def instance(path: String) = s"""c.map(c => if(c.$hasPath("$path")) Some(${durationGetter(path, baseType)}) else None).get"""
  }
  case class GetDurationOr(baseType: BaseType, value: String)(implicit genOpts: GenOpts) extends DurationAccessor with HasPath {
    def `type` = "scala.Long"
    def instance(path: String) = s"""c.map(c => if(c.$hasPath("$path")) ${durationGetter(path, baseType)} else ${durationValue(value, baseType)}).get"""
  }

}
