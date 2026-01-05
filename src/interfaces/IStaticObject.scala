package interfaces

import `abstract`.GameObject

trait IStaticObject extends GameObject {
  protected var containedObject: Option[IMovableObject] = None

  def requestAssignMovableObject(IMovableObject: IMovableObject): Boolean
}