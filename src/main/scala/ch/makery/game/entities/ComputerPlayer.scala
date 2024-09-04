package ch.makery.game.entities

import scala.util.Random

class ComputerPlayer extends Player {
  def selectRandomCard: Card = {
    val randomIndex = Random.nextInt(currentCards.length)
    currentCards(randomIndex)
  }
}
