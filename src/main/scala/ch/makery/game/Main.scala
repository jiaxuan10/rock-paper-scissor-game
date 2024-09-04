package ch.makery.game

import ch.makery.game.model.AudioPlayer
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.scene.Scene
import scalafx.Includes._
import javafx.{scene => jfxs}

object Main extends JFXApp {
  stage = new PrimaryStage {
    title = "Rock Paper Scissors"
  }

  showHomePage()

  def showHomePage(): Unit = {
    val resource = getClass.getResource("view/HomePage.fxml")
    if (resource == null) {
      throw new RuntimeException("HomePage.fxml not found")
    }

    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()

    val roots = loader.getRoot[jfxs.Parent]

    val homePageScene = new Scene(roots)
    stage.setScene(homePageScene)
    val musicURL = getClass.getResource("/sounds/mainPageSound.wav")
    AudioPlayer.startBackgroundMusic(musicURL)
  }

  def showGameScene(): Unit = {
    val resource = getClass.getResource("view/GamePage.fxml")
    if (resource == null) {
      throw new RuntimeException("GamePage.fxml not found")
    }

    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()

    val roots = loader.getRoot[jfxs.Parent]
    val gamePageScene = new Scene(roots)
    stage.setScene(gamePageScene)

    // Play background music when the game scene is loaded
    val musicURL = getClass.getResource("/sounds/gameMusic.wav")
    AudioPlayer.stopCurrentBackgroundMusic()
    AudioPlayer.startBackgroundMusic(musicURL)
  }

  def showHowToPlay(): Unit = {
    val resource = getClass.getResource("view/HowToPlay.fxml")
    if (resource == null) {
      throw new RuntimeException("HowToPlay.fxml not found")
    }

    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()

    val roots = loader.getRoot[jfxs.Parent]
    val howToPlayScene = new Scene(roots)
    stage.setScene(howToPlayScene)
  }
}
