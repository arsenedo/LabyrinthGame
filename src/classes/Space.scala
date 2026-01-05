package classes

import interfaces.{IMovableObject, IStaticObject}

import java.awt.Color

class Space extends IStaticObject {
  mesh = Color.BLUE

  override def isLandingAllowed: Boolean = containedObject.isEmpty

  override def requestAssignMovableObject(IMovableObject: IMovableObject): Boolean = {
    if (containedObject.isEmpty) {
      containedObject = Some(IMovableObject)
      if (IMovableObject.isInstanceOf[Player]) mesh = Color.GREEN
      true
    } else {
      false
    }
  }

  override def discardMovableObject(): Unit = {
    containedObject = None
  }
}
