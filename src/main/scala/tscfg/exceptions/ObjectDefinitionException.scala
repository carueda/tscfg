package tscfg.exceptions

/** Exception to indicate, that there is a problem in the definition of an
  * object
  *
  * @param msg
  *   Error message
  * @param cause
  *   What caused the error
  */
final case class ObjectDefinitionException(
    private val msg: String = "",
    private val cause: Throwable = None.orNull
) extends Exception(msg, cause)
