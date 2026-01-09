import classes.{AudioManager, Bullet, GunWall, Player, Space, SpikedWall, Wall}
import hevs.graphics.FunGraphics
import hevs.graphics.utils.GraphicsBitmap
import `abstract`.{MovableObject, MovementDirection, StaticObject}

import java.awt.Color
import java.awt.event.{KeyEvent, KeyListener}
import javax.swing.JOptionPane

object Game extends App {
  val f = new FunGraphics(1000, 1000)

  private var frame = 0

  private var level_id = 1
  private val levels_count = 10

  private val gameGridSize = 100

  private val audioManager = new AudioManager

  private var gameArray: Array[Array[StaticObject]] = Array.empty

  private val player = new Player

  private var playingAudio = "";

  private def setupLevel(): Unit = {
    // The game contains 10 levels,
    // after completing level 10, the game ends and the end screen is shown
    if (level_id <= levels_count) {
      gameArray = LevelBuilder.ImportLevel(s"level_$level_id")

      playAudio("/assets/sound/MainMusic.wav")

      player.movementDirection = MovementDirection.Stationary
      player.isHit = false

      gameArray(1)(1).requestAssignMovableObject(player)

      drawOnce()
    } else {
      f.clear()

      f.drawPicture(f.width / 2, f.height / 2, new GraphicsBitmap("/assets/img/tear_down_wall.jpg"))

      playAudio("/assets/sound/Victory.wav")

      f.drawFancyString(250, f.height / 2, "CONGRATULATIONS!!!1!11!!1!", Color.PINK, 35)
    }
  }

  private def playAudio(newAudio: String): Unit = {
    if (newAudio != playingAudio) {
      audioManager.playAudio(newAudio)
      playingAudio = newAudio
    }
  }

  private def drawOnce(): Unit = {
    gameArray.flatten
      .filter(staticObject => !staticObject.isInstanceOf[Space])
      .foreach(wall => {
        val position = wall.position
        f.drawPicture(
          position.x * gameGridSize + 50,
          position.y * gameGridSize + 50,
          wall.mesh
        )
      })
  }

  private def draw(): Unit = {
    gameArray.flatten
      .filter(staticObject => staticObject.isInstanceOf[Space])
      .foreach(space => {
      val position = space.position
      val containedObjectOption = space.getContainedObject

      f.drawPicture(
        position.x * gameGridSize + 50,
        position.y * gameGridSize + 50,
        space.mesh
      )

      if (containedObjectOption.isDefined) {
        f.drawPicture(
          position.x * gameGridSize + 50,
          position.y * gameGridSize + 50,
          containedObjectOption.get.mesh
        )
      }
    })
  }

  private def spawnBullets(): Unit = {
    val gunWalls: Array[GunWall] = gameArray.flatten
      .filter(staticObject => staticObject.isInstanceOf[GunWall])
      .map(space => space.asInstanceOf[GunWall])

    gunWalls.foreach(gunWall => {
      val bulletOption: Option[Bullet] = gunWall.spawnBullet(frame)

      if (bulletOption.isDefined) {
        val bullet = bulletOption.get

        var gunWallPosX = gunWall.position.x
        var gunWallPosY = gunWall.position.y
        bullet.movementDirection match {
          case MovementDirection.Up => gunWallPosY -= 1
          case MovementDirection.Right => gunWallPosX += 1
          case MovementDirection.Down => gunWallPosY += 1
          case MovementDirection.Left => gunWallPosX -= 1
        }

        gameArray(gunWallPosY)(gunWallPosX).requestAssignMovableObject(bullet)
      }
    })
  }

