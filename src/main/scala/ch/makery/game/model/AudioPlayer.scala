package ch.makery.game.model

import java.net.URL
import javax.sound.sampled.{AudioSystem, Clip, AudioInputStream}
import scala.util.{Try, Success, Failure}

object AudioPlayer {

  private var activeBackgroundMusic: Option[Clip] = None

  /**
   * Plays a sound effect from the specified file URL.
   * @param fileUrl The URL of the sound effect file.
   */
  def playSoundEffect(fileUrl: URL): Unit = {
    Try {
      val audioStream: AudioInputStream = AudioSystem.getAudioInputStream(fileUrl)
      val soundClip: Clip = AudioSystem.getClip
      soundClip.open(audioStream)
      soundClip.start()

      // Close the clip after it finishes playing
      new Thread(new Runnable {
        def run(): Unit = {
          try {
            Thread.sleep(soundClip.getMicrosecondLength / 1000)
          } finally {
            soundClip.close()
          }
        }
      }).start()
    } match {
      case Success(_) =>
      // Effect successfully played
      case Failure(exception) =>
        println(s"Error playing sound effect: ${exception.getMessage}")
        exception.printStackTrace()
    }
  }

  /**
   * Starts playing background music from the specified file URL.
   * @param fileUrl The URL of the background music file.
   */
  def startBackgroundMusic(fileUrl: URL): Unit = {
    stopCurrentBackgroundMusic()
    Try {
      val audioStream: AudioInputStream = AudioSystem.getAudioInputStream(fileUrl)
      val musicClip: Clip = AudioSystem.getClip
      musicClip.open(audioStream)
      musicClip.start()
      musicClip.loop(Clip.LOOP_CONTINUOUSLY)

      activeBackgroundMusic = Some(musicClip)
    } match {
      case Success(_) =>
      // Background music started successfully
      case Failure(exception) =>
        println(s"Error starting background music: ${exception.getMessage}")
        exception.printStackTrace()
    }
  }

  /**
   * Stops the currently playing background music, if any.
   */
  def stopCurrentBackgroundMusic(): Unit = {
    activeBackgroundMusic.foreach { clip =>
      if (clip.isRunning) {
        clip.stop()
      }
      clip.close()
    }
    activeBackgroundMusic = None
  }
}
