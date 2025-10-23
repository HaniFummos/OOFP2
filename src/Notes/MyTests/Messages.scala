package Notes.MyTests

object Messages {
  def printMessage(user: String = "Tom", message: String = "Hello", importance: Int = 100): String = {
    "Message for " + user + "(importance " + importance + "):" + message
  }
}

// Messages.printMessage(user = "big", message = "hi", importance = 5)