package Notes.MyTests

class Point(x : Int, y : Int) {

  def apply(i : Int) : Int =
    if(i == 0) x else y

  override def toString: String =
    "(" + x + "," + y + ")"
}

object Point {
  val origin: Point = new Point(0, 0)
  def apply(x: Int = 0, y: Int = 0) = new Point(x, y)
}

//??