package tscfg

object buildWarnings {

  sealed abstract class Warning(ln: Int, src: String, msg: String = "") {
    val line: Int       = ln
    val source: String  = src
    val message: String = msg
  }

  case class MultElemListWarning(ln: Int, src: String)
      extends Warning(ln, src, "only first element will be considered")

  case class OptListElemWarning(ln: Int, src: String)
      extends Warning(ln, src, "ignoring optional mark in list's element type")

  case class DefaultListElemWarning(ln: Int, default: String, elemType: String)
      extends Warning(
        ln,
        default,
        s"ignoring default value='$default' in list's element type: $elemType"
      )
}
