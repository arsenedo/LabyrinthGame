package classes

import interfaces.MovementDirection.Direction
import interfaces.{IMovableObject, MovementDirection}

import java.awt.Color

class Player(x: Int, y: Int) extends IMovableObject {
  val position: Position = new Position(x, y)

  override var movementDirection: Direction = MovementDirection.Stationary
  override var isMoving: Boolean = false

  mesh = Color.RED
}
