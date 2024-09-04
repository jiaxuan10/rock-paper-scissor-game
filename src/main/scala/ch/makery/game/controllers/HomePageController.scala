package ch.makery.game.controllers

import ch.makery.game.Main
import ch.makery.game.model.AudioPlayer
import javafx.fxml.FXML
import scalafxml.core.macros.sfxml
import javafx.event.ActionEvent

@sfxml
class HomePageController {
  @FXML
  def startGame(event: ActionEvent): Unit = {
    // Your logic here
    println("Start Game button clicked!")
    val selectSoundURL = getClass.getResource("/sounds/buttonSound.wav")
    AudioPlayer.playSoundEffect(selectSoundURL)
    Main.showGameScene()
  }

  @FXML
  def showHowToPlay(event: ActionEvent): Unit = {
    // Your logic here
    println("How To Play button clicked!")
    val selectSoundURL = getClass.getResource("/sounds/buttonSound.wav")
    AudioPlayer.playSoundEffect(selectSoundURL)
    Main.showHowToPlay()
  }
}


