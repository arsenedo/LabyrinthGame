import `abstract`.GameObject
import classes.{Player, Space, Wall}
import hevs.graphics.FunGraphics

object Game extends App {
  val f = new FunGraphics(1000, 1000)

  val gameArray: Array[Array[GameObject]] = Array(
    Array(new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall),
    Array(new Wall,   new Space,  new Space,  new Space,  new Wall,   new Space,  new Space,  new Wall,   new Space,  new Wall),
    Array(new Wall,   new Space,  new Space,  new Space,  new Space,  new Space,  new Space,  new Wall,   new Space,  new Wall),
    Array(new Wall,   new Space,  new Space,  new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Space,  new Wall),
    Array(new Wall,   new Space,  new Space,  new Wall,   new Space,  new Space,  new Space,  new Space,  new Space,  new Wall),
    Array(new Wall,   new Space,  new Space,  new Wall,   new Space,  new Wall,   new Space,  new Wall,   new Space,  new Wall),
    Array(new Wall,   new Space,  new Space,  new Wall,   new Wall,   new Wall,   new Space,  new Wall,   new Space,  new Wall),
    Array(new Wall,   new Space,  new Space,  new Space,  new Space,  new Space,  new Space,  new Wall,   new Space,  new Wall),
    Array(new Wall,   new Wall,   new Space,  new Space,  new Space,  new Space,  new Space,  new Wall,   new Space,  new Wall),
    Array(new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall,   new Wall),
  )

  val player = new Player(1, 1)

  def draw(): Unit = {
    val gameTopLeft = Array[Int](250, 250)
    val gameGridSize = 50

    for ((row, i) <- gameArray.zipWithIndex) {
      for ((space, j) <- row.zipWithIndex) {
        f.setColor(space.mesh)
        f.drawFillRect(
          gameTopLeft(0) + j * gameGridSize,
          gameTopLeft(1) + i * gameGridSize,
          gameGridSize,
          gameGridSize)
      }
    }

    f.setColor(player.mesh)
    f.drawFillRect(
      gameTopLeft(0) + player.position.x * gameGridSize,
      gameTopLeft(1) + player.position.y * gameGridSize,
      gameGridSize,
      gameGridSize)
  }

  draw()
}
