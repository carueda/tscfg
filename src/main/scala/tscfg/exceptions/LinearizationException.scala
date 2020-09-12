package tscfg.exceptions

/**
  * Exception to indicate, that there is a problem when linearizing [[tscfg.Struct]]s
  *
  * @param msg   Error message
  * @param cause What caused the error
  */
final case class LinearizationException(private val msg: String = "", private val cause: Throwable = None.orNull) extends Exception(msg, cause)