  private def moveObjects(): Unit = {
    val spacesWithMovables = gameArray.flatten.filter(space => space.getContainedObject.isDefined)

    spacesWithMovables.foreach(space => {
      val movementDirection = Array[Int](0, 0)

      val movableObject: MovableObject = space.getContainedObject.get
      movableObject.movementDirection match {
        case MovementDirection.Up => movementDirection(1) = -1
        case MovementDirection.Down => movementDirection(1) = 1
        case MovementDirection.Left => movementDirection(0) = -1
        case MovementDirection.Right => movementDirection(0) = 1
        case MovementDirection.Stationary =>
      }

      movableObject.isMoving = true

      space.discardMovableObject()

      val newSpace: StaticObject = gameArray(space.position.y + movementDirection(1))(space.position.x + movementDirection(0))

      if (!newSpace.requestAssignMovableObject(movableObject)) {
        movableObject.movementDirection = MovementDirection.Stationary
        movableObject.isMoving = false

        if (movableObject.isInstanceOf[Player]) {
          space.requestAssignMovableObject(movableObject) // The movable object is reassigned to the old space if possible
        }
      }
    })
  }

  private def setupGameListener(): Unit = {
    val keyListener = new KeyListener {
      override def keyPressed(e: KeyEvent): Unit = {
        if (player.movementDirection != MovementDirection.Stationary) return // If the player is in any other movement state, we do not want to update his movement
        e.getKeyCode match {
          case KeyEvent.VK_UP =>
            player.movementDirection = MovementDirection.Up
            player.mesh = new GraphicsBitmap("/assets/img/PinkUp.png")
          case KeyEvent.VK_DOWN =>
            player.movementDirection = MovementDirection.Down
            player.mesh = new GraphicsBitmap("/assets/img/PinkDown.png")
          case KeyEvent.VK_LEFT =>
            player.movementDirection = MovementDirection.Left
            player.mesh = new GraphicsBitmap("/assets/img/PinkLeft.png")
          case KeyEvent.VK_RIGHT =>
            player.movementDirection = MovementDirection.Right
            player.mesh = new GraphicsBitmap("/assets/img/PinkRight.png")
          case _ =>
        }
      }

      override def keyReleased(e: KeyEvent): Unit = {
      }

      override def keyTyped(e: KeyEvent): Unit = {}
    }
    f.setKeyManager(keyListener)
  }

  private def checkGameCompletion(): Unit = {
    if (player.isHit) {
      initiateDefeat()
      return
    }

    val spaces: Array[Space] = gameArray.flatten
      .filter(staticObject => staticObject.isInstanceOf[Space])
      .map(space => space.asInstanceOf[Space])

    val clearedSpaces: Int = spaces.count(space => space.playerLanded)

    if (spaces.length == clearedSpaces) {
      initiateEndgame()
    }
  }

  private def initiateEndgame(): Unit = {
    Utilities.generateYesNoJOptionPane(
      f.mainFrame,
      "Level completed!",
      "Well done! Would you like to go to the next level?",
      JOptionPane.INFORMATION_MESSAGE,
      JOptionPane.YES_NO_OPTION,
      selectedValue => {
        if (selectedValue == JOptionPane.YES_OPTION) {
          level_id += 1
          setupLevel()
        } else {
          System.exit(0)
        }
      }
    )
  }

  private def initiateDefeat(): Unit = {
    playAudio("/assets/sound/Loose.wav")
    Utilities.generateYesNoJOptionPane(
      f.mainFrame,
      "Skill issue!",
      "Bruh you dead! Would you like to try again?",
      JOptionPane.ERROR_MESSAGE,
      JOptionPane.YES_NO_OPTION,
      selectedValue => {
        if (selectedValue == JOptionPane.YES_OPTION) {
          setupLevel()
        } else {
          System.exit(0)
        }
      }
    )
  }

  setupGameListener()

  setupLevel()

  // Level ID is updated every time the player completes a level
  // The game contains 10 levels, after level 10 the game should end
  while(true) {
    frame += 1

    if (level_id <= levels_count) {
      moveObjects()

      spawnBullets()

      draw()

      checkGameCompletion()
    }
      f.syncGameLogic(30)
  }
}
