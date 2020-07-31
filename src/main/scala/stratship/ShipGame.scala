package stratship

import indigo._

import scala.scalajs.js.annotation.JSExportTopLevel
import stratship.core.{Model, StartupData, ViewModel}

@JSExportTopLevel("IndigoGame")
object ShipGame extends IndigoSandbox[StartupData, Model] {

  val config: indigo.GameConfig = GameConfig.default.withClearColor(ClearColor.Green)

  val magnification = 3
  val animations: Set[Animation] = Set()
  val fonts: Set[indigo.FontInfo] = Set()

  val assetName = AssetName("dots")

  val assets: Set[AssetType] = Set(AssetType.Image(assetName, AssetPath("assets/dots" +
    ".png")))

  override def setup(
             assetCollection: indigo.AssetCollection,
             dice: indigo.Dice
           ): indigo.Startup[indigo.StartupErrors, StartupData] =
    Startup.Success(StartupData())

//  override def setup(assetCollection: AssetCollection, dice: Dice)
//  : Startup[StartupErrors, StartupData] = Startup.Success(StartupData())

  override def initialModel(startupData: StartupData): Model =
    Model.initial(config.viewport.giveDimensions(magnification).center)

//  override def initialViewModel(startupData: StartupData, model: Model): ViewModel =
//    ViewModel.initial


  override def updateModel(
                   context: indigo.FrameContext[StartupData],
                   model: Model
                 ): indigo.GlobalEvent => indigo.Outcome[Model] = {
    case MouseEvent.Click(x, y) =>
      val adjustedPosition = Point(x, y) - model.center
      Outcome( model )

    case FrameTick =>
      Outcome(model.update(context.delta))

    case _ =>
      Outcome(model)
  }

  override def present(
               context: indigo.FrameContext[StartupData],
               model: Model
             ): indigo.SceneUpdateFragment =
    SceneUpdateFragment(
      Graphic(Rectangle(0, 0, 32, 32), 1, Material.Textured(assetName))
    )
}
