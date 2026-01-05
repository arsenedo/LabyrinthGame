package classes

import interfaces.IMovableObject

import java.awt.Color

class Player(x: Int, y: Int) extends IMovableObject {
  val position: Position = new Position(x, y)

  mesh = Color.RED
}
