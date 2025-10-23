package Notes.Lectures

class LEC6 {} //advanced functions part 2

/*
Higher order function meets function composition
1. twice[A] means that twice takes any type A (int, string). //generics
2. (f : A => A) means that we are taking a f(unction) as an argument, and it takes type a and return type a
3. ) : A => A means that we the function will return a function that takes type a and return type a
4. when we see the = we know the real function has started. To illustrate this we can also add curly braces:
def twice[A](f : A => A) : A => A = {
  (x : A) => f(f(x))
}
5. (x : A) => f(f(x)) the function itself immediately RETURNS a function that takes in x of type A (Int) and returns our function
   within out function
6. now we are back with our function which is f(f(x)) where f = x+1. so when we have x=0 f(f(0)) = f(0+1) = 1+1

So essentially to make a function within a function we had to make a function that takes any type, define f and what the
function returns, and then at the end just return something like (x+1)+1 where ()=x for outer f

Mathematically, what we just did is represented as:
(g∘f)(x)=g(f(x))
but in our case g=f

=Function Composition: a way to combine two functions where the output of one function
becomes the input of another. In programming and functional programming (like in Scala),
this is a common pattern for chaining operations.

the weird pic is supposed to illustrate that we can skip the middle man of like storing the result and can instead
just put a function in a function
 */



object Function_composition0{
  def twice[A](f : A => A) : A => A = (x : A) => f(f(x))

  def main(args : Array[String]) = {
    println(twice((x: Int) => x + 1)(0))
    println(twice((x : Int) => x * 2)(1))
  }
}

/*
same as before but with 2 unique functions
g returns C aka BOOL. just see improved version I made its ez.
 */

object Function_composition1Improved {
  def compose[A, B](g: A => B, f: A => A): A => B = //though it might be safe to do a b c
    (x: A) => g(f(x)) //first we do f(x)=plusone, then do iseven // also x always has to be type a?

  def main(args: Array[String]) = {
    def plusOne(x: Int): Int = x + 1

    def isEven(x: Int): Boolean = x % 2 == 0

    println(compose(isEven, plusOne)(1)) // == (x : Int) => (x + 1) % 2
  }
}

/*
It seems we can change the order that the arguments we got are in. like now its first Jaap of type B
and then 28 of type A. remember that the funciton only expressly changes order of arguments taken in, and doesnt
change order of the actual fucntion C or the type only or something.
*/

object FlipExample {

  def nameAgeString(name: String, age: Int): String =
    name + " is " + age + " years old"

  def flip[A, B, C](f: (A, B) => C): (B, A) => C =
    (b: B, a: A) => f(a, b) // now we take b,a and then the function is a,b. we (can?) only changed the order in which we take

  def main(args: Array[String]): Unit = {
    println(flip(nameAgeString)(28, "Jaap")) //flip takes nameagestring as an argument
  }
}


/*
You can... change variables from wider scope if you have a local function...
 */

object changeVariablesFromWiderScope {

  def main(args: Array[String]): Unit = {
    var count = 0
    def plusOne() = {
      count += 1 // this changes variable from wider scope!
    }
    plusOne()
  }
}

/*
Combining first class functions and mutability: Mutability & closures

DO NOT EVER USE

So diff = diff not diff = 1. as when diff changes increase also changes (pointer)
 */

object FunVall {

  def main(argv : Array[String]) = {

    var diff = 1
    def increase(x : Int) = x + diff // or var increase : Int => Int = (x : Int) => x + diff
    println(increase(10))
    diff = 2 //increase changes as increase = diff not increase = 1
    println(increase(12))
    diff = 3
    println(increase(10))
  }
}

//but this is not the case:

object FunVall1 {
  def increaser(diff : Int) : Int => Int =
    (x : Int) => x + diff

  def main(argv : Array[String]) = {
    var hi = 1
    var a : Int => Int = increaser(hi)
    println(a(10)) // we get increaser(1) = (x : Int) => x + 1 and then we give it argument of 10 so: 10 + 1
    hi += 1 // a doesnt change
    println(a(12))
  }
}

//also (INCLUDES WEIRD NEW LAMBDA:
object FunVall2 {

  def addNrCalls () : Int => Int = {
    var count = 0
    i => {count += 1; i + count} //returns i + count
  }

  def main(argv : Array[String]) = {
    val countFun : Int => Int  = addNrCalls()
    println(countFun(0))
    println(countFun(0))
    println(countFun(0))
    println(countFun(0))
  }
}


/*
We can nest classes, same logic as local function where we can use outer scope
 */

