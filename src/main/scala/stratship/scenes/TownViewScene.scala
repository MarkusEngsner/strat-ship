package stratship.scenes

import indigo.scenes.{Lens, Scene, SceneName}
import indigo.shared.{FrameContext, Outcome}
import indigo.shared.events.GlobalEvent
import indigo.shared.scenegraph.SceneUpdateFragment
import indigo.shared.subsystems.SubSystem
import stratship.core.{Model, ViewModel}

//class TownViewScene extends Scene[Model, ViewModel] {
//  override type SceneModel = this.type
//  override type SceneViewModel = this.type
//  override val name: SceneName = SceneName("Town View")
//  override val sceneModelLens: Lens[Model, TownViewScene.this.type] = Lens.fixed()
//  override val sceneViewModelLens: Lens[ViewModel, TownViewScene.this.type] = Lens
//    .fixed()
//  override val sceneSubSystems: Set[SubSystem] = Set()
//
//  override def updateSceneModel(context: FrameContext, sceneModel: this.type)
//  : GlobalEvent => Outcome[TownViewScene.this.type] = _ => Outcome(sceneModel)
//
//  override def updateSceneViewModel(context: FrameContext, sceneModel: TownViewScene
//    .this.type, sceneViewModel: TownViewScene.this.type): Outcome[this.type] = _ =>
//    Outcome(sceneViewModel)
//
//  override def updateSceneView(context: FrameContext, sceneModel: TownViewScene.this
//    .type, sceneViewModel: TownViewScene.this.type): SceneUpdateFragment = ???
//}
