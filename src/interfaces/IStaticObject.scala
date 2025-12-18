package interfaces

import `abstract`.GameObject

trait IStaticObject extends GameObject {
  val containedObject: IMovableObject

  def requestAssignMovableObject(IMovableObject: IMovableObject): Boolean
}