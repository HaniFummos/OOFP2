package Notes.Lectures

class LEC5 {} //advanced functions part 1

/*
Local functions = not global functions. Can only be accessed within that function (private)
Good thing is that that local func can access variables in our functino (width and filename) without even taking it in as an
argument as so far in the main def we have width in scope
*/

object FindLongLines3 {

  def processFile(a : String) = {
    var LocalA = a
    def getImproved() = {
      LocalA = LocalA + " hi"
    }

    getImproved()

    println(LocalA)
  }

  def main(args : Array[String]) = {
    var a = args(0)
    processFile(a)
  }
}

/*
import tests.FindLongLines3
FindLongLines3.main(Array("hello"))
*/


/*
First class functions = functions are values
To make first class functions you need it set up like:
var a : type1 => type2 = function
type1= type of argument(s) it takes, type2=type of argument it returns
*/

object FunVal0 {

  def main(argv : Array[String]) = {
    def plusOne(x : Int) = x + 1 //just another way to write local functions
    def plusTwo(x : Int) = x + 2

    var increase : Int => Int = plusOne // var a : type => type = function
    println(increase(10))
    increase = plusTwo // so we can reassign increase
    println(increase(12))
  }
}

/*
Higher order functions are functions that take functions as arguments
increaseAndPrint takes a function that takes an int and returns int as its first argument,
and then prints out what that function would do with input x
*/

object FunVal1 { //all funvals should print 11 14
  def increaseAndPrint(increase : Int => Int, x : Int) = {
    println(increase(x))
  }

  def main(argv : Array[String]) = {
    def plusOne(x : Int) = x + 1
    def plusTwo(x : Int) = x + 2

    increaseAndPrint(plusOne, 10)
    increaseAndPrint(plusTwo, 12)
  }
}

/*
functions as results
Same as last code, but we use a function to get plusOne/plusTwo into increaseAndPrint
increasefunction returns a function that takes an int and returns int
 */

object FunVal3 {
  def increaseFunction(double : Boolean) : Int => Int = { //returns a function that takes an int and returns int
    def plusOne(x : Int) = x + 1
    def plusTwo(x : Int) = x + 2
    if (double) plusTwo else plusOne
  }

  def increaseAndPrint(increase : Int => Int, x : Int) = {
    println(increase(x))
  }

  def main(argv : Array[String]) = {
    // argument of function type, function as value
    increaseAndPrint(increaseFunction(false), 10)
    increaseAndPrint(increaseFunction(true), 12)
  }
}

/*
Lambdas are anonymous functions also called function literals
so instead of having:

functionA (x : int) = return x + 1 //(can also write ): int)
and then doing
var a : Int => Int = functionA

We just do var a : Int => Int = (x : int) => x+1
 */
object FunVal4 {
  def main(argv : Array[String]) = {
    var increase : Int => Int =
      (x : Int) => x + 1  // function literal, the enter is just for show
    println(increase(10))
    increase = (y : Int) => y + 2 // we can also change what function increase is ofc
    println(increase(12))
  }
}

/*
 we can directly print the function without first assigning it also...
 So  a function literal can literally just be ( (x : Int) => x + 1 )(10)
 and it can instantly call itself with 10
 */

object FunVal4_5 {
  def main(argv : Array[String]) = {
    println(((x : Int) => x + 1)(10))
    println(((x : Int) => x + 2)(12))
  }
}


//More control over functions returning functions:

object FunVal5 {
  def increaser(diff : Int) : Int => Int =
    (x : Int) => x + diff // no need to return something like "def plus(x : Int) = x + diff"

  def main(argv : Array[String]) = {
    var increase : Int => Int = increaser(1) // so we get increase = a function which = (x : Int) => x + 1
    println(increase(10))
    increase = increaser(2) // so we get (x : Int) => x + 2
    println(increase(12))
  }
}

//even more concise...:

object FunVal {

  def increaser(diff : Int) : Int => Int =
    (x : Int) => x + diff

  def main(argv : Array[String]) = {
    println(increaser(1)(10)) // we get increaser(1) = (x : Int) => x + 1 and then we give it argument of 10 so: 10 + 1
    println(increaser(2)(12))
  }
}

/*
Closure
Ex in increaser above the function value diff refers to values from outer scope, which in this situation
just so happened to be in scope.

So like the function x+diff only knows of diff because it was in scope, and now diff forever is 1 (if we captured that instance), so
at that point we have a function where diff=1 always, because it captured the environment in which it was created where we said
diff=1 and that was in scope at that time

A closure in Scala (and many other languages) is a function that captures the environment in which it was created.
It "closes over" the variables that were in scope at the time of its creation.

single, union and intersection use closure
 */

object Sets {

  type Set = Int => Boolean

  def contains(s : Set, elem : Int) = s(elem)

  val evenNumbers : Set = (x : Int) => x % 2 == 0

  def singletonSet(elem : Int):Set = (x : Int) => x == elem
  //when we do a = singletonSet(5) then we get a function where a = x == 5. so we remember elem
  //aka (x: Int) => x == elem "closes over" the variable elem from the surrounding context.

  def union(a : Set, b : Set) : Set = (x : Int) => a(x) || b(x)

  def intersection(a : Set, b : Set) : Set = (x : Int) => a(x) && b(x)

}


//placeholder syntax

object placeholder {
  def main (argv : Array[String]) = {
    val numbers = List(1, 2, 3, 4, 5)
    val doubled = numbers.map(_ * 2)

    println(doubled)  // Output: List(2, 4, 6, 8, 10)
  }
}

object a {
  // Function returning a placeholder version
  def placeholderDoubler(): Int => Int = {
    _ * 2
  }

  // Function returning a normal function version
  def normalDoubler(): Int => Int = {
    (num: Int) => num * 2
  }

  def main (argv : Array[String]) = {
    // Using both functions to create the doubled functions
    val doubled = placeholderDoubler()
    val doubledNormal = normalDoubler()

    // Example Usage
    val numbers = List(1, 2, 3, 4, 5)

    // Applying the doubling functions
    val doubledResults = numbers.map(doubled)
    val doubledNormalResults = numbers.map(doubledNormal)

    println(doubledResults)       // Output: List(2, 4, 6, 8, 10)
    println(doubledNormalResults) // Output: List(2, 4, 6, 8, 10)
  }
}


/*
Instead of:
 def singletonSet(elem : Int):Set = (x : Int) => x == elem //returns (x : Int) => x == elem
use:
def singletonSet(elem : Int):Set = _ == elem //returns _ == elem
 */