class Cook(val name : String, var dishesMade : Int) {

  def print(): Unit = {
    println("hi")
  }

  class SignatureDish(dishName : String) {
    def describe : String = s"$dishName ala $name"//we use name from outer class

    print() // use function from outer class

    def make() : Unit = dishesMade += 1 // we use dishesmade
  }
}


/*
Currying:

-Different way of writing functions with 2 or more arguments
-Write f(x)(y) instead of f(x,y)
-Default in functional programming languages
-Partial application: f(x) gives another function
-In Haskell/OCaml/Lean write f x y instead of f(x)(y)
 */

/*
Since printMessage2 is already a function reference, it's easier to pass around to higher-order functions
that expect a function as an argument. This can be helpful in scenarios where you need to dynamically
choose or pass around behavior.
 */

object theSame {
  def printMessage(importance: Int, message: String): String =
    s"Message (importance: $importance) L $message"

  def printMessage2: (Int, String) => String = (importance: Int, message: String) =>
    s"Message (importance: $importance) L $message"


  def main(argv : Array[String]) = {
    printMessage2(100,"Hello")

    //now partial application is possible:
    val printWarning = printMessage2(1, _)
    println(printWarning("This is a warning message"))

    //You can pass printMessage2 to higher-order functions like map:
    val messages = List("Warning", "Error", "Info")
    val result = messages.map(printMessage2(2, _))
  }
}

object Curried {
  def printMessage2 : (Int, String) => String = (importance : Int, message : String) =>
    s"Message (importance: $importance) L $message"

  def printMessage3: Int => (String => String) = (importance: Int) => (message: String) =>
    s"Message (importance: $importance) L $message"

  def main(argv : Array[String]) = {
    println(printMessage3(100)("Hello"))
    //you can apply one argument at a time:
    val print100 = printMessage3(100)
    println(print100("Hello")) // Message (importance: 100) : Hello
    println(print100("Bye"))  // Message (importance: 100) : Bye
  }
}

// now call with printMessage3("Hello")(100) instead of printMessage2("Hello",100)

/*
explanation:
printmessage 2 instantly has :, indicating everything after it is what will be returned
and that is a lambda where int and string are arguments, string is returned, and then we apply
variables and actually show the functon

printmessage 3 is similar, but each argument causes a new higher order function
like, it returns something that takes int, that returns something that takes string and returns string
then we just go on like normal but also "do => apply" for the second argument.

so now primessage3 returns a function which can have only int input, and still be a function waiting for string
or give string and int activating "both functions"

Int => (String => String) = (importance: Int) => (message: String) =>
 */

/*
here we do string first then int

as you can see we can simplify it to:
4. normal, but returns function
5. yeah
 */

object theSame2 {
  def printMessage3: String => (Int => String) = (message: String) => (importance: Int) =>
    s"Message (importance: $importance) L $message"

  def printMessage4(message : String) : Int => String = (importance : Int) =>
    s"Message (importance: $importance) L $message"

  def printMessage5(message : String) (importance : Int) =
    s"Message (importance: $importance) L $message"
}



object Curry {

  def nonCurriedSum(x : Int, y : Int) = x + y
  def curriedSum(x : Int)(y : Int) = x + y
  def alsoCurriedSum(x : Int) = (y : Int) => x + y


  def main(argv : Array[String]) = {
    val equal : Boolean = nonCurriedSum(2,3) == curriedSum(2)(3)
    val increase : Int => Int = curriedSum(2) // partialy applied function
    println(increase(3))
  }
}

// !!!!!!!!!!!! In scala can also partially applied non-curried: nonCurriedSum(2,_)

//Converting between curried and non-curried
//bring a curried function into uncuryy and vice versa...

object unCurry {
  def uncurry[A, B, C](f: A => (B => C)): (A, B) => C =
    (a: A, b: B) => f(a)(b)
  def curry[A, B, C](f: (A,B) => C): A => B => C = {
    (a: A) => (b: B) => f(a,b)
  }
}

/*
def doubleValue           =  computed each time when called ofc
val doubleValue           = computed once, when object is created, reuses value afterwards
lazy val doubleValue   =  computed once, when value is first used, reuses value afterwards

lazy means: wait with computing value until necessary

Eager evaluation:
Expression is computed as soon as it is bound to a variable (just like normal...)
Used in most programming languages

Lazy evaluation:
Does not compute things which are not necessary
Allows for infinite datastructures (infinite lists, trees etc)
Can save unnecessary work
Evaluation order hard to follow (hence always combined with pure functions only where order does not matter)
 */