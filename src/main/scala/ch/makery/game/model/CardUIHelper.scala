package ch.makery.game.model

import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.AnchorPane
import scalafx.animation.ScaleTransition
import scalafx.util.Duration
import javafx.scene.input.{MouseEvent => JfxMouseEvent}
import ch.makery.game.entities.Card

object CardUIHelper {

  def updateHandUI(cardsPane: AnchorPane, cards: Seq[Card], isComputer: Boolean, handleCardPress: JfxMouseEvent => Unit): Unit = {
    cardsPane.children.clear()

    val cardPane = new AnchorPane {
      layoutX = -7
      layoutY = -10
    }

    val maxRows = 3
    val defaultCardsPerRow = 2
    val totalCards = cards.size

    val (cardsPerRow, numberOfRows) = {
      if (totalCards <= 6) {
        (defaultCardsPerRow, Math.ceil(totalCards.toDouble / defaultCardsPerRow).toInt)
      } else if (totalCards <= 9) {
        (3, Math.min(maxRows, Math.ceil(totalCards.toDouble / defaultCardsPerRow).toInt))
      } else {
        (4, Math.min(maxRows, Math.ceil(totalCards.toDouble / defaultCardsPerRow).toInt))
      }
    }

    cards.zipWithIndex.foreach { case (card, index) =>
      val row = index / cardsPerRow
      val column = index % cardsPerRow

      if (!isComputer) {
        val imageView = new ImageView(new Image(getClass.getResourceAsStream(s"/image/card/${card.toString.toLowerCase}Card.png"))) {
          fitHeight = 116
          fitWidth = 78
          layoutX = column * 90
          layoutY = row * 130
          cursor = scalafx.scene.Cursor.Hand
          id = card.toString.toLowerCase
        }
        addMouseEffects(imageView, handleCardPress)
        cardPane.children.add(imageView)
      } else {
        val imageView = new ImageView(new Image(getClass.getResourceAsStream(s"/image/card/computerCard.png"))) {
          fitHeight = 116
          fitWidth = 78
          layoutX = column * 90
          layoutY = row * 130
          cursor = scalafx.scene.Cursor.Default
          id = card.toString.toLowerCase
        }
        cardPane.children.add(imageView)
      }
    }

    cardsPane.children.add(cardPane)
  }

  private def addMouseEffects(imageView: ImageView, handleCardPress: JfxMouseEvent => Unit): Unit = {
    imageView.onMouseEntered = (_: JfxMouseEvent) => {
      val scaleUp = new ScaleTransition {
        node = imageView // Use the ScalaFX ImageView directly
        duration = Duration(200)
        toX = 1.1
        toY = 1.1
      }
      scaleUp.play()
    }

    imageView.onMouseExited = (_: JfxMouseEvent) => {
      val scaleDown = new ScaleTransition {
        node = imageView // Use the ScalaFX ImageView directly
        duration = Duration(200)
        toX = 1.0
        toY = 1.0
      }
      scaleDown.play()
    }

    imageView.onMousePressed = (event: JfxMouseEvent) => {
      val clickEffect = new ScaleTransition {
        node = imageView // Use the ScalaFX ImageView directly
        duration = Duration(100)
        toX = 0.9
        toY = 0.9
        autoReverse = true
        cycleCount = 2
      }
      clickEffect.setOnFinished(_ => handleCardPress(event))
      clickEffect.play()
    }
  }
}
