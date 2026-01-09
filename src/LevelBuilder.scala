import `abstract`.{GameObject, MovementDirection, StaticObject}
import classes.{GunWall, Player, Space, SpikedWall, Wall}
import hevs.graphics.FunGraphics
import hevs.graphics.utils.GraphicsBitmap

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

  def Grid(): Unit = {
    for(y <- 0 until 10) {
      f.drawLine(0, y * 100, 999, y * 100)
    }
    for (x <- 0 until 10){
      f.drawLine(x * 100, 0, x * 100, 999)
    }
    val wallMesh = new GraphicsBitmap("/assets/img/Wall.png")
    for(y <- 0 until 10)
      for (x <- 0 until 10) {
        if ((y == 0 || y == 9) || (x == 0 || x == 9)) {
          GameArray(x)(y) = 1
          f.drawPicture(x * 100 + 50, y * 100 + 50, wallMesh)
        }
      }

  }
  def AddElement(x: Int, y: Int): Unit = {
    val x_round : Int = math.floor(x/100).toInt
    val y_round : Int = math.floor(y/100).toInt

    if (FillArray(current_key - 48,x_round, y_round)){
      var mesh = new GraphicsBitmap("/assets/img/Path.png")
      current_key match {
        case '0' => mesh = new GraphicsBitmap("/assets/img/Path.png")
        case '1' => mesh = new GraphicsBitmap("/assets/img/Wall.png")
        case '2' => mesh = new GraphicsBitmap("/assets/img/SpikedWall.png")
        case '3' => mesh = new GraphicsBitmap("/assets/img/GunWallLeft.png")
        case '4' => mesh = new GraphicsBitmap("/assets/img/GunWallRight.png")
        case '5' => mesh = new GraphicsBitmap("/assets/img/GunWallUp.png")
        case '6' => mesh = new GraphicsBitmap("/assets/img/GunWallBottom.png")
      }
      f.drawPicture(x_round * 100 + 50, y_round * 100 + 50, mesh)
      println("x " + math.floor(x/100).toInt)
      println("y " + math.floor(y/100).toInt)
    }
    println("x" + math.floor(x/100).toInt)
  }
  def FillArray(value: Int, x: Int, y:Int): Boolean = {
    GameArray(y)(x) = value
    for (i <- GameArray.indices) {
      for (j <- GameArray(i).indices){
        print(GameArray(j)(i).toString + " ")
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
  def ImportLevel(filename : String) : Array[Array[StaticObject]] = {
    try {
      var gameArray: Array[Array[StaticObject]] = Array.ofDim(10, 10)
      val fr = new FileReader("src/levels/" + filename)
      val inputReader = new BufferedReader(fr)
      for (y <- gameArray.indices) {
        var line = inputReader.readLine()
        for (x <- gameArray(y).indices){
          line(x) match {
            case '0' => gameArray(y)(x) = new Space(x, y)
            case '1' => gameArray(y)(x) = new Wall(x, y)
            case '2' => gameArray(y)(x) = new SpikedWall(x, y)
            case '3' => gameArray(y)(x) = new GunWall(x, y, MovementDirection.Left)
            case '4' => gameArray(y)(x) = new GunWall(x, y, MovementDirection.Right)
            case '5' => gameArray(y)(x) = new GunWall(x, y, MovementDirection.Up)
            case '6' => gameArray(y)(x) = new GunWall(x, y, MovementDirection.Down)
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
