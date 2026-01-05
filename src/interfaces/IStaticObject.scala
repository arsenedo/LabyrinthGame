package interfaces

import `abstract`.GameObject

trait IStaticObject extends GameObject {
  protected var containedObject: Option[IMovableObject] = None

  def isLandingAllowed: Boolean
  def requestAssignMovableObject(IMovableObject: IMovableObject): Boolean
  def discardMovableObject: Unit
}