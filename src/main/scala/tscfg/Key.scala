package tscfg

/**
  * Created by carueda on 1/7/16.
  */
abstract class Key extends Ordered[Key] {
  val parent: Key
  val simple: String

  def compare(that: Key): Int = toString.compare(that.toString)
}

case class SimpleKey(simple: String) extends Key {
  val parent: Key = Key.root
  override val toString = simple
}

case class CompositeKey(parent: Key, simple: String) extends Key {
  override val toString = parent + "." + simple
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

object KeyTest {
  def main(args: Array[String]): Unit = {
    assert(Key("/") == Key.root)
    assert(Key("abc.edf").toString == "abc.edf")
    assert(Key("abc.edf").parent.toString == "abc")
    assert(Key("abc").parent.toString == "/")
  }
}
