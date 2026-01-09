package classes

import hevs.graphics.utils.GraphicsBitmap
import `abstract`.{MovableObject, StaticObject}

import java.awt.Color

class Wall(x: Int, y: Int) extends StaticObject(x, y) {
  override var mesh: GraphicsBitmap = new GraphicsBitmap("/assets/img/Wall.png")
}
