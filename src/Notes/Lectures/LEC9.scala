package Notes.Lectures

class LEC9 {} //Pattern matching

//SEE PMI FOR DEEP PATTERN MATCHING. SEE PMI0 ALSO AND UNDERSTAND ALL

/*
Match vs. case(c++):

match is an expression (like if)
Match can match deeply
Match can match case classes
Match can bind variables
No fallthrough or break statement
 */

/*
Patterns must be linear: each variable occurs only once

So to calculate e + e => 2 * e, we can't do:

  def simplifyTop(expr: Expr): Expr = expr match {
    case BinOp(e, "+", e) => BinOp(Number(2), "*", e) //fail bc e occurs twice
    case _ => expr
  }

We can use pattern guards:
case p if c
so this case only used if p ofc, but also if c is true

but can do:

def simplifyTop(expr : Expr) : Expr = expr match {
   case BinOp(l , "+",r) if l == r 	=> BinOp(Number(2),"*",l) //here the if ensures that l and r are the same
   case _                         	 	=> expr
 }
 */



/*
Types of patterns - wildcards/dontcares (_)

def describe(expr : Expr) : String = expr match {
 case Var(_) => "A variable"                         //if of type var
 case Number(_) => "A number"
 case UnOp(_,_) => "A unary operator"
 case BinOp(_,_,_) => "A binary operator"
}
val expr1 = Var("x")
val expr2 = Number(42)
val expr3 = UnOp("-", expr2) // -42
val expr4 = BinOp("+", expr1, expr2)

types of patterns - constants (eaaasy)

def describe(x: Any) = x match {
 case 5 => "five"
 case true => "truth"
 case "hello" => "hi!"
 case Nil => "the empty list"
 case _ => "something else"
}

Types of patterns - variables

expr match {
 case 0 => "zero"
 case somethingElse => "not zero: " + somethingElse //now somethinElse is bound to expr
}

Types of patterns - Constructors = see first part

Types of patterns - list patterns

expr match {
 case List(_,_) => "A list with 2 elements"
 case List(_,_,3) => "A list with 3 elements ending in 3"
 case List(1,_*) => "A list with more than one element starting with 1" // _* means from here on list can be whatever
 case List(_*) => "Some list"
}

poggies

Types of patterns - tuples

def tupleDemo(expr: Any) =
 expr match {
   case (a, b, c) => println("matched " + a + b + c) //three elements. also assigns variables
   case _ =>
 }

Can use any pattern after val

val tuple = ("x",2,1.0)
val (a,b,_) = tuple // This will match the tuple and assign values to a, b, and discard the third element
val (_,_,_) = tuple //similar
//crashes if not true. a and b are just random ahh vars

Most useful for tuples. Match fail => entire program crash
 */

object hi extends App {
  def describe(x: Any) = x match { //the any does it
    case 5 => "five"
    case true => "truth"
    case "hello" => "hi!"
    case Nil => "the empty list"
    case _ => "something else"
  }

  var a = 5
  println(describe(a))

  val tuple = ("x",2,1.0)
  val (_,c,_) = tuple //crashes if not true, you see that c does nothigng
}

/*
Sealed class = no extension (subclasses) of class possible outside of current file

sealed abstract class Expr
case class Var(name : String) extends Expr
case class Number(num : Double) extends Expr
case class UnOp(operator : String, arg : Expr) extends Expr
case class BinOp(lhs : Expr, operator : String, rhs : Expr) extends Expr

//these are all the subclasses, and there may not be any more outside of this file


ohhhh coool:
If Expr is not sealed, describe needs a default case or you will get a warning: match is not that exhaustive!

def describe(expr : Expr) : String = expr match {
 case Var(_) => "A variable"
 case Number(_) => "A number"
 case UnOp(_,_) => "A unary operator"
 case BinOp(_,_,_) => "A binary operator"
}
*/







/*
The problem with null:
-References (variables that point to objects) can potentially hold the value null. This means that you have to account
for the possibility that a reference might be null whenever you interact with it -> adds lot of cases to be handled
-No way to say that a reference cannot be null

So we need option:

val result: Option[Int] = Some(5)
val noValue: Option[Int] = None

result match {
  case Some(value) => println(s"The value is $value") //5
  case None        => println("No value present")
}


Either type is an alternative to Option where instead of None, we can pass some information of type A
(for example String). Can be used as an alternative to exceptions

Left: Represents a failure or error case.
Right: Represents a success case.

def divide(a: Int, b: Int): Either[String, Int] = {
  if (b == 0) Left("Division by zero error")
  else Right(a / b)
}

val result1 = divide(10, 2)   // Right(5)
val result2 = divide(10, 0)   // Left("Division by zero error")

println(result1)  // Output: Right(5)
println(result2)  // Output: Left(Division by zero error)

result1 match {
  case Left(error) => println(s"Error occurred: $error")
  case Right(value) => println(s"Result is: $value")
}
 */




/*
Tail recursion means that the recursive call is the last thing that happens in the method (right after the return)

def approximate(guess: Double): Double = {
   if (isGoodEnough(guess)) guess
   else approximate(improve(guess))
}

Can be as efficient as iterative (loops):
Tail recursion is fast because it allows for tail call optimization (TCO), which enables the compiler (or interpreter)
to reuse the current function's stack frame for the next recursive call, instead of creating a new one. This avoids
stack overflow errors and reduces memory usage.
-so make functions tail recursive

why tail recursion tho:
In Haskell, Scheme, Lisp, ML, F# there is no for or while loop, use tail recursion instead

tail recursive factorial better than non tail as it has a much smaller stack depth (seeword)

def factorial(n : Int) : Int = n match {
 case 0 => 1
 case _ => n * factorial(n-1) // not tail recursive because recursion is not the last thing you do. N*fac is
 }

def factorial(n : Int) : Int = {
 def facTailRec(n : Int, accum : Int) : Int = n match {
   case 0 => accum
   case n => facTailRec(n-1, accum * n) // tail recursive
 }
 facTailRec(n,1)
}
//very similar to a for loop

can also use fold to get factorial: def factorial(n : Int) =(1 until n).fold(1)(_ * _)

 */


