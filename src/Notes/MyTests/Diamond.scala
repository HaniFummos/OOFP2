package Notes.MyTests

object Diamond {
  def main(arg: Int) = {
    var height = arg
    var diamond: String = "" // you can specify string
    var halfway = (height + 1) / 2 // So that the spaces align

    var r = 0 until 99

    for (i <- 1 to halfway) {
      diamond += " " * (halfway - i) + "#" * (i*2 - 1) + "\n"
    }

    if (height %2 == 0){
      diamond += "#" * (halfway * 2 - 1) + "\n"
    }

    for (i <- 1 until halfway) {
      diamond += " " * i + "#" * ((halfway - i) * 2 - 1)
      if (i+1 < halfway) {
        diamond += "\n"
      }
    }


    println(diamond)
  }
}

//Diamond.main(12)