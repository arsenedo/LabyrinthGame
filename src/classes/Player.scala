package classes

import hevs.graphics.utils.GraphicsBitmap
import `abstract`.MovableObject

import java.awt.Color

class Player() extends MovableObject {
  var mesh: GraphicsBitmap = new GraphicsBitmap("/assets/img/PinkDown.png")
}
