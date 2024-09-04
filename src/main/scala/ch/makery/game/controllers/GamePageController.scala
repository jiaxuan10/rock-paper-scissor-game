package ch.makery.game.controllers

import ch.makery.game.Main
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.AnchorPane
import scalafx.animation.{FadeTransition, PauseTransition}
import scalafx.util.Duration
import javafx.fxml.FXML
import javafx.scene.input.{MouseEvent => JfxMouseEvent}
import scalafxml.core.macros.sfxml
import ch.makery.game.entities.{Card, ComputerPlayer, GameLogic, Player}
import ch.makery.game.model.{AudioPlayer, CardUIHelper}
import scalafx.scene.control.Label
import scalafx.scene.paint.Color

@sfxml
class GamePageController(
                          @FXML var mainScene: AnchorPane,
                          @FXML var scissor: ImageView,
                          @FXML var paper: ImageView,
                          @FXML var rock: ImageView,
                          @FXML var playerCardsPane: AnchorPane,
                          @FXML var transitionScene: AnchorPane,
                          @FXML var computerCardsPane: AnchorPane,
                          @FXML var resultpane: AnchorPane,
                          @FXML var resultLabel: Label,
                          @FXML var playerSelectedCard: ImageView,
                          @FXML var computerSelectedCard: ImageView,
                          @FXML var resultPhase: Label,
                          @FXML var finalResultPane: AnchorPane,
                          @FXML var finalPhase: Label
                        ) {

  private val player = new Player
  private val computer = new ComputerPlayer
  private val gameLogic = new GameLogic

  private val imageViewToCard = Map(
    scissor.delegate.getId -> ch.makery.game.entities.Scissor,
    paper.delegate.getId -> ch.makery.game.entities.Paper,
    rock.delegate.getId -> ch.makery.game.entities.Rock
  )

  def handleCardPress(event: JfxMouseEvent): Unit = {
    try {
      val selectedCardImageView = event.getSource.asInstanceOf[javafx.scene.image.ImageView]
      val selectedCard = imageViewToCard.getOrElse(selectedCardImageView.getId, throw new NoSuchElementException(s"No card found for ID: ${selectedCardImageView.getId}"))

      player.selectCard(selectedCard)
      val (playerCard, computerCard, winner) = gameLogic.playRound(player, computer, selectedCard)

      showResult(playerCard, computerCard, winner)

      val selectSoundURL = Option(getClass.getResource("/sounds/cardChoose.wav")).getOrElse {
        throw new RuntimeException("Sound file for cardChoose.wav not found")
      }
      AudioPlayer.playSoundEffect(selectSoundURL)

      playerCardsPane.children.remove(selectedCardImageView)

      CardUIHelper.updateHandUI(playerCardsPane, player.currentCards, isComputer = false, handleCardPress)
      CardUIHelper.updateHandUI(computerCardsPane, computer.currentCards, isComputer = true, _ => {})

      checkGameEnd()

    } catch {
      case ex: NoSuchElementException =>
        println(s"Error: ${ex.getMessage}")
        showError("Invalid card selection. Please try again.")
      case ex: Exception =>
        println(s"Unexpected error: ${ex.getMessage}")
        showError("An unexpected error occurred. Please restart the game.")
    }
  }

  private def checkGameEnd(): Unit = {
    if (!player.hasCards && computer.hasCards) endGame("Lose")
    else if (player.hasCards && !computer.hasCards) endGame("Win")
    else if (!player.hasCards && !computer.hasCards) endGame("Draw")
  }

  def closeTransitionScene(): Unit = {
    val pause = new PauseTransition(Duration(500))
    pause.setOnFinished(_ => {
      val fadeOut = new FadeTransition {
        node = transitionScene
        duration = Duration(500)
        fromValue = 0.54
        toValue = 0.0
      }
      fadeOut.setOnFinished(_ => transitionScene.visible = false)
      fadeOut.play()
    })
    pause.play()
  }

  def backHomePage(event: JfxMouseEvent): Unit = {
    try {
      Main.showHomePage()
    } catch {
      case ex: Exception =>
        println(s"Error navigating back to the home page: ${ex.getMessage}")
        showError("Unable to return to the home page. Please restart the game.")
    }
  }

  private def showResult(playerCard: Card, computerCard: Card, winner: String): Unit = {
    try {
      resultpane.opacity = 1.0
      resultpane.visible = true

      val playerCardImagePath = s"/image/card/${playerCard.toString.toLowerCase}Card.png"
      val playerCardImage = Option(getClass.getResourceAsStream(playerCardImagePath)).getOrElse {
        throw new RuntimeException(s"Player card image not found: $playerCardImagePath")
      }
      playerSelectedCard.image = new Image(playerCardImage)

      val computerCardImagePath = s"/image/card/${computerCard.toString.toLowerCase}Card.png"
      val computerCardImage = Option(getClass.getResourceAsStream(computerCardImagePath)).getOrElse {
        throw new RuntimeException(s"Computer card image not found: $computerCardImagePath")
      }
      computerSelectedCard.image = new Image(computerCardImage)

      winner match {
        case "Player" =>
          resultLabel.text = "You Win This Round!"
          resultPhase.text = s"You gain a ${computerCard.toString} card."
        case "Computer" =>
          resultLabel.text = "You Lose This Round!"
          resultPhase.text = s"You lose a ${playerCard.toString} card."
        case "Tie" =>
          resultLabel.text = "It's a Tie!"
          resultPhase.text = s"Both sides lose a ${playerCard.toString} card."
        case _ =>
          throw new IllegalArgumentException(s"Invalid winner: $winner")
      }

      val pause = new PauseTransition(Duration(3000))
      pause.setOnFinished(_ => {
        val fadeOut = new FadeTransition {
          node = resultpane
          duration = Duration(500)
          fromValue = 1.0
          toValue = 0.0
        }
        fadeOut.setOnFinished(_ => resultpane.visible = false)
        fadeOut.play()
      })
      pause.play()

    } catch {
      case ex: RuntimeException =>
        println(s"Error showing result: ${ex.getMessage}")
        showError("Failed to display the game result. Please try again.")
      case ex: IllegalArgumentException =>
        println(s"Invalid game state: ${ex.getMessage}")
        showError("An invalid game state was encountered. Please restart the game.")
      case ex: Exception =>
        println(s"Unexpected error: ${ex.getMessage}")
        showError("An unexpected error occurred while displaying the result.")
    }
  }

  private def endGame(result: String): Unit = {
    try {
      AudioPlayer.stopCurrentBackgroundMusic()
      playerCardsPane.cursor = scalafx.scene.Cursor.Default

      val pause = new PauseTransition(Duration(3500))
      pause.setOnFinished { _ =>
        finalResultPane.visible = true
        result match {
          case "Win" =>
            val endSoundURL = Option(getClass.getResource("/sounds/winSound.wav")).getOrElse {
              throw new RuntimeException("Sound file for winSound.wav not found")
            }
            AudioPlayer.playSoundEffect(endSoundURL)
            finalPhase.text = "You Win"
            finalPhase.textFill = Color.web("#40ff00")
          case "Lose" =>
            val endSoundURL = Option(getClass.getResource("/sounds/loseSound.wav")).getOrElse {
              throw new RuntimeException("Sound file for loseSound.wav not found")
            }
            AudioPlayer.playSoundEffect(endSoundURL)
            finalPhase.text = "You Lose"
            finalPhase.textFill = Color.web("#ff0f00")
          case "Draw" =>
            val endSoundURL = Option(getClass.getResource("/sounds/tieSound.wav")).getOrElse {
              throw new RuntimeException("Sound file for tieSound.wav not found")
            }
            AudioPlayer.playSoundEffect(endSoundURL)
            finalPhase.text = "It's a Draw"
            finalPhase.textFill = Color.web("#0006ff")
          case _ =>
            throw new IllegalArgumentException(s"Invalid end game result: $result")
        }
      }
      pause.play()

    } catch {
      case ex: RuntimeException =>
        println(s"Error ending game: ${ex.getMessage}")
        showError("Failed to end the game properly. Please try again.")
      case ex: IllegalArgumentException =>
        println(s"Invalid game result: ${ex.getMessage}")
        showError("An invalid game result was encountered. Please restart the game.")
      case ex: Exception =>
        println(s"Unexpected error: ${ex.getMessage}")
        showError("An unexpected error occurred during the game ending sequence.")
    }
  }

  private def showError(message: String): Unit = {
    println(s"Error: $message")
    // Optionally, display the error message to the user using an alert or similar UI component.
  }
}
