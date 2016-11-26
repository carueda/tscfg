package tscfg

abstract sealed class Key extends Ordered[Key] {
  val parent: Key
  val simple: String

  def compare(that: Key): Int = toString.compare(that.toString)

  def +(s: String) = CompositeKey(this, s)

  def parts: List[String]
}

case class SimpleKey(simple: String) extends Key {
  require(! simple.contains('.'))
  val parent: Key = Key.root
  override val toString = simple
  def parts: List[String] = List(simple)
}

case class CompositeKey(parent: Key, simple: String) extends Key {
  require(! simple.contains('.'))
  override val toString = parent.toString + "." + simple
  def parts: List[String] = parent.parts :+ simple
}

object Key {
  def apply(string: String): Key = {
    val list = string.split('.').toList
    val size = list.size
    val simple = list(size - 1)
    if (size == 1) SimpleKey(simple)
    else {
      val parent = list.slice(0, size - 1).mkString(".")
      CompositeKey(Key(parent), simple)
    }
  }

  val root = SimpleKey("/")
}
