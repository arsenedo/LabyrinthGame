package classes

class Position(var x: Int = 0, var y: Int = 0) {
  def moveBy(dx: Int, dy: Int): Unit = {
    x += dx
    y += dy
  }

  def setPosition(newX: Int, newY: Int): Unit = {
    x = newX
    y = newY
  }
}
