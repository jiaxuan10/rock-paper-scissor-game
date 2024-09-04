package ch.makery.game.controllers

import ch.makery.game.Main
import scalafxml.core.macros.sfxml


@sfxml
class HowToPlayController {
  def backHomePage(): Unit = {
    Main.showHomePage()
  }
}
