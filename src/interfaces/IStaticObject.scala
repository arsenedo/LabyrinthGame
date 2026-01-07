package interfaces

import `abstract`.GameObject

trait IStaticObject extends GameObject {
  protected var containedObject: Option[IMovableObject] = None

  def getContainedObject: Option[IMovableObject] = containedObject
  def requestAssignMovableObject(IMovableObject: IMovableObject): Boolean
  def discardMovableObject(): Unit
}