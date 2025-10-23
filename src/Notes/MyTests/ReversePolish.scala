package Notes.MyTests

import scala.collection.mutable

object ReversePolish {

  def isNumber(e: String): Boolean = {
    if(e == "") false
    else {
      for(c <- e.toCharArray){
        if(!c.isDigit) return false
      }
      return true
    }
  }

  def eval(rpn : String) : Int  = {
    val s = new mutable.Stack[Int]()
    for(e <- rpn.split(" ")){
      if(isNumber(e)){
        s.push(e.toInt)
      } else if (e == "+" || e == "*" || e == "/") {
        val r = s.pop()
        val l = s.pop()
        var res = {
          if(e == "+") l + r
          else if(e == "*") l * r
          else l / r
        }
        s.push(res)
      } else {
        throw new Error("HELP!")
      }
    }
    return s.pop()
  }

  // eval ("1 2 +") == 3
  // eval (1 2 + 3 4 * +") == 15
}

//ReversePolish.eval("1 2 +")