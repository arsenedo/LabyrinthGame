package classes

import interfaces.{IMovableObject, IStaticObject}

import java.awt.Color

class Space extends IStaticObject {
  mesh = Color.BLUE

  var playerLanded: Boolean = false;

  override def requestAssignMovableObject(IMovableObject: IMovableObject): Boolean = {
    if (containedObject.isEmpty) {
      containedObject = Some(IMovableObject)
      if (IMovableObject.isInstanceOf[Player]) {
        mesh = Color.GREEN
        playerLanded = true
      }
      true
    } else {
      // Collision!
      containedObject.get.isHit = true;
      IMovableObject.isHit = true;
      false
    }
  }

  override def discardMovableObject(): Unit = {
    containedObject = None
  }
}
