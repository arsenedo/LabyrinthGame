package classes

import interfaces.{IMovableObject, IStaticObject}
import interfaces.MovementDirection

import java.awt.Color

class GunWall(shootDirection: MovementDirection.Direction, cooldown: Int = 60) extends IStaticObject {
  mesh = Color.orange

  def spawnBullet(currentFrame: Int): Option[Bullet] = {
    if (currentFrame % cooldown == 0) Some(new Bullet(0, 0, shootDirection)) else None
  }

  override def requestAssignMovableObject(IMovableObject: IMovableObject): Boolean = {
    false
  }

  override def discardMovableObject: Unit = {
    containedObject = None
  }

}


