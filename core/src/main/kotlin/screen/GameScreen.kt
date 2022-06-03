package screen

import Action
import BaseScreen
import GameBoot
import WorldSize
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import component.AnimationComponent
import component.BoundingBoxComponent
import component.InputComponent
import component.PlayerComponent
import component.RenderComponent
import component.RockComponent
import component.RotateEffectComponent
import component.SignComponent
import component.StarfishComponent
import component.TransformComponent
import generatePolygon
import generateRectangle
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.async.AssetStorage
import ktx.assets.disposeSafely
import ktx.tiled.forEachMapObject
import ktx.tiled.totalHeight
import ktx.tiled.totalWidth
import ktx.tiled.type
import ktx.tiled.x
import ktx.tiled.y
import listener.StarfishCounter
import system.AnimationSystem
import system.CameraSystem
import system.CollisionSystem
import system.FadeEffectSystem
import system.InputSystem
import system.MovementSystem
import system.RenderingSystem
import system.RotateEffectSystem

class GameScreen(
    private val assets: AssetStorage,
    labelStyle: LabelStyle
) : BaseScreen() {
    private val engine = PooledEngine()
    private val batch = SpriteBatch()
    private val camera = OrthographicCamera(
        GameBoot.WINDOW_WIDTH.toFloat(),
        GameBoot.WINDOW_HEIGHT.toFloat()
    )
    private val tiledMap = assets.get<TiledMap>("map.tmx")
    private val mapRenderer = OrthoCachedTiledMapRenderer(tiledMap).apply {
        setBlending(true)
    }
    private val worldSize = WorldSize(tiledMap.totalWidth(), tiledMap.totalHeight())
    private lateinit var turtle: Entity
    private var starFishLabel = Label("", labelStyle)

    init {
        registerAction(Input.Keys.W, Action.Name.UP)
        registerAction(Input.Keys.S, Action.Name.DOWN)
        registerAction(Input.Keys.A, Action.Name.LEFT)
        registerAction(Input.Keys.D, Action.Name.RIGHT)

        table.apply {
            pad(5f)
            add(starFishLabel).top()
            add().expandX().expandY()
        }

        spawnPlayer()

        engine.apply {
            addEntityListener(allOf(StarfishComponent::class).get(), StarfishCounter())
            addSystem(InputSystem(turtle))
            addSystem(MovementSystem(turtle, worldSize))
            addSystem(AnimationSystem())
            addSystem(CameraSystem(camera, turtle, worldSize))
            addSystem(RotateEffectSystem())
            addSystem(FadeEffectSystem())
            addSystem(RenderingSystem(batch, camera, mapRenderer))
            addSystem(CollisionSystem(turtle, assets))
        }

        spawnObjects()
    }

    private fun spawnPlayer() {
        turtle = engine.entity {
            with<PlayerComponent>()
            with<InputComponent>()
            with<RenderComponent>()
            with<TransformComponent> {
                position.x = 50f
                position.y = 50f
                zIndex = -1f
                acceleration = 400f
                deceleration = 250f
                maxSpeed = 150f
            }
            val animation = with<AnimationComponent> {
                region = assets
                    .get<TextureAtlas>("starfish-collector.atlas")
                    .findRegion("turtle")
                frames = 6
                frameDuration = 0.1f
            }
            with<BoundingBoxComponent> {
                val width = (animation.region.regionWidth / animation.frames).toFloat()
                val height = animation.region.regionHeight.toFloat()
                polygon = generatePolygon(8, width, height)
            }
        }
    }

    private fun spawnObjects() {
        tiledMap.forEachMapObject("collision") { obj ->
            val texture = assets.get<Texture>("${obj.type}.png")
            engine.entity {
                with<TransformComponent> { position.set(obj.x, obj.y) }
                with<RenderComponent> {
                    sprite.apply {
                        setRegion(texture)
                        setSize(texture.width.toFloat(), texture.height.toFloat())
                    }
                }
                when (obj.type) {
                    "rock" -> {
                        with<RockComponent>()
                        with<BoundingBoxComponent> {
                            polygon = generatePolygon(8, texture.width, texture.height).apply {
                                setPosition(obj.x, obj.y)
                            }
                        }
                    }

                    "sign" -> {
                        with<SignComponent>()
                        with<BoundingBoxComponent> {
                            polygon = generateRectangle(texture.width, texture.height).apply {
                                setPosition(obj.x, obj.y)
                            }
                        }
                    }

                    "starfish" -> {
                        with<StarfishComponent>()
                        with<RotateEffectComponent> { speed = 1f }
                        with<BoundingBoxComponent> {
                            polygon = generatePolygon(8, texture.width, texture.height).apply {
                                setPosition(obj.x, obj.y)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun doAction(action: Action) {
        val input = InputComponent.mapper.get(turtle)
        val isStarting = action.type == Action.Type.START
        when (action.name) {
            Action.Name.UP -> input.up = isStarting
            Action.Name.DOWN -> input.down = isStarting
            Action.Name.LEFT -> input.left = isStarting
            Action.Name.RIGHT -> input.right = isStarting
        }
    }

    override fun render(delta: Float) {
        engine.update(delta)
        starFishLabel.setText("Starfish Left: ${StarfishCounter.getStarfishCounter()}")
        uiStage.draw()
    }

    override fun dispose() {
        batch.disposeSafely()
        assets.disposeSafely()
        mapRenderer.disposeSafely()
        tiledMap.disposeSafely()
        uiStage.disposeSafely()
    }
}
