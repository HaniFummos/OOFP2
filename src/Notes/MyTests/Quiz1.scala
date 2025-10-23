package Notes.MyTests

class TEST1(var x: Int) {}

object Test {
  def main(args: Array[String]): Unit = {
    val arr = new Array[TEST1](10)
    val a = new TEST1(0)
    for(i <- 0 until 10) {
      arr(i) = a
      arr(i).x += 1
    }
    var res = 0
    for(i <- 0 until 10) {
      res += arr(i).x
    }
    println(res)
  }
}

//putting class type variables in an array means that it is a pointer, so the array will update according to the class type
//variable

//this is not the case when doing val b = new TEST1(0), as we only copy the class, it's not a pointer