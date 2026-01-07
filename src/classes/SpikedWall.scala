package classes

import interfaces.{IMovableObject, IStaticObject}

import java.awt.Color

class SpikedWall extends IStaticObject {
  mesh = Color.DARK_GRAY

  override def requestAssignMovableObject(IMovableObject: IMovableObject): Boolean = {
    IMovableObject.isHit = true;

    false
  }

  override def discardMovableObject: Unit = {
    containedObject = None
  }
}
