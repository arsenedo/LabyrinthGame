package `abstract`

import `abstract`.GameObject

abstract class MovableObject extends GameObject{
  var movementDirection: MovementDirection.Direction = MovementDirection.Stationary
  var isMoving: Boolean = false
  var isHit: Boolean = false
}

object MovementDirection extends Enumeration {
  type Direction = Value
  val Up, Right, Down, Left, Stationary = Value
}