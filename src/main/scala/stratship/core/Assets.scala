package stratship.core

import indigo.{AssetName, AssetPath, AssetType, Material, Graphic}
import indigoextras.ui.{ButtonAssets, Button}

object Assets {

  val junctionBoxAlbedo: AssetName = AssetName("junctionbox_albedo")
  val junctionBoxEmission: AssetName = AssetName("junctionbox_emission")
  val junctionBoxNormal: AssetName = AssetName("junctionbox_normal")
  val junctionBoxSpecular: AssetName = AssetName("junctionbox_specular")

  def junctionboxImageAssets: Set[AssetType] =
    Set(
      AssetType.Image(
        junctionBoxAlbedo,
        AssetPath("assets/" + junctionBoxAlbedo.value + ".png")
      ),
      AssetType.Image(
        junctionBoxEmission,
        AssetPath("assets/" + junctionBoxEmission.value + ".png")
      ),
      AssetType.Image(
        junctionBoxNormal,
        AssetPath("assets/" + junctionBoxNormal.value + ".png")
      ),
      AssetType.Image(
        junctionBoxSpecular,
        AssetPath("assets/" + junctionBoxSpecular.value + ".png")
      )
    )

  def otherAssetsToLoad: Set[AssetType] =
    Set(
      AssetType.Text(AssetName("text"), AssetPath("assets/test.txt")),
      AssetType.Audio(AssetName("sfx"), AssetPath("assets/RetroGameJump.mp3"))
    )

  val junctionBoxMaterial: Material.Lit =
    Material.Lit(
      junctionBoxAlbedo,
      junctionBoxEmission,
      junctionBoxNormal,
      junctionBoxSpecular
    )

  val baseTile: AssetName = AssetName("tile")
  val house: AssetName = AssetName("house")

  def assets: Set[AssetType] =
    Set(
      AssetType.Image(AssetName("graphics"), AssetPath("assets/graphics.png")),
      AssetType.Image(baseTile, AssetPath("assets/land_1.png")),
      AssetType.Image(house, AssetPath("assets/building_1.png")),
    )

  val buttonAssets: ButtonAssets =
    ButtonAssets(
      up = Graphic(0, 0, 16, 16, 2, Material.Textured(AssetName("graphics")))
        .withCrop(32, 0, 16, 16),
      over = Graphic(0, 0, 16, 16, 2, Material.Textured(AssetName("graphics")))
        .withCrop(32, 16, 16, 16),
      down = Graphic(0, 0, 16, 16, 2, Material.Textured(AssetName("graphics")))
        .withCrop(32, 32, 16, 16)
    )

}
