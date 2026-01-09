package classes

import hevs.graphics.utils.GraphicsBitmap
import `abstract`.{MovableObject, StaticObject}

import java.awt.Color

class SpikedWall(x: Int, y: Int) extends StaticObject(x, y) {
  override var mesh: GraphicsBitmap = new GraphicsBitmap("/assets/img/SpikedWall.png")

  override def requestAssignMovableObject(IMovableObject: MovableObject): Boolean = {
    IMovableObject.isHit = true;

    false
  }
}
