package classes

import interfaces.{IMovableObject, IStaticObject}

import java.awt.Color

class Wall extends IStaticObject {
  mesh = Color.pink

  override def requestAssignMovableObject(IMovableObject: IMovableObject): Boolean = {
    // A wall can't assign a movable object
    false
  }
}
