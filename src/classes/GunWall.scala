package classes

import hevs.graphics.utils.GraphicsBitmap
import `abstract`.{MovableObject, StaticObject}
import `abstract`.MovementDirection

import java.awt.Color

class GunWall(x: Int, y: Int, shootDirection: MovementDirection.Direction, cooldown: Int = 30) extends StaticObject(x, y) {
  override var mesh: GraphicsBitmap = new GraphicsBitmap("/assets/img/GunWallLeft.png")

  shootDirection match {
    case MovementDirection.Up => mesh = new GraphicsBitmap("/assets/img/GunWallUp.png")
    case MovementDirection.Right => mesh = new GraphicsBitmap("/assets/img/GunWallRight.png")
    case MovementDirection.Down => mesh = new GraphicsBitmap("/assets/img/GunWallBottom.png")
    case MovementDirection.Left => mesh = new GraphicsBitmap("/assets/img/GunWallLeft.png")
  }

  def spawnBullet(currentFrame: Int): Option[Bullet] = {
    if (currentFrame % cooldown == 0) Some(new Bullet(shootDirection)) else None
  }

  override def requestAssignMovableObject(IMovableObject: MovableObject): Boolean = {
    false
  }

  override def discardMovableObject(): Unit = {
    containedObject = None
  }

}


