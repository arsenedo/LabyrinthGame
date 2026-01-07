package interfaces

import `abstract`.GameObject
import classes.Position

object MovementDirection extends Enumeration {
  type Direction = Value
  val Up, Right, Down, Left, Stationary = Value
}

trait IMovableObject extends GameObject{
  val position: Position
  var movementDirection: MovementDirection.Direction
  var isMoving: Boolean
  var isHit: Boolean
}