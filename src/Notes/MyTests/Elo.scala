package Notes.MyTests

object Elo {
  val eloK = 24

  def updateEloScores(players: List[Player], games: List[Game]): Unit = {
    var eloBChange = 0.0

    for (i <- games.indices) {
      val probBWins = 1 / (1 + Math.pow(10, (games(i).playerA.rating - games(i).playerB.rating) / 400.0))
      eloBChange = eloK * (games(i).outcome - probBWins)
      println(eloBChange)
      games(i).playerB.addEloChange(eloBChange)
      games(i).playerA.addEloChange(-eloBChange)
    }

    for (i <- players.indices) {
      players(i).rating += players(i).getTotalEloChange
    }

  }

  class Player(val name: String, var rating: Double) {
    var totalEloChange: Double = 0.0
      def addEloChange(eloChange: Double): Unit = {
        totalEloChange += eloChange
      }
      def getTotalEloChange: Double = {
        return totalEloChange
      }
  }

  class Game(val playerA: Player, val playerB: Player, val outcome: Double) {
  } // player A went against B and A won/lost/tied we also know both player's ratings
}

/*
import tests.Elo
import tests.Elo.Player
import tests.Elo.Game

val jaap = new Player("Jaap",2000)
val piet = new Player("Piet", 2200)
val game = new Game(jaap,piet, 0.0)  //Jaap won
val game1 = new Game(jaap,piet, 0.0)  //Jaap won

Elo.updateEloScores(List(jaap,piet), List(game,game1))
jaap.rating

*/



// jaap.name
//jaap.rating