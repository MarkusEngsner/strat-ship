package stratship.core

import indigo.AssetName
import stratship.core.Assets

case class Tile(assetName: AssetName, width: Int, height: Int, layer: Int) {}

object Tile {
  def initial = new Tile(Assets.baseTile, 32, 32, 3)

}

class TileMap(val width: Int, val height: Int) {
  val tiles: Vector[Vector[Tile]] = Vector.fill(width, height)(Tile.initial)
  // Mutable or immutable representation??
}

object TileMap {
  def initial = new TileMap(10, 10)
}
