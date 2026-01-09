package classes

import hevs.graphics.utils.GraphicsBitmap
import `abstract`.{MovableObject, StaticObject}

import java.awt.Color

class Space(x: Int, y: Int) extends StaticObject(x, y) {
  override var mesh: GraphicsBitmap = new GraphicsBitmap("/assets/img/Space.jpg")

  var playerLanded: Boolean = false;

  override def requestAssignMovableObject(IMovableObject: MovableObject): Boolean = {
    if (containedObject.isEmpty) {
      containedObject = Some(IMovableObject)

      if (IMovableObject.isInstanceOf[Player]) {
        mesh = new GraphicsBitmap("/assets/img/Path.png")
        playerLanded = true
      }
      true
    } else {
      // Collision!
      containedObject.get.isHit = true;
      IMovableObject.isHit = true;
      false
    }
  }
}
