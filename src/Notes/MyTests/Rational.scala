package Notes.MyTests

import scala.annotation.tailrec

class Rat(ni : Int, val di : Int) { // case class means you don't need to put new
  private val commonFactor = Rat.gcd(ni,di) // gcd = ni % di
  val n : Int = ni / commonFactor
  val d : Int = di / commonFactor // now 4/6 will become 2/3

  def +(r : Rat) : Rat = //r = RHS also operator overloading //can do L.+(r) or L + r
    Rat(n * r.d + r.n * d, d * r.d) // formula for adding fractions

  def *(r : Rat) : Rat =
    Rat(n * r.n , d * r.d) //same

  def ==(r : Rat) : Boolean =
    n == r.n && d == r.d //same


  override def toString : String = { // so you get 30 ipv 30/1
    if(d == 1) n.toString
    else s"$n/$d"
  }
}

object Rat {

  def apply(n : Int, d : Int = 1) = new Rat(n,d) // this gets called if you call without new, also demoninator is optional (1)

  @tailrec
  def gcd(a: Int, b: Int): Int =
    if(b ==0) a else gcd(b, a % b)
}

//do
//var d = Rat(1,10)
//var e = Rat(2,10)
//d + e
//= 3/10