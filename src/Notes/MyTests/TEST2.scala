package Notes.MyTests

import scala.io.Source

object TEST2 {

  def f(m: Array[String]): Unit = {
    m(0) = "Hi"
    m(1) = "Everyone!"
  }

  def main(args: Array[String]): Unit = {
    val m1 = Array("Hello", "World")
    val m2 = m1
    f(m2)
    println((m1(0), m1(1)))
    println((m2(0), m2(1)))
  }
}

//TEST2.processFile("GameLogic.scala", 10)