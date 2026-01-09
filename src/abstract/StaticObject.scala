package `abstract`

import `abstract`.GameObject
import classes.Position

abstract class StaticObject(x: Int, y: Int) extends GameObject {
  val position = new Position(x, y)

  protected var containedObject: Option[MovableObject] = None

  def getContainedObject: Option[MovableObject] = containedObject
  def requestAssignMovableObject(IMovableObject: MovableObject): Boolean = false

  def discardMovableObject(): Unit = {
    containedObject = None
  }
}