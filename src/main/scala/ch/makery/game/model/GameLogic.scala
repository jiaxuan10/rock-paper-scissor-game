package ch.makery.game.entities

class GameLogic {

  def determineWinner(playerCard: Card, computerCard: Card): String = {
    if (playerCard.beats(computerCard)) "Player"
    else if (computerCard.beats(playerCard)) "Computer"
    else "Tie"
  }

  def playRound(player: Player, computer: ComputerPlayer, playerCard: Card): (Card, Card, String) = {
    val computerCard = computer.selectRandomCard
    val winner = determineWinner(playerCard, computerCard)

    // Print choices
    println(s"Player chose: $playerCard")
    println(s"Computer chose: $computerCard")

    // Adjust cards based on the winner
    winner match {
      case "Player" =>
        player.winCard(computerCard)
        computer.loseCard(computerCard)
        println("Player wins this round!")
      case "Computer" =>
        computer.winCard(playerCard)
        player.loseCard(playerCard)
        println("Computer wins this round!")
      case "Tie" =>
        player.loseCard(playerCard)
        computer.loseCard(computerCard)
        println("It's a tie! Both cards are removed.")
    }

    // Print remaining cards
    println(s"Player's remaining cards: ${player.getCardNames.mkString(", ")}")
    println(s"Computer's remaining cards: ${computer.getCardNames.mkString(", ")}")

    (playerCard, computerCard, winner)
  }
}
