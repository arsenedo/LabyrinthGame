package classes

import `abstract`.{MovableObject, MovementDirection}
import `abstract`.MovementDirection.Direction
import hevs.graphics.utils.GraphicsBitmap

import java.awt.Color

class Bullet(startingDirection: Direction) extends MovableObject {
  movementDirection = startingDirection

  override var mesh: GraphicsBitmap = _

  movementDirection match {
    case MovementDirection.Left => mesh = new GraphicsBitmap("/assets/img/FireballLeft.png")
    case MovementDirection.Up => mesh = new GraphicsBitmap("/assets/img/FireballUp.png")
    case MovementDirection.Right => mesh = new GraphicsBitmap("/assets/img/FireballRight.png")
    case MovementDirection.Down => mesh = new GraphicsBitmap("/assets/img/FireballDown.png")
    case MovementDirection.Stationary => mesh = new GraphicsBitmap("/assets/img/FireballLeft.png")
  }
}
