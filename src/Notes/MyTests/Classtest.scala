package Notes.MyTests

class Person(private var firstName : String, lastNameArg : String, birthYearArg : Int){
  private var lastName : String = lastNameArg
  private var birthyear : Int = birthYearArg
  println("Person created")

  //def this(lastName : String, birthYear : Int) = this("Ada", lastName, birthYear)

  def info() : String = firstName + " " + lastName + "was born in " + birthyear
}

object Hello {
  def main(args: Array[String]) = {
    val p : Person = new Person("Ada", "Lovelace", 1815) //can input info but not acces
    p.info()
  }
}

//val p : Person = new Person("Bro", "doe", 2010)