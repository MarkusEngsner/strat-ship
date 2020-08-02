package stratship

import indigo._
import indigoextras.subsystems.{AssetBundleLoader, AssetBundleLoaderEvent}

import scala.scalajs.js.annotation.JSExportTopLevel
import stratship.core.{Assets, Building, CityMap, Model, StartupData, Tile, ViewModel}
import indigoextras.ui.Button

@JSExportTopLevel("IndigoGame")
object ShipGame extends IndigoDemo[Unit, StartupData, Model, ViewModel] {

  val magnification = 1
  val config: indigo.GameConfig =
    GameConfig(GameViewport(1280, 720), 60, ClearColor.Green, magnification)

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
    case MouseEvent.Click(x, y) => {
      println(Point(x, y))
      Outcome(model.addBuilding(Point(x, y)))
    }
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

  val background = {
    val temp =
      drawBackground(config.viewport.width, config.viewport.height, Tile.initial)
    temp foreach (g => println(s"position: ${g.position}"))
    println(s"Squares: " + temp.length)
    Group(temp)
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
      .addGameLayerNodes(drawBuildings(model.cityMap.buildings))
      .addGameLayerNodes(background.moveTo(0, 0))
//      .addGameLayerNodes(Graphic(Rectangle()))

  }

  def drawBuildings(buildings: List[Building]): List[Graphic] = {
    buildings.map { b =>
      Assets.clickBuilding.moveTo(b.center)
    }
  }

//  val bgClone = Clone(CloneId(), tile.layer, CloneTransformData())

  def drawBackground(pixelWidth: Int, pixelHeight: Int, tile: Tile): List[Graphic] = {
    def createRect(x: Int, y: Int): Graphic = {
//      if (x > 480) println(x)
      Graphic(Rectangle(0, 0, tile.width, tile.height),
              tile.layer,
              Material.Textured(tile.assetName)).moveTo(x, y)
    }

//    println(s"Width: $pixelWidth\tHeight: $pixelHeight")
    val w = List.range(0, pixelWidth / magnification, tile.width)
    val h = List.range(0, pixelHeight / magnification, tile.height)
    w collect (x => h collect (y => createRect(x, y))) flatMap (identity)
  }
}
