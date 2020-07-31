package stratship

import indigo._

import scala.scalajs.js.annotation.JSExportTopLevel
import stratship.core.{Model, StartupData, ViewModel}

@JSExportTopLevel("IndigoGame")
class ShipGame extends IndigoDemo[Unit, StartupData, Model, ViewModel] {

  val config = GameConfig.default.withClearColor(ClearColor.Green)

  val assetName = AssetName("dots")

  val assets: Set[AssetType] = Set(AssetType.Image(assetName, AssetPath("assets/dots" +
    ".png")))

  //  val animations: Set[Animation] = Set()
  //  val fonts: Set[indigo.FontInfo] = Set()
  override def boot(flags: Map[String, String]): BootResult[Unit] = BootResult.noData(config)

  override def setup(bootData: Unit, assetCollection: AssetCollection, dice: Dice)
  : Startup[StartupErrors, StartupData] = Startup.Success(StartupData())

  override def initialModel(startupData: StartupData): Model = Model.initial

  override def initialViewModel(startupData: StartupData, model: Model): ViewModel =
    ViewModel.initial

  override def updateModel(context: FrameContext, model: Model): GlobalEvent =>
    Outcome[Model] = _ => Outcome((model))

  //  override def updateViewModel(context: FrameContext, model: Model,
  //                               viewModel: ViewModel): GlobalEvent =>
  //    Outcome[ViewModel] = _ => Outcome(viewModel)
  def updateViewModel(context: FrameContext, model: Model, viewModel: ViewModel)
  : Outcome[ViewModel] = Outcome(viewModel)

  override def present(context: FrameContext, model: Model, viewModel: ViewModel)
  : SceneUpdateFragment = SceneUpdateFragment(Graphic(Rectangle(0, 0, 32, 32), 1,
    Material.Textured(assetName)))

}
