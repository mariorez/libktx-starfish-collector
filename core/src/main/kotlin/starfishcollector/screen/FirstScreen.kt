package starfishcollector.screen

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.ashley.add
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.async.AssetStorage
import ktx.assets.disposeSafely
import starfishcollector.Action
import starfishcollector.GameBoot
import starfishcollector.Screen
import starfishcollector.component.*
import starfishcollector.system.*

class FirstScreen(
    private val assets: AssetStorage
) : Screen() {
    private val engine = PooledEngine()
    private val batch = SpriteBatch()
    private val orthographicCamera = OrthographicCamera(
        GameBoot.SCREEN_WIDTH.toFloat(),
        GameBoot.SCREEN_HEIGHT.toFloat()
    )
    private val player: Entity

    init {
        registerAction(Input.Keys.W, Action.Name.UP)
        registerAction(Input.Keys.S, Action.Name.DOWN)
        registerAction(Input.Keys.A, Action.Name.LEFT)
        registerAction(Input.Keys.D, Action.Name.RIGHT)

        val worldBackground = assets.get<Texture>("large-water.jpg")

        player = engine.entity {
            with<PlayerComponent>()
            with<InputComponent>()
            with<RenderComponent>()
            with<AnimationComponent> {
                region = assets
                    .get<TextureAtlas>("starfish-collector.atlas")
                    .findRegion("turtle")
                frames = 6
                frameDuration = 0.1f
            }
            with<TransformComponent> {
                position.x = 150f
                position.y = 150f
                acceleration = 400f
                deceleration = 400f
                maxSpeed = 100f
            }
        }

        engine.add {
            entity {
                with<CameraComponent> {
                    camera = orthographicCamera
                    target = player
                    worldWidth = worldBackground.width.toFloat()
                    worldHeight = worldBackground.height.toFloat()
                }
            }

            entity {
                with<RenderComponent> {
                    sprite.apply {
                        setRegion(worldBackground)
                        setSize(
                            worldBackground.width.toFloat(),
                            worldBackground.height.toFloat()
                        )
                    }
                }
                with<TransformComponent> {
                    position.x = 0f
                    position.y = 0f
                    zIndex = 1f
                }
            }
        }

        engine.apply {
            addSystem(InputSystem())
            addSystem(MovementSystem())
            addSystem(AnimationSystem())
            addSystem(CameraSystem())
            addSystem(RenderingSystem(batch, orthographicCamera))
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
