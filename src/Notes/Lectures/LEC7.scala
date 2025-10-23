package Notes.Lectures

class LEC7 {} //polymorphism:

  /*
  Polymorphism = greek: many forms
  Polymorphic code:  same code works multiple types
  Goal: Code reuse

  Parametric polymorphism (A.k.a. generics): Use same code for all types (like taking any type: def function[A](thing : A) : A = {}
  Subtype polymorphism: Use different (sub)code for each type. LIKE TETROMINO AND SUBCLASSES WITH DIFFERENT FUNCTIONS

   When you interact with a Tetromino object, you can treat any of its subclasses
   (OTetromino, ITetromino, etc.) as a Tetromino type. This allows you to write code that works with the Tetromino type in a
   general way, but at runtime, the appropriate subclass implementation (e.g., rotateRight for OTetromino) will be invoked based
   on the actual instance. (!!!!and the functions themselves can be completely different)

  */

  /*
  *explanations of sub/superclasses which I already know*
  But theres also subtyping in general:

  D is subtype of A (D <: A) = D supports at least the interface  A = D can be used anywhere A is expected (substitution)
   */

  /*
  If a and B are classes:
def f(x : A) : B
Accepts any value of a subtype of A, returns a value of a subtype of B

var v : A
Can hold any value of a subtype of A
   */

  object IteratorExample {

    def main(args: Array[String]): Unit = {
      val numbers = List(1, 2, 3).iterator // val numbers is a subtype of an iterator

      while (numbers.hasNext) {
        println(numbers.next()) // Outputs 1, 2, 3 sequentially
      }
    }
  }


/*
iterators are a generic interface for looping over containers. they are needed in for loops
they are only used (lazily) when needed to loop over

!!above I have deconstructed how they work

An iterator is an object in programming that allows you to traverse or step through a collection of elements one at a
time, without exposing the underlying structure of the collection. It provides a standard interface for sequential
access to elements, and typically includes two main methods:

hasNext(): Returns true if there are more elements to iterate over, false otherwise.
next(): Returns the next element in the collection and advances the iterator to the next position.

 */


class NumberRange(var cur : Int, val end : Int, val stepSize : Int) extends Iterator[Int] {
  override def hasNext: Boolean = cur < end

  override def next(): Int = {
    val res = cur
    cur += stepSize
    res
  }
}


// see/\ Now "next" can get overridden and because of our range we manipulate what is next.
// the range can for ex be 0 until 10 by steps of 2 (end=10 stepsize=1)



/*
 iterators in Scala are single-use. Once you have iterated through all the elements,
 the iterator is exhausted and cannot be reset or reused.

 scala> val r = new NumberRange(0,10,3)
val r: NumberRange = <iterator>

scala> for(i <- r) print(i)
0369
scala> for(i <- r) print(i)
// nothing!
scala>

fix the problem (this is how normally we can iterate over arrays as much as we want):
*/

object IterableExample {

  // Defining the Iterable trait
  abstract class Iterable[A] {
    // gives a new iterator from the start !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    def iterator: Iterator[A] // Requires subclasses to implement the iterator method
  }

  // NumberRange case class extending Iterable[Int]
  case class NumberRange(start: Int, end: Int, step: Int = 1) extends Iterable[Int] { //this range is immutable
    // Provide a fresh iterator for each call to iterator()
    def iterator: Iterator[Int] =
      new NumberRangeIterator(start, end, step)
  }

  // Iterator for NumberRange
  class NumberRangeIterator(var cur: Int, val end: Int, val stepSize: Int = 1) extends Iterator[Int] {

    // Checks if there are more elements to iterate over
    override def hasNext: Boolean = cur < end

    // Returns the current element and advances the iterator
    override def next(): Int = {
      val res = cur
      cur += stepSize
      res
    }
  }

  // Simple example to demonstrate the usage of NumberRange
  def main(args: Array[String]): Unit = {
    val range = NumberRange(0, 10, 2) // Create a NumberRange from 0 to 10 with step 2

    // example using while loop to iterate manually
    val iterator = range.iterator
    while (iterator.hasNext) {
      println(s"Next value: ${iterator.next()}")
    }
  }
}

