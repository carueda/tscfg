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
    def `type` = "String"
  }

  case class GetString()(implicit genOpts: GenOpts) extends StringAccessor {
    def instance(path: String) = s"""c.getString("$path")"""
  }
  case class GetOptString()(implicit genOpts: GenOpts) extends StringAccessor with HasPath {
    override def `type` = "Option[String]"
    def instance(path: String) = s"""if(c.$hasPath("$path")) Some(c.getString("$path")) else None"""
  }
  case class GetStringOr(value: String)(implicit genOpts: GenOpts) extends StringAccessor with HasPath {
    def instance(path: String) = s"""if(c.$hasPath("$path")) c.getString("$path") else $value"""
  }

  case class GetInt()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "Int"
    def instance(path: String) = s"""c.getInt("$path")"""
  }
  case class GetOptInt()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Option[Int]"
    def instance(path: String) = s"""if(c.$hasPath("$path")) Some(c.getInt("$path")) else None"""
  }
  case class GetIntOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Int"
    def instance(path: String) = s"""if(c.$hasPath("$path")) c.getInt("$path") else $value"""
  }

  case class GetLong()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "Long"
    def instance(path: String) = s"""c.getLong("$path")"""
  }
  case class GetOptLong()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Option[Long]"
    def instance(path: String) = s"""if(c.$hasPath("$path")) Some(c.getLong("$path")) else None"""
  }
  case class GetLongOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Long"
    def instance(path: String) = s"""if(c.$hasPath("$path")) c.getLong("$path") else $value"""
  }

  case class GetDouble()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "Double"
    def instance(path: String) = s"""c.getDouble("$path")"""
  }
  case class GetOptDouble()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Option[Double]"
    def instance(path: String) = s"""if(c.$hasPath("$path")) Some(c.getDouble("$path")) else None"""
  }
  case class GetDoubleOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Double"
    def instance(path: String) = s"""if(c.$hasPath("$path")) c.getDouble("$path") else $value"""
  }

  case class GetBoolean()(implicit genOpts: GenOpts) extends Accessor {
    def `type` = "Boolean"
    def instance(path: String) = s"""c.getBoolean("$path")"""
  }
  case class GetOptBoolean()(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Option[Boolean]"
    def instance(path: String) = s"""if(c.$hasPath("$path")) Some(c.getBoolean("$path")) else None"""
  }
  case class GetBooleanOr(value: String)(implicit genOpts: GenOpts) extends Accessor with HasPath {
    def `type` = "Boolean"
    def instance(path: String) = s"""if(c.$hasPath("$path")) c.getBoolean("$path") else $value"""
  }

  case class GetDuration(baseType: BaseType)(implicit genOpts: GenOpts) extends DurationAccessor {
    def `type` = "Long"
    def instance(path: String) = durationGetter(path, baseType)
  }
  case class GetOptDuration(baseType: BaseType)(implicit genOpts: GenOpts) extends DurationAccessor with HasPath {
    def `type` = "Option[Long]"
    def instance(path: String) = s"""if(c.$hasPath("$path")) Some(${durationGetter(path, baseType)}) else None"""
  }
  case class GetDurationOr(baseType: BaseType, value: String)(implicit genOpts: GenOpts) extends DurationAccessor with HasPath {
    def `type` = "Long"
    def instance(path: String) = s"""if(c.$hasPath("$path")) ${durationGetter(path, baseType)} else ${durationValue(value, baseType)}"""
  }

}
