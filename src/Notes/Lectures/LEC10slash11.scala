package Notes.Lectures

class LEC10slash11 {} //Advanced types

/*
Static typing: Check types at compile time, without running the code

Advantages:
Rule out bugs
Better IDE support (refactoring)
Better performance (types guide compiler)
Types document functions

Disadvantages:
Steeper learning curve
Verbose
Separate compilation step
Types can be limiting (escape hatch typically present)
Little advantage of static typing for one-off script (you only run it once)

Dynamic typing: Check types at runtime, while running the code

Type inference: types can be automatically detected, reduces verbosity
In Scala:
Can infer:
types of variables
return types of (non-recursive) functions
Cannot infer:
types of arguments of (named) functions
return types of recursive functions
 */




/*
why subtyping:
Goal: Code reuse via subtype polymorphism
Common in Object oriented languages

alternative is pattern matching

 D is subtype of A (D <: A) = D supports at least the interface  A = D can be used anywhere A is expected (substitution)

 abstract class Animal {
  def speak: String
}

class Dog extends Animal {
  def speak = "Woof woof"
  def wagTail: String = "weee"
}

def demo : Unit = {
  var animal: Animal = new Dog()
  animal.wagTail()  // This line causes the error
}


SeewordIM
- In scala, everything is an object (anyref = things that are objects in java anyval = not)
-	Value types (subtype of AnyVal) such as Int, Char, etc cannot be null
-	Reference type (subtypes of AnyRef) (any class you define) can be null
-	Anyref adds only not interesting (to us) methods

We can say int has supertype anyVal and Any, but subtype nothing
We can say list has supertype anyRef and Any, but subtype null and nothing

Null is a subtype of any reference class
The type Null has one value: null
Subtype != subclass
Since null is a subtype of every reference type, we can always use null where any reference type is required
Example:
val v : Animal = null


Nothing is a subtype of everything
Nothing has no values
Can use where any value is required
Make type setup consistent,
Used to signal that a function cannot return normally

def error(msg : String) : Nothing =
 throw new Error(msg)

def divide(x : Int, y : Int) : Int =
 if (y == 0) error("Division by zero")
 else x / y
 */






/*
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

SEEWORD for drawing with root at the top and more examples ofc


For any (covariant) collection, the type of the collection is the lub of the elements
List(a : A ,b : B,c : C,d : D,) : List(lub(A,B,C,D,)) = List(lub(lub(A,B),lub(C,D))


DIA 36 (c=condition) it shows that for a condition the static type is the LUB of the given 2 things
which for ex can be any or of type mammal //(if(c) x : A else y : B) : lub(A,B) //(if(c) x : Int else y : String) : Any
like u can say var = if else

for tuples: lub((A,B),(C,D))  = (lub(A,C),lub(B,D)) //just saying its the same
//lub((String,Koala),(Int,Cat)) = (lub(String,Int),lub(Koala,Cat)) = (Any, Mammal) //much simpler

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

!!isInstanceOf[T]
Purpose: Checks whether the object is an instance of the specified type T.
Return Type: Returns a Boolean (true or false).

Usage:
val obj: Any = "Hello, World!"

if (obj.isInstanceOf[String]) {
    println("obj is a String") // This will be printed
} else {
    println("obj is not a String")
}

!!asInstanceOf[T]
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

 */






/*
Traits
A trait is like an abstract class (Cannot be instantiated)
Goal: More flexible code reuse
A class can extends multiple traits (but one class) (dog extends tail with dogular with mammal)
A trait also defines a type
(No constructor in a trait)
Alternative to “multiple inheritance” (multiple parent classes are not possible in scala)


trait Winged {
 def flySpeedKilometersPerHour : Double
 final def travelsInHours(hours : Double) =
hours * flySpeedKilometersPerHour
}

trait Tail {
 def wag : String = "weeeeee"
}

trait Sonar {
 def frequencyHertz : Double
}

trait LaysEggs {
 def eggSize : String
}

class Dolphin extends Tail with Sonar {
 def frequencyHertz: Double = 120000
 override def wag: String = “<blub>”
}

class Bat extends  Sonar with Winged {
 def frequencyHertz: Double = 200000
 def flySpeedKilometersPerHour: Double =
 100
}

class Platypus extends Tail with LaysEggs {
 def eggSize: String = "BIG"
}

//traits can hold more defs btw..., see how sonar is different in bat and dolphin. We use
multiple traits which isnt possible with just (super)classes

traits define types: (we can do this if you just see it as a supertype)

var tail : Tail = new Dolphin
tail = new Platypus
tail.wag
var sonar : Sonar = new Dolphin
sonar = new Bat
sonar.frequencyHertz
 */

/*


Diamond problems occurs whenever we have some
form of multiple inheritance.
A,B,C,D are traits
A method x in A, and is overridden by B and C (not by D)
Which method x will D have ?


trait D extends C with B    ----> x from B
trait D extends B  with C   ----> x from C
so last one ig

also seeIM, D points do B and C means D is subtype

Linearization
if cat overrides something in mammal, and we call the implementation (in cat), we take the deepest one in cat

see word im

Searched in linear order for method implementation:
Deepest first, last trait first. //LHS takes priority

first cat is deepest,then its last trait is fourlegged, then haslegs, furry animal.
scenic route though this is how it works

Why is Furry before Animal?
Superclasses of traits appear after their traits: The linearization order places the superclass or parent trait after
the traits that extend them.
So we needed to follow the mmethod for everything that is a subclass of animal (going back)

 */



//remember: subtypes can be used anywhere where a supertype is needed but supertype cant be used anywhere subtype is needed

//if A<:B ListA <: ListB (if a subtype of b) (= covariance). so you can put dog and cat into an animal list

//if you have a dog array. putting cat into dog array will only give a runtime exception in java as they are covariant
//there
//we can add cat to doglist if we use .updated(0,new cat()) and then the type of the list changes to animal

//abstract class lst[+A] should be used when using generics to make it covariant (like a list)

/*
if covariant: list of animals can = list of dogs if dog<:animal ListDog <: ListAnimal (if a subtype of b) (= covariance).
contravariant: if Animal:>dog then printer[animal] <: printer[dog] //animal :> cat but printer for animals is subtype of printer cats
so
 */

/*
Covariance = reading //=everything in java

Contravariance = writing
if A:>B then printer[A] <: printer[B]. add [-A]

Invariance = Reading & writing. array [A]

function subtyping
a function is contravariant in its arguments (read)
and covariant in its return value (write)

A => B <: C =>D
if
A :> C and B <: D //this can work with only 2 like AD=pokemon and CB = pikachu, which holds

if a is supertype of c and b is subtype of d. (more generic and more specific)
Then a goes to b is a subtype of c goes to d
sup returns sub <: sub returns sup
inputs / outputs


simple: in function inputs we want supertype returns subtype (more specific)
in outputs we want subtype returns supertype (more generic)


A => B <: C =>D
if
pikachu :> raichu and raichu <: pikachu //more generic --> more specific




so to have "LHS" = subtype of "RHS", input must be more generic, output more specific
//any is always most generic, nothing is always most specific
//so any => nothing always is subtype of LHS
//this can be used when going into a function see word

because we read AB = covariance
but we write BD = contravariance


q1 of quiz
supertype of catlike --> subtype of marsupial
because if there is already an input combo, we need any others to be either the same, or supertype --> subtype
for outputs its the opposite?

q8
we see how covariance is normal, contra wants supertype and invariant wants
 */