/*
Iterable vs. Iterator:
Iterable is like a collection that can provide new Iterators on demand. It allows you to iterate over
its elements multiple times because it gives a fresh iterator with each call to iterator().
Iterator, on the other hand, is a one-time use object that steps through elements and gets "exhausted" once
you've gone through all its values.

scala> val r = new NumberRange(0,10,3)
val r: NumberRange = <iterator>

scala> for(i <- r) print(i)
0369
scala> for(i <- r) print(i)
0369 // fixed!
scala>


 */

abstract class Animal extends AnyRef {
  def speak : String //abstract doesnt work
  def name : String //here too
  // default implementation
  def describe = "The " + name + " says " + speak
}

class Dog() extends Animal { // can never be final
  def speak = "Woof woof!"
  def name = "Dog"
}
class Cat()  extends Animal {
  def speak = "Miauw!!"
  def name = "Cat"
}
final class Parrot(val line : String) extends Animal {
  def speak = line
  def name = "Parrot"
  // overrides default implementation
  // override annotation optional NOPE
  override def describe = line + ", says the " + name
}
class GoldenRetriever() extends Dog() {
  override def speak : String = super.speak + " old chap!" //super goes UP 1 subclass / 1 superclass
}


/*
see describe, subclasses can just reuse functions
see how override only needed when a default implementation is already set (when its without abstract)
(abstract is supposed to say do whatever you want with the given rules?)

you can also add new defs to subclasses

override not optional when changing a function

Use A extends B only when:
A is conceptually a subtype of B
  Cat is an Animal
  Snake is a Game
  TA is an Employee
Do not use A extends B for other reasons! (for example to more easiliy access methods in A from B)

Dynamic dispatch - Always uses deepest implementation of method
Like how I cant say var a : tetromino = tetromino but can say = otetromino.
and its the same as var a : otetromino = otetromino

Final class = No subclasses possible
Final method = Cannot be overridden in subclass

SO adding final to describe clashes with the override
also doing final abstract class breaks things

goldenretriever for superclass and
subclasses can add new defs (gamebase) if they are abstract

every class in scala is a subclass of the any class. so anything can have extends any
any has many methods, like tostring. so to use a new tostring function in a class you need to override it
 */

/*
class Rational(initN : Int, initD : Int)  {
....

  override def toString : String = // override what?
    if(d == 1) n.toString
    else n.toString + "/" + d.toString

 */

/*
class Any {
  final def == (rhs: Any): Boolean = equals(rhs)
  final def != (rhs: Any): Boolean = !==(rhs)

  def equals(rhs : Any)
  def toString(rhs : Any) = ...
}

It seems == and != cant be overridden lol
 */


//NEW AD HOC POLYMORPHISM

object Overloading { //same def name but we choose the one based on the static type (static = : int)

  def max(l : List[Int]) : Int = l.max
  def max(a : Int, b : Int)  : Int= if(a>b) a else b

  def max(l : Set[Int])  : Int = l.max
  def max(a : Any) : Int = throw new Error("No max for any")
}

/*
in contrast to subtype polymorphism, which max we get depends on the static type.
So doing X : Any = List(1,2,3) unlike of subtype poly, we dont look at deepest implementation of the method but at the given static type
So in the object they wrongly choose the any type  ipv list


you cant override a final definition like == but you can overload it:

class MyClass(val value: Int) {
  // Final method from Any, can't be overridden
  def ==(other: Any): Boolean = ... // ERROR

  // Overloading the == method
  def ==(other: MyClass): Boolean = this.value == other.value //works
}

The == method from Any (the root of Scala's class hierarchy) is final, meaning it cannot be overridden.

However, overloading refers to defining multiple methods with the same name but different parameter types or numbers,
so we can add a method def ==(other: MyClass) without interfering with the inherited == method from Any.
The overloaded method compares two MyClass instances based on their value, while the inherited == from Any will still
handle other comparisons, like comparing with a string.

We can see that according to the docent code above where any can do all things and other defs with the same name can do for
specific

 */



