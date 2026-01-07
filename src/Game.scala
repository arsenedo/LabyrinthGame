import `abstract`.GameObject
import classes.{Player, Space, Wall}
import hevs.graphics.FunGraphics
import interfaces.{IStaticObject, MovementDirection}

import java.awt.Dimension
import java.awt.event.{KeyEvent, KeyListener}
import javax.swing.{JDialog, JOptionPane, UIManager}

object Game extends App {
  val f = new FunGraphics(1000, 1000)

  var gameArray: Array[Array[IStaticObject]] = Array.empty

  val player = new Player(0, 0)

  def setupLevel(): Unit = {
    gameArray = Array(
      Array(new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall),
      Array(new Wall,   new Space,  new Space,  new Space,  new Wall,   new Space,  new Space,  new Wall,   new Space,  new Wall),
      Array(new Wall,   new Space,  new Space,  new Space,  new Space,  new Space,  new Space,  new Wall,   new Space,  new Wall),
      Array(new Wall,   new Space,  new Space,  new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Space,  new Wall),
      Array(new Wall,   new Space,  new Space,  new Wall,   new Space,  new Space,  new Space,  new Space,  new Space,  new Wall),
      Array(new Wall,   new Space,  new Space,  new Wall,   new Space,  new Wall,   new Space,  new Wall,   new Space,  new Wall),
      Array(new Wall,   new Space,  new Space,  new Wall,   new Wall,   new Wall,   new Space,  new Wall,   new Space,  new Wall),
      Array(new Wall,   new Space,  new Space,  new Space,  new Space,  new Space,  new Space,  new Wall,   new Space,  new Wall),
      Array(new Wall,   new Wall,   new Space,  new Space,  new Space,  new Space,  new Space,  new Wall,   new Space,  new Wall),
      Array(new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall),
    )

    player.movementDirection = MovementDirection.Stationary
    player.position.setPosition(1, 1)

    gameArray(player.position.y)(player.position.x).requestAssignMovableObject(player)
  }

  def draw(): Unit = {
    f.clear()

    val gameTopLeft = Array[Int](0, 0)
    val gameGridSize = 100

    for ((row, i) <- gameArray.zipWithIndex) {
      for ((space, j) <- row.zipWithIndex) {
        f.setColor(space.mesh)
        f.drawFillRect(
          gameTopLeft(0) + j * gameGridSize,
          gameTopLeft(1) + i * gameGridSize,
          gameGridSize,
          gameGridSize)
      }
    }

    f.setColor(player.mesh)
    f.drawFillRect(
      gameTopLeft(0) + player.position.x * gameGridSize,
      gameTopLeft(1) + player.position.y * gameGridSize,
      gameGridSize,
      gameGridSize)
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

    if (newSpace.isLandingAllowed) {
      newSpace.requestAssignMovableObject(player)
      playerPosition.y += movementDirection(1)
      playerPosition.x += movementDirection(0)
    } else {
      player.movementDirection = MovementDirection.Stationary
      player.isMoving = false
      previousSpace.requestAssignMovableObject(player) // The player is reassigned to the old space in case he can't move further
    }
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
    val spaces: Array[Space] = gameArray.flatten
      .filter((staticObject) => staticObject.isInstanceOf[Space])
      .map((space) => space.asInstanceOf[Space])

    val clearedSpaces: Int = spaces.count((space) => space.playerLanded)

    if (spaces.length == clearedSpaces) {
      initiateEndgame()
    }
  }

  def initiateEndgame(): Unit = {
    val optionPane: JOptionPane = new JOptionPane("Well done! Would you like to go to the next level?", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION)
    val dialog: JDialog = optionPane.createDialog(f.mainFrame, "Level completed!");
    dialog.setVisible(true)

    val selectedValue: Object = optionPane.getValue();

    if (selectedValue == JOptionPane.YES_OPTION) {
      setupLevel()
    } else {
      System.exit(0);
    }
  }

  setupGameListener()

  setupLevel()
  while(true) {
    movePlayer()
    draw()

    checkGameCompletion()

    f.syncGameLogic(20)
  }
}
