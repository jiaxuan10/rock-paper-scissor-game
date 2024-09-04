package ch.makery.game.entities

import scala.collection.mutable.ListBuffer

class Player {
  val currentCards: ListBuffer[Card] = ListBuffer.fill(2)(Rock) ++ ListBuffer.fill(2)(Scissor) ++ ListBuffer.fill(2)(Paper)

  def selectCard(card: Card): Option[Card] = {
    if (currentCards.contains(card)) Some(card) else None
  }

  def loseCard(card: Card): Unit = {
    currentCards -= card
  }

  def winCard(card: Card): Unit = {
    currentCards += card
    arrangeCards()
  }

  def hasCards: Boolean = currentCards.nonEmpty

  protected def arrangeCards(): Unit = {
    currentCards.sortBy(_.toString).reverse
  }

  def getCardCount: Int = currentCards.length

  def getCardNames: Seq[String] = currentCards.map(_.toString)
}
