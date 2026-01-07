package classes

import interfaces.IMovableObject
import interfaces.MovementDirection.Direction

import java.awt.Color

class Bullet(x: Int, y: Int, override var movementDirection: Direction) extends IMovableObject {
  val position: Position = new Position(x, y)
  mesh = Color.yellow

  override var isMoving: Boolean = true
  override var isHit: Boolean = false
}
