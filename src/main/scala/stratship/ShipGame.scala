package stratship

import indigo._
import indigoextras.subsystems.{AssetBundleLoader, AssetBundleLoaderEvent}

import scala.scalajs.js.annotation.JSExportTopLevel
import stratship.core.{Assets, Building, CityMap, Model, StartupData, Tile, ViewModel}
import indigoextras.ui.Button

@JSExportTopLevel("IndigoGame")
object ShipGame extends IndigoDemo[Unit, StartupData, Model, ViewModel] {

  val magnification = 2
  val config: indigo.GameConfig = GameConfig.default
    .withClearColor(ClearColor.Green)
    .withMagnification(magnification)

  override def eventFilters = EventFilters.Default
  val animations: Set[Animation] = Set()
  val fonts: Set[indigo.FontInfo] = Set()

  val assetName = AssetName("dots")

  val assets: Set[AssetType] = Set(
    AssetType.Image(
      assetName,
      AssetPath(
        "assets/dots" +
          ".png"
      )
    )
  )

  override def boot(flags: Map[String, String]): BootResult[Unit] =
    BootResult
      .noData(config)
      .withAssets(Assets.assets)
      .withSubSystems(AssetBundleLoader)

  override def setup(
      bootInfo: Unit,
      assetCollection: indigo.AssetCollection,
      dice: indigo.Dice
  ): indigo.Startup[indigo.StartupErrors, StartupData] =
    assetCollection.findTextDataByName(AssetName("text")) match {
      case Some(value) =>
        println("Loaded text! " + value)
        Startup.Success(StartupData())
      case None => Startup.Success(StartupData())
    }

  override def initialModel(startupData: StartupData): Model =
    Model(loaded = false, CityMap.initial)

  override def initialViewModel(startupData: StartupData, model: Model): ViewModel =
    ViewModel(
      button = Button(
        buttonAssets = Assets.buttonAssets,
        bounds = Rectangle(10, 10, 16, 16),
        depth = Depth(2)
      ).withUpAction {
        println("Start loading assets...")
        List(
          AssetBundleLoaderEvent.Load(
            BindingKey("Junction box assets"),
            Assets.junctionboxImageAssets ++ Assets.otherAssetsToLoad
          )
        )
      }
    )

  override def updateModel(
      context: indigo.FrameContext[StartupData],
      model: Model
  ): indigo.GlobalEvent => indigo.Outcome[Model] = {
    case MouseEvent.Click(x, y) =>
      Outcome(model.addBuilding(Point(x, y)))
    case AssetBundleLoaderEvent.Started(key) =>
      println("Load started! " + key.toString())
      Outcome(model)

    case AssetBundleLoaderEvent.LoadProgress(key, percent, completed, total) =>
      println(s"In progress...: ${key.toString()} - ${percent
        .toString()}%, ${completed.toString()} of ${total.toString()}")
      Outcome(model)

    case AssetBundleLoaderEvent.Success(key) =>
      println("Got it! " + key.toString())
      Outcome(model.copy(loaded = true))
        .addGlobalEvents(PlaySound(AssetName("sfx"), Volume.Max))

    case AssetBundleLoaderEvent.Failure(key, message) =>
      println(s"Lost it... '$message' " + key.toString())
      Outcome(model)

    case _ =>
      Outcome(model)
  }
//    case MouseEvent.Click(x, y) =>
//      val adjustedPosition = Point(x, y) - model.center
//      Outcome( model )
//
//    case FrameTick =>
//      Outcome(model.update(context.delta))

  override def updateViewModel(
      context: FrameContext[StartupData],
      model: Model,
      viewModel: ViewModel
  ): GlobalEvent => Outcome[ViewModel] = {
    case FrameTick =>
      viewModel.button.update(context.inputState.mouse).map { btn =>
        viewModel.copy(button = btn)
      }
    case _ => Outcome(viewModel)
  }

  override def present(context: indigo.FrameContext[StartupData],
                       model: Model,
                       viewModel: ViewModel): indigo.SceneUpdateFragment = {
    val box = if (model.loaded) {
      List(
        Graphic(Rectangle(0, 0, 64, 64), 1, Assets.junctionBoxMaterial)
          .moveTo(30, 30)
      )
    } else Nil
    SceneUpdateFragment(viewModel.button.draw :: box)
      .addGameLayerNodes(
        List[Graphic](
          Graphic(Rectangle(0, 0, 512, 512), 1, Material.Textured(Assets.house))
            .scaleBy(0.5, 0.5),
          Graphic(Rectangle(30, 30, 32, 32), 1, Material.Textured(AssetName("tile"))),
        )
      )
      .addGameLayerNodes(drawBuildings(model.cityMap.buildings))
      .addGameLayerNodes(
        drawBackground(config.viewport.width, config.viewport.width, Tile.initial))

  }

  def drawBuildings(buildings: List[Building]): List[Graphic] = {
    buildings.map { b =>
      Graphic(Rectangle(0, 0, 32, 32), 1, Material.Textured(Assets.baseTile))
        .withRef(16, 16)
        .moveTo(b.center)
    }
  }

  def drawBackground(pixelWidth: Int, pixelHeight: Int, tile: Tile): List[Graphic] = {
    def createRect(x: Int, y: Int): Graphic =
      Graphic(Rectangle(x, y, tile.width, tile.height),
              tile.layer,
              Material.Textured(tile.assetName))

    val w = List.range(0, pixelWidth, tile.width)
    val h = List.range(0, pixelHeight, tile.height)
    w collect (x => h collect (y => createRect(x, y))) flatMap (identity)
  }
}
