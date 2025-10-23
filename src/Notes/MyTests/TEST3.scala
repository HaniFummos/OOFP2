package Notes.MyTests

import scala.io.Source

class TEST3 {
  def processFile(fileName : String, width : Int) = {
    def processLine(line : String) = {
      if (line.length > width) println(fileName + ": " + line.trim)
    }

    val source = Source.fromFile(fileName)
    for(line <- source.getLines()) processLine(line)
  }

  def main(args : Array[String]) = {
    val width = args(0).toInt
    for(arg <- args.drop(1)) processFile(arg,width)
  }

}
