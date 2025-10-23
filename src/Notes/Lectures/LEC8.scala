package Notes.Lectures

class LEC8 {} //commonly used higher order functions, desugaring and folds

//! we can use seq[int] like for a function can take seq. then it can take arrays, lists vectors etc.

/*
The following are higher order functinos that are useful :)
contains, exists, forall, count
 */

object ElementExistsExample extends App { //extends app means need no main
  val fruits = List("apple", "banana", "cherry")
  val hasApple: Boolean = fruits.contains("apple") // if apple is there return true (use for finding specific elements)
  println(s"List contains 'apple': $hasApple")
}

object ForAllExample extends App {
  val numbers = List(2, 4, 6, 8)
  val allEven: Boolean = numbers.forall(_ % 2 == 0) //return true if every single element matches the predicate
  println(s"All numbers are even: $allEven")
}
object ExistsExample extends App {
  val numbers = List(1, 2, 3, 4, 5)
  val hasEven = numbers.exists(_ % 2 == 0) //return true if any single element matches the predicate
  println(s"List contains an even number: $hasEven")
}

object CountExample extends App {
  val numbers = List(1, 2, 3, 4, 5)
  val evenCount = numbers.count(_ % 2 == 0) // how many match the predicate
  println(s"Number of even numbers: $evenCount")
}

object FilterExample extends App {
  val numbers = List(1, 2, 3, 4, 5)
  val evenNumbers = numbers.filter(_ % 2 == 0) // return the list of elements that matched the predicate
  println(s"Even numbers: $evenNumbers")
}

object MapExample extends App {
  val numbers = List(1, 2, 3, 4, 5)
  val doubled = numbers.map(_ * 2)  //return a list where each element is modified according to the predicate
  println(s"Doubled numbers: $doubled")
}

object TakeWhileExample extends App {
  val numbers = List(1, 2, 3, 4, 5)
  val taken = numbers.takeWhile(_ < 4) // take elements (and put them into a new list) while predicate is true.
  println(s"Taken numbers: $taken")   // instantly return the second its not true
}

object DropWhileExample extends App {
  val numbers = List(1, 2, 3, 4, 5, 1, 7)
  val dropped = numbers.dropWhile(_ < 4) //start taking ALL elements the second predicate is false
  println(s"Dropped numbers: $dropped")
}


//extra

object ShortExample extends App {
  val words = List("apple", "banana", "cherry")
  println(words.exists(_.startsWith("b"))) // Check for words starting with 'b'
}


object GenericFunctionExample0 extends App {
  def callFunction[A](f: A => A, value: A): A = f(value)
  val multiplyByTen: Int => Int = _ * 10
  val result = callFunction(multiplyByTen, 5)
  println(result)
}
// /\YOU NEED TO MAKE CLEAR THAT THE LHS IS AN INT WHEN DOING PLACEHOLDERS\/
object GenericFunctionExample1 extends App {
  def callFunction[A](f: A => A, value: A): A = f(value)
  val result = callFunction[Int](_ * 10, 5)
  println(result)
}


/*
Map fusion:
Map X (purity and equational reasoning)
F and G are just predicates
doing List.Map(F).Map(G) is the same as doing List.Map(FandthenG) for each element
This is true as long as F and G are pure and RHS is desugared version of LHS.
If F/G impure then its different cuz LHS= G modifies Flist modifies list, RHS= modifies per element
 */


/*
For loops don't really exist in scala, the ones we use actually consist of foreach (and filter)
 */
//Simple for loop desugaring

object sugarForLoop extends App {
  var l : List[Int] = List(1, 2, 3, 4)
  var s = 0
  for(e<-l) s +=e
  println(s)
}

object desugaredForLoop extends App {
  var l: List[Int] = List(1, 2, 3, 4)
  var s = 0
  l.foreach(e => s += e) //in l, for each element e, perform s+=e (where result is stored in s)
  //returns unit tho, just side effect on s
  println(s)
}

//If within for loop desugaring, add withfilter

object sugarForLoop2 extends App {
  var l : List[Int] = List(1, 20, 30, 4)
  var s = 0
  for (e<-l; if e> 10) s += e //s only += if e>10
  println(s)
}

object desugaredForLoop2 extends App {
  var l : List[Int] = List(1, 20, 30, 4)
  var s = 0
  l.withFilter(e => e > 10).foreach(e => s += e) //if it was filter: make a new list with elements >10,
  // then for each of that list: s += e
  println(s) //but withfilter just remembers to filter when we are looping over (in the foreach), more lazy, no memory/new list
  //one pass over a list ipv two over 2 lists
}


