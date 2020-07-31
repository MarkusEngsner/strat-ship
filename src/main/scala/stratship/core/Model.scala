package stratship.core

import indigo.{Point, Seconds}

case class Model(center: Point) {
  def update(timeDelta: Seconds): Model = this
}
object Model {
  def initial(center: Point): Model = Model(center)
}
