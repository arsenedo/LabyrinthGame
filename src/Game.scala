import `abstract`.GameObject
import classes.{Bullet, GunWall, Player, Space, SpikedWall, Wall}
import hevs.graphics.FunGraphics
import interfaces.{IMovableObject, IStaticObject, MovementDirection}

import java.awt.Dimension
import java.awt.event.{KeyEvent, KeyListener}
import javax.swing.{JDialog, JOptionPane, UIManager}

object Game extends App {
  val f = new FunGraphics(1000, 1000)

  var frame = 0;

  var gameArray: Array[Array[IStaticObject]] = Array.empty

  val player = new Player(0, 0)

  def setupLevel(): Unit = {

    gameArray = LevelBuilder.ImportLevel("level_2")
    player.movementDirection = MovementDirection.Stationary
    player.isHit = false
    player.position.setPosition(1, 1)

    gameArray(player.position.y)(player.position.x).requestAssignMovableObject(player)
  }

  def draw(): Unit = {
    f.clear()

    val gameTopLeft = Array[Int](0, 0)
    val gameGridSize = 100

    for ((row, i) <- gameArray.zipWithIndex) {
      for ((space, j) <- row.zipWithIndex) {
        val containedObjectOption: Option[IMovableObject] = space.getContainedObject

        if (containedObjectOption.isDefined) {
          f.setColor(containedObjectOption.get.mesh)
        } else {
          f.setColor(space.mesh)
        }

        f.drawFillRect(
          gameTopLeft(0) + j * gameGridSize,
          gameTopLeft(1) + i * gameGridSize,
          gameGridSize,
          gameGridSize)
      }
    }
  }

  def spawnBullets(): Unit = {
    val gunWalls: Array[GunWall] = gameArray.flatten
      .filter((staticObject) => staticObject.isInstanceOf[GunWall])
      .map((space) => space.asInstanceOf[GunWall])

    gunWalls.foreach((gunWall) => {
      val bulletOption: Option[Bullet] = gunWall.spawnBullet(frame)

      if (bulletOption.isDefined) {
        val bullet = bulletOption.get

        var gunWallPosX = 7
        var gunWallPosY = 2
        bullet.movementDirection match {
          case MovementDirection.Up => gunWallPosY -= 1
          case MovementDirection.Right => gunWallPosX += 1
          case MovementDirection.Down => gunWallPosY += 1
          case MovementDirection.Left => gunWallPosX -= 1
        }

        bullet.position.setPosition(gunWallPosX, gunWallPosY)
        gameArray(gunWallPosY)(gunWallPosX).requestAssignMovableObject(bullet)
      }
    })
  }

  def movePlayer(): Unit = {
    val playerPosition = player.position
    val movementDirection = Array[Int](0, 0)

    player.movementDirection match {
      case MovementDirection.Up => movementDirection(1) = -1
      case MovementDirection.Down => movementDirection(1) = 1
      case MovementDirection.Left => movementDirection(0) = -1
      case MovementDirection.Right => movementDirection(0) = 1
      case MovementDirection.Stationary => return
    }

    player.isMoving = true
    val previousSpace: IStaticObject = gameArray(playerPosition.y)(playerPosition.x)
    previousSpace.discardMovableObject // As the player wants to get out, we discard him from the old space
    val newSpace: IStaticObject = gameArray(playerPosition.y + movementDirection(1))(playerPosition.x + movementDirection(0))

    if (newSpace.requestAssignMovableObject(player)) {
      playerPosition.y += movementDirection(1)
      playerPosition.x += movementDirection(0)
    } else {
      player.movementDirection = MovementDirection.Stationary
      player.isMoving = false
      previousSpace.requestAssignMovableObject(player) // The player is reassigned to the old space in case he can't move further
    }
  }

  def moveBullets(): Unit = {
    val bullets: Array[Bullet] = gameArray.flatten.filter((space) => {
      val containedObjectOption = space.getContainedObject

      if (containedObjectOption.isDefined) containedObjectOption.get.isInstanceOf[Bullet] else false
    }).map((space) => space.getContainedObject.get.asInstanceOf[Bullet])

    bullets.foreach((bullet) => {
      val bulletPosition = bullet.position
      val movementDirection = Array[Int](0, 0)

      bullet.movementDirection match {
        case MovementDirection.Up => movementDirection(1) = -1
        case MovementDirection.Down => movementDirection(1) = 1
        case MovementDirection.Left => movementDirection(0) = -1
        case MovementDirection.Right => movementDirection(0) = 1
      }

      bullet.isMoving = true
      val previousSpace: IStaticObject = gameArray(bulletPosition.y)(bulletPosition.x)
      previousSpace.discardMovableObject // As the player wants to get out, we discard him from the old space
      val newSpace: IStaticObject = gameArray(bulletPosition.y + movementDirection(1))(bulletPosition.x + movementDirection(0))

      if (newSpace.requestAssignMovableObject(bullet)) {
        bulletPosition.y += movementDirection(1)
        bulletPosition.x += movementDirection(0)
      } else {
        bullet.movementDirection = MovementDirection.Stationary
        bullet.isMoving = false
        // Bullet is simply destroyed
      }
    })

  }

  def setupGameListener(): Unit = {
    val keyListener = new KeyListener {
      override def keyPressed(e: KeyEvent): Unit = {
        if (player.movementDirection != MovementDirection.Stationary) return // If the player is in any other movement state, we do not want to update his movement
        e.getKeyCode match {
          case KeyEvent.VK_UP => player.movementDirection = MovementDirection.Up
          case KeyEvent.VK_DOWN => player.movementDirection = MovementDirection.Down
          case KeyEvent.VK_LEFT => player.movementDirection = MovementDirection.Left
          case KeyEvent.VK_RIGHT => player.movementDirection = MovementDirection.Right
          case default => null
        }
      }

      override def keyReleased(e: KeyEvent): Unit = {
      }

      override def keyTyped(e: KeyEvent): Unit = {}
    }
    f.setKeyManager(keyListener)
  }

  def checkGameCompletion(): Unit = {
    if (player.isHit) {
      initiateDefeat()
      return
    }

    val spaces: Array[Space] = gameArray.flatten
      .filter((staticObject) => staticObject.isInstanceOf[Space])
      .map((space) => space.asInstanceOf[Space])

    val clearedSpaces: Int = spaces.count((space) => space.playerLanded)

    if (spaces.length == clearedSpaces) {
      initiateEndgame()
    }
  }

  def initiateEndgame(): Unit = {
    Utilities.generateYesNoJOptionPane(
      f.mainFrame,
      "Level completed!",
      "Well done! Would you like to go to the next level?",
      JOptionPane.INFORMATION_MESSAGE,
      JOptionPane.YES_NO_OPTION,
      (selectedValue) => {
        if (selectedValue == JOptionPane.YES_OPTION) {
          setupLevel()
        } else {
          System.exit(0);
        }
      }
    )
  }

  def initiateDefeat(): Unit = {
    Utilities.generateYesNoJOptionPane(
      f.mainFrame,
      "Skill issue!",
      "Bruh you dead! Would you like to try again?",
      JOptionPane.ERROR_MESSAGE,
      JOptionPane.YES_NO_OPTION,
      (selectedValue) => {
        if (selectedValue == JOptionPane.YES_OPTION) {
          setupLevel()
        } else {
          System.exit(0);
        }
      }
    )
  }

  setupGameListener()

  setupLevel()
  while(true) {
    frame += 1

    movePlayer()

    moveBullets()

    spawnBullets()

    draw()

    checkGameCompletion()

    f.syncGameLogic(30)
  }
}