/*
Method dispatch methods

Dynamic dispatch
Select dynamically (at run time) from the type of the object
Always use deepest implementation
Only in Scala, Java
Behavior of virtual methods in C#/C++

Static dispatch
Select statically (at compile time) from the type of the variable.
Cheaper, no runtime method lookup
Default behavior in C#, C++
Abstract methods must be virtual

In scala, everything is an object
Value types (subtype of AnyVal) such as Int, Char, etc cannot be null
Reference type (subtypes of AnyRef) (any class you define) can be null

Least upper bound or LUB is the lowest possible common ancestor in the inheritance hierarchy that all the given types
inherit from. (instead of dynamic where we look at the deepest implementation)

ex:
Animal
  |-- Mammal
  |    |-- Cat
  |    |-- Dog
  |-- Bird
lub(Cat, Dog) would be Mammal, since both Cat and Dog are subclasses of Mammal.
and lub(Cat, Bird) would be Animal, since Cat (a Mammal) and Bird share the common ancestor Animal.

List(a,b,c) : List[A]
The most specific type of List(a, b, c) will be List[A], where A is the LUB of a, b, and c.

DIA 54 (c=condition) it shows that for a condition the (static) type is the LUB of the given 2 things
which for ex can be any or of type mammal //(if(c) x : A else y : B) : lub(A,B) //(if(c) x : Int else y : String) : Any
like u can say var = if else

for tuples: lub((A,B),(C,D))  = (lub(A,C),lub(B,D))
//lub((String,Koala),(Int,Cat))
//= (lub(String,Int),lub(Koala,Cat)) //much simpler
//= (Any, Mammal)

The type of a variable is the lub of all its possible values

examples of the types working (and not):

var x : lub(A,B) = v : A
 ...
x = w : B


var x : Mammal = new Koala()
 ...
x = new Cat()

var x : Koala = new Koala()
 ...
x = new Cat() // ERROR, Cat is not a subtype of Koala



Types - instance of and casting

isInstanceOf[T]
Purpose: Checks whether the object is an instance of the specified type T.
Return Type: Returns a Boolean (true or false).

Usage:
val obj: Any = "Hello, World!"

if (obj.isInstanceOf[String]) {
    println("obj is a String") // This will be printed
} else {
    println("obj is not a String")
}


asInstanceOf[T]
Purpose: Converts (casts) the object to the specified type T.
Return Type: Returns the object as the specified type if the cast is valid.
Behavior: If the object is not of type T, it throws a ClassCastException.

Usage:
val obj: Any = "Hello, World!"

val str: String = obj.asInstanceOf[String] // This will succeed
println(str) // Outputs: Hello, World!

val number: Any = 42
// val str2: String = number.asInstanceOf[String] // This will throw ClassCastException



In most situations, casting is bad and should be avoided. (In C some casts are unavoidable)
(casts that can fail are bad, but casting between numbers (aka conversions) is ok!)
like:
val intVal: Int = 10
val doubleVal: Double = intVal.toDouble // Safe conversion from Int to Double
val anotherInt: Int = 15.5.toInt // Converts Double to Int (will truncate the decimal part)

Null is a subtype of any user defined class
The type Null has one value: null
Subtype != subclass
Since null is a subtype of every reference type, we can always use null where any reference type is required

ex:
val v : Animal = null

Nothing is a subtype of everything
Nothing has no values
Can use where any value is required
Make type setup consistent see second example
Used to signal that a function cannot return normally/anything // like in divide we dont return anything

def error(msg : String) : Nothing =
 throw new Error(msg)

def divide(x : Int, y : Int) : Int =
 if (y == 0) error("Division by zero")
 else x / y

Hierarchy Consistency: Since Nothing is a subtype of every other type in Scala, it allows for a consistent type
hierarchy. This means you can use Nothing in any situation where a type is expected, which simplifies the type system
and keeps it organized.

def process(value: Any): Unit = value match {
  case s: String => println(s"String: $s")
  case i: Int    => println(s"Integer: $i")
  case _         => println("Unknown type")
}

val x: Nothing = throw new Exception("Error!") // x can be assigned to Any, Int, String, etc.
process(x) // Works because Nothing is a subtype of Any
*/

//DIA62

/*
Suppose we want a function which gives the argument animal back (with its original type (like dog)) along with its sound:

UPPER TYPE BOUND // A <: Animal means A is a subtype of animal
def animalWithSound[A <: Animal](a: A): (A, String) = (a, a.speak)

What Happens: This approach introduces an upper type bound A <: Animal. It states that the type parameter A must be a
subtype of Animal. This ensures that whatever type you pass to the method will have the speak method available, allowing
you to call a.speak without compiler errors.
https://chatgpt.com/c/6703c806-5c10-8004-b5e4-7f9440c422e0
 */

//dia 65 66
//DIA 68