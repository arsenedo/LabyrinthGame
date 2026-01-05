package classes

import interfaces.{IMovableObject, IStaticObject}

import java.awt.Color

class Space extends IStaticObject {
  mesh = Color.BLUE

  override def requestAssignMovableObject(IMovableObject: IMovableObject): Boolean = {
    if (containedObject.isEmpty) {
      containedObject = Some(IMovableObject)
      true
    } else {
      false
    }
  }
}