//for yield loop desugaring: essentially for yield is used to instantly assign into the variable, so we don't need var.foreach
//just need map or withfilter or both (before we didnt NEED them as we weren't assigning to a variable)
//sv: "if in for loop"=withfilter, modifying the array=map and in normal for loop we use foreach

object desugarForYieldLoop2 extends App {
  val fruits: Vector[String] = Vector("apple", "banana", "lime", "orange")

  val capitalFruits: Vector[String] = for (e <- fruits) yield e.toUpperCase //yield just means put it into the vector
  val capitalFruitsNoSugar: Vector[String] = fruits.map(_.toUpperCase) //filter not needed as we use all elements

  val smallFruits: Vector[String] = for (e <- fruits if e.length < 6) yield e
  val smallFruitsNoSugar: Vector[String] = fruits.filter(_.length < 6) //map not needed as we don't modify anything

  val capitalSmallFruits: Vector[String] = for (e <- fruits if e.length < 6) yield e.toUpperCase
  val capitalSmallFruitsNoSugar : Vector[String] = {
    fruits.withFilter(_.length < 6).map(_.toUpperCase) //filter and map needed as we modify and discriminate
    //combines, "if in for loop"=withfilter and modifying the array=map
  }
}

/*
if you get a list of lists, you can flatten:

l = (List(List(1,2), List(3,4), List(5,6)))
l.flatten = List(1, 2, 3, 4, 5, 6)


like in getting boardpositions, using map twice gives list[list[Int]]. so we flatten then.

val allBoardPositions : Seq[Point] =
  (0 until nrRows).map(y => (0 until nrColumns).map(x => Point(x, y))).flatten

THOUGH we can also just use flatmap. usually .map().flatten --> .flatmap

val allBoardPositions : Seq[Point] =
  (0 until nrRows).flatMap(y => (0 until nrColumns).map(x => Point(x, y) ) )

though we can avoid all this by not making a 2d sequence:
val allBoardPositions: Seq[Point] =
  for(y <- 0 until nrRows; x <- 0 until nrColumns)
    yield Point(x, y)

 */

/*
foldleft takes an initial (minimum) element : A, an operator (which is a lambda) that can have two operands at a time : (B,A) =>B,
and a list of operands. then in the function it uses the operator on the operands list (and the initial)
you can sum, max, flatten, reverse, etc
 */
object Folds extends App {

  def foldLeft[A, B](init:B, operator : (B,A)=> B, l: Seq[A]) : B = {
    var res = init
    for (e <- l) res = operator(res, e)
    res
  }

  def foldRight[A, B](init:B, operator : (A,B)=> B, l: Seq[A]) : B = {
    var res = init
    for (e <- l.reverse) res = operator(e, res)
    res
  }

  def fold[A](init : A, operator : (A,A) => A, l: Seq[A]) : A = foldLeft(init,operator,l)

  def maximum(l: List[Int]): Int = fold(Int.MinValue, (x: Int, y: Int) => x max y, l)

  def sum(l: List[Int]): Int = fold(0, (x: Int, y: Int) => x + y, l)

  def reverseA[A](l: List[A]) : List[A] = foldLeft(List(), (l: List[A], e: A) => e :: l, l)

  var a = List(1,2,3)
  a = reverseA(a)
  println(a)
  var b = maximum(a)
  println(b)
  b = sum(a)
  println(b)
  println(
    foldLeft[Int,String]("z", (s : String, i : Int) => "(" + s + "+" + i.toString + ")", List(1,2,3,4,5,6,7))
  )
  //outputs: (((((((z+1)+2)+3)+4)+5)+6)+7)
  //iteration 1: "(z+1)", iteration 2: "((z+1)+2)", iteration 3: "(((z+1)+2)+3)"
  println(
    foldRight[Int,String]("z", (i : Int, s : String) => "(" + s + "+" + i.toString + ")", List(1,2,3,4,5,6,7))
  )
  println(
    foldRight[Int,String]("z", (i : Int, s : String) => "(" + i.toString + "+" + s + ")", List(1,2,3,4,5,6,7))
  )
  //result: (1+(2+(3+(4+(5+(6+(7+z)))))))
  //f = 7: "(7+z)" f = 6: "(6+(7+z))" f = 5: "(5+(6+(7+z)))"
}


/*
essentially the difference is that foldright starts with the end of the list.

You can use just "fold" as fold requirements are met (word) (fold can get you any of the three trees)
if they are met we can do operations parallel (seeword)


*/

//seeword for the rest

//filter should be called retain????
