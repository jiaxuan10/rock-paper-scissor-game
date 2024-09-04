package ch.makery.game.entities

sealed trait Card {
  def beats(other: Card): Boolean
  def drawWith(other: Card): Boolean
}

case object Rock extends Card {
  override def beats(other: Card): Boolean = other == Scissor
  override def drawWith(other: Card): Boolean = other == Rock
}

case object Scissor extends Card {
  override def beats(other: Card): Boolean = other == Paper
  override def drawWith(other: Card): Boolean = other == Scissor
}

case object Paper extends Card {
  override def beats(other: Card): Boolean = other == Rock
  override def drawWith(other: Card): Boolean = other == Paper
}
