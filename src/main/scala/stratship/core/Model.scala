package stratship.core

import indigo.{AssetName, Point, Seconds}
import stratship.core.TileMap

// Add size etc
case class Building(center: Point) {}

case class CityMap(width: Int, height: Int, buildings: List[Building]) {
  def addBuilding(center: Point) =
    this.copy(buildings = Building(center) :: buildings)
}

object CityMap {
  def initial(): CityMap = CityMap(5000, 2000, Nil)

}
case class Model(loaded: Boolean, cityMap: CityMap) {
  def addBuilding(center: Point): Model =
    this.copy(cityMap = cityMap.addBuilding(center))

//  def update(timeDelta: Seconds): Model = this
}
object Model {
  def initial(): Model = Model(false, CityMap.initial)
}
