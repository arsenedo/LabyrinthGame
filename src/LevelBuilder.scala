import `abstract`.GameObject
import classes.{Player, Space, Wall}
import hevs.graphics.FunGraphics
import interfaces.{IStaticObject, MovementDirection}

import java.awt.{Dimension, Rectangle, Shape, Color}
import java.awt.event.{KeyEvent, KeyListener, MouseEvent, MouseListener, MouseMotionListener}
import javax.swing.{JDialog, JOptionPane, UIManager}


object LevelBuilder extends App {
  private val mouse = new MouseListener {
    override def mouseClicked(e: MouseEvent): Unit = {
      println(s"mouseclick at ${e.getX} ${e.getY} ")
      AddWall(e.getX, e.getY)

    }

    override def mousePressed(e: MouseEvent): Unit = {}

    override def mouseReleased(e: MouseEvent): Unit = {}

    override def mouseEntered(e: MouseEvent): Unit = {}

    override def mouseExited(e: MouseEvent): Unit = {}
  }
  val f = new FunGraphics(1000, 1000)
  Grid()
  f.addMouseListener(mouse)

  def Grid(): Unit = {
    for(y <- 0 until 10) {
      f.drawLine(0, y * 100, 999, y * 100)
    }
      for (x <- 0 until 10){
        f.drawLine(x * 100, 0, x * 100, 999)
    }
  }
  def AddWall(x: Int, y: Int): Unit = {
    f.setColor(Color.pink)
    f.drawFillRect((x/100),(y/100),100,100)
  }

}
