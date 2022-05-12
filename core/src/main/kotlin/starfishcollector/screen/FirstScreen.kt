package starfishcollector.screen

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer
import ktx.ashley.add
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.async.AssetStorage
import ktx.assets.disposeSafely
import ktx.tiled.forEachMapObject
import ktx.tiled.height
import ktx.tiled.tileHeight
import ktx.tiled.tileWidth
import ktx.tiled.type
import ktx.tiled.width
import ktx.tiled.x
import ktx.tiled.y
import starfishcollector.Action
import starfishcollector.GameBoot
import starfishcollector.Screen
import starfishcollector.component.AnimationComponent
import starfishcollector.component.BoundingBoxComponent
import starfishcollector.component.BoundingBoxComponent.BoxType.ROCK
import starfishcollector.component.BoundingBoxComponent.BoxType.SIGN
import starfishcollector.component.BoundingBoxComponent.BoxType.STARFISH
import starfishcollector.component.BoundingBoxComponent.BoxType.TURTLE
import starfishcollector.component.CameraComponent
import starfishcollector.component.EffectsRotateComponent
import starfishcollector.component.InputComponent
import starfishcollector.component.PlayerComponent
import starfishcollector.component.RenderComponent
import starfishcollector.component.TransformComponent
import starfishcollector.component.WorldComponent
import starfishcollector.generateBoundaryPolygon
import starfishcollector.generateBoundaryRectangle
import starfishcollector.system.AnimationSystem
import starfishcollector.system.CameraSystem
import starfishcollector.system.CollisionSystem
import starfishcollector.system.EffectsRotateSystem
import starfishcollector.system.InputSystem
import starfishcollector.system.MovementSystem
import starfishcollector.system.RenderingSystem

class FirstScreen(
    private val assets: AssetStorage
) : Screen() {
    private val engine = PooledEngine()
    private val batch = SpriteBatch()
    private val mainCamera = OrthographicCamera(
        GameBoot.WINDOW_WIDTH.toFloat(),
        GameBoot.WINDOW_HEIGHT.toFloat()
    )
    private val mapRenderer: OrthoCachedTiledMapRenderer
    private val player: Entity

    init {
        registerAction(Input.Keys.W, Action.Name.UP)
        registerAction(Input.Keys.S, Action.Name.DOWN)
        registerAction(Input.Keys.A, Action.Name.LEFT)
        registerAction(Input.Keys.D, Action.Name.RIGHT)

        val tiledMap = assets.get<TiledMap>("map.tmx")

        mapRenderer = OrthoCachedTiledMapRenderer(tiledMap).apply {
            setBlending(true)
        }

        tiledMap.forEachMapObject("collision") { obj ->
            val texture = assets.get<Texture>("${obj.type}.png")
            engine.entity {
                with<RenderComponent> {
                    sprite.apply {
                        setRegion(texture)
                        setSize(
                            texture.width.toFloat(),
                            texture.height.toFloat()
                        )
                    }
                }
                with<TransformComponent> {
                    position.x = obj.x
                    position.y = obj.y
                }
                when (obj.type) {
                    "rock" -> {
                        with<BoundingBoxComponent> {
                            type = ROCK
                            polygon = generateBoundaryPolygon(8, texture.width.toFloat(), texture.height.toFloat())
                        }
                    }
                    "sign" -> {
                        with<BoundingBoxComponent> {
                            type = SIGN
                            polygon = generateBoundaryRectangle(texture.width.toFloat(), texture.height.toFloat())
                        }
                    }
                    "starfish" -> {
                        with<BoundingBoxComponent> {
                            type = STARFISH
                            polygon = generateBoundaryPolygon(8, texture.width.toFloat(), texture.height.toFloat())
                        }
                        with<EffectsRotateComponent> {
                            speed = 1
                        }
                    }
                }

                with<BoundingBoxComponent> {
                    when (obj.type) {
                        "rock" -> {
                            type = ROCK
                            polygon = generateBoundaryPolygon(8, texture.width.toFloat(), texture.height.toFloat())
                        }
                        "starfish" -> {
                            type = STARFISH
                            polygon = generateBoundaryPolygon(8, texture.width.toFloat(), texture.height.toFloat())
                        }
                        "sign" -> {
                            type = SIGN
                            polygon = generateBoundaryRectangle(texture.width.toFloat(), texture.height.toFloat())
                        }
                    }
                }
            }
        }

        val world = WorldComponent().apply {
            width = (tiledMap.tileWidth * tiledMap.width).toFloat()
            height = (tiledMap.tileHeight * tiledMap.height).toFloat()
        }

        player = engine.entity {
            with<PlayerComponent>()
            with<InputComponent>()
            with<RenderComponent>()
            with<TransformComponent> {
                position.x = 50f
                position.y = 50f
                acceleration = 400f
                deceleration = 400f
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
                type = TURTLE
                polygon = generateBoundaryPolygon(8, width, height)
            }
        }.add(world)

        engine.add {
            entity {
                with<CameraComponent> {
                    camera = mainCamera
                    target = player
                }
            }.add(world)
        }

        engine.apply {
            addSystem(InputSystem())
            addSystem(MovementSystem())
            addSystem(AnimationSystem())
            addSystem(EffectsRotateSystem())
            addSystem(CameraSystem())
            addSystem(RenderingSystem(batch, mainCamera, mapRenderer))
            addSystem(CollisionSystem(player))
        }
    }

    override fun doAction(action: Action) {
        val isStarting = action.type == Action.Type.START
        when (action.name) {
            Action.Name.UP -> InputComponent.mapper.get(player).up = isStarting
            Action.Name.DOWN -> InputComponent.mapper.get(player).down = isStarting
            Action.Name.LEFT -> InputComponent.mapper.get(player).left = isStarting
            Action.Name.RIGHT -> InputComponent.mapper.get(player).right = isStarting
        }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun dispose() {
        batch.disposeSafely()
        assets.disposeSafely()
    }
}
