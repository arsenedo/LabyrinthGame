import `abstract`.GameObject
import classes.{GunWall, Player, Space, SpikedWall, Wall}
import hevs.graphics.FunGraphics
import interfaces.{IStaticObject, MovementDirection}

import java.awt.{Color, Dimension, Rectangle, Shape}
import java.awt.event.{KeyEvent, KeyListener, MouseEvent, MouseListener, MouseMotionListener}
import java.io.{BufferedReader, FileNotFoundException, FileOutputStream, FileReader, PrintWriter}
import java.nio.file.{Files, Paths}
import javax.swing.{JDialog, JOptionPane, UIManager}


/*
    Utilisation du LevelBuilder:

    1. Sélectionner un élément key : 0-6
    2. Cliquer sur la case pour ajouter l'élément
    3. Valider la création en Appuyant sur 'y'

    Commandes:

    Space     : 0
    Wall      : 1
    Spike     : 2
    Gun_left  : 3
    Gun_right : 4
    Gun_up    : 5
    Gun_Down  : 6

*/

object LevelBuilder extends App {

  private var current_key: Int = '0'
  private var GameArray : Array[Array[Int]] = Array.ofDim(10,10)
  private val keyboard = new KeyListener {
    override def keyTyped(e: KeyEvent): Unit = {
      println(e.getKeyChar)
      if (e.getKeyChar.toInt >= '0' && e.getKeyChar.toInt <= '7')
        current_key = e.getKeyChar.toInt
      if (e.getKeyChar.toInt == 'y')
        CSVCreator(GameArray)
    }
    override def keyPressed(e: KeyEvent): Unit = {}
    override def keyReleased(e: KeyEvent): Unit = {}
  }
  private val mouse = new MouseListener {
    override def mouseClicked(e: MouseEvent): Unit = {
      println(s"mouseclick at ${e.getX} ${e.getY} ")
      AddElement(e.getX, e.getY)


    }
    override def mousePressed(e: MouseEvent): Unit = {}
    override def mouseReleased(e: MouseEvent): Unit = {}
    override def mouseEntered(e: MouseEvent): Unit = {}
    override def mouseExited(e: MouseEvent): Unit = {}
  }
  val f = new FunGraphics(1000, 1000)

  Grid()

  f.setKeyManager(keyboard)
  f.addMouseListener(mouse)
  ImportLevel("level_1")

  def Grid(): Unit = {
    for(y <- 0 until 10) {
      f.drawLine(0, y * 100, 999, y * 100)
    }
    for (x <- 0 until 10){
      f.drawLine(x * 100, 0, x * 100, 999)
    }
    for(y <- 0 until 10)
      for (x <- 0 until 10) {
        if (y == 0 || y == 9) {
          GameArray(x)(y) = 1
          f.drawFillRect(x * 100,y * 100,100,100)
        } else if (x == 0 || x == 9) {
          GameArray(x)(y) = 1
          f.drawFillRect(x * 100,y * 100,100,100)
        }
      }

  }
  def AddElement(x: Int, y: Int): Unit = {
    val x_round : Int = math.floor(x/100).toInt
    val y_round : Int = math.floor(y/100).toInt

    if (FillArray(current_key - 48,x_round, y_round)){
      current_key match {
        case '0' => f.setColor(Color.WHITE)
        case '1' => f.setColor(Color.BLACK)
        case '2' => f.setColor(Color.ORANGE)
        case '3' => f.setColor(Color.YELLOW)
        case '4' => f.setColor(Color.YELLOW)
        case '5' => f.setColor(Color.YELLOW)
        case '6' => f.setColor(Color.YELLOW)
      }
      f.drawFillRect(x_round * 100,y_round * 100,100,100)
      println("x " + math.floor(x/100).toInt)
      println("y " + math.floor(y/100).toInt)
    }
    println("x" + math.floor(x/100).toInt)
  }
  def FillArray(value: Int, x: Int, y:Int): Boolean = {
    if (value == GameArray(x)(y))
      return false
    GameArray(x)(y) = value
    for (i <- GameArray.indices) {
      for (j <- GameArray(i).indices){
        print(GameArray(j)(i) + " ")
      }
      println()
    }

    true
  }
  def CSVCreator(a : Array[Array[Int]]): Unit = {
    var new_a : String = ""
    for (i <- a.indices) {
      for (j <- a(i).indices) {
        new_a += a(i)(j)
        // new_a += ","
      }
      new_a += "\n"
    }
    var file_name : String = getFileName
    println(file_name)
    println( Files.exists(Paths.get("/levels/level_" + 1)))
    try {
      val fs: FileOutputStream = new FileOutputStream("src/levels/" + file_name,true)
      val pw: PrintWriter = new PrintWriter(fs)
      AddWall(e.getX, e.getY)

      pw.println(new_a)
      pw.close()
    }

  }
  def getFileName: String = {
    var index : Int = 1
    while (true){
      if (!Files.exists(Paths.get("src/levels/level_" + index)))
        return "level_" + index
      else
        index += 1
    }
    "" //compilateur pas content
  }
  def ImportLevel(filename : String) : Array[Array[IStaticObject]] = {
    try {
      var gameArray: Array[Array[IStaticObject]] = Array.ofDim(10, 10)
      val fr = new FileReader("src/levels/" + filename)
      val inputReader = new BufferedReader(fr)
      for (y <- gameArray.indices) {
        var line = inputReader.readLine()
        for (x <- gameArray(y).indices){
          line(x) match {
            case '0' => gameArray(x)(y) = new Space
            case '1' => gameArray(x)(y) = new Wall
            case '2' => gameArray(x)(y) = new SpikedWall
            case '3' => gameArray(x)(y) = new GunWall(MovementDirection.Left)
            case '4' => gameArray(x)(y) = new GunWall(MovementDirection.Right)
            case '5' => gameArray(x)(y) = new GunWall(MovementDirection.Up)
            case '6' => gameArray(x)(y) = new GunWall(MovementDirection.Down)
            case _ =>
          }
        }
      }
      inputReader.close()
      gameArray
    }
    catch {
      case e: FileNotFoundException =>
        println(s"fichier $filename n'existe pas!")
        Array.empty
    }
  }
}
