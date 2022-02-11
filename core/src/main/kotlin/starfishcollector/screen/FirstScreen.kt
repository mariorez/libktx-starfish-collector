package starfishcollector.screen

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.ashley.add
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.disposeSafely
import ktx.assets.getAsset
import ktx.log.debug
import starfishcollector.Action
import starfishcollector.Name
import starfishcollector.Screen
import starfishcollector.Type
import starfishcollector.component.InputComponent
import starfishcollector.component.RenderComponent
import starfishcollector.component.TransformComponent
import starfishcollector.system.RenderingSystem

class FirstScreen(
    private val assets: AssetManager
) : Screen() {
    private val engine = PooledEngine()
    private val batch = SpriteBatch()
    private val player: Entity

    init {
        registerAction(Input.Keys.W, Name.UP)
        registerAction(Input.Keys.S, Name.DOWN)
        registerAction(Input.Keys.A, Name.LEFT)
        registerAction(Input.Keys.D, Name.RIGHT)

        player = engine.entity {
            with<RenderComponent> {
                sprite.setRegion(assets.getAsset<Texture>("turtle-1.png"))
            }
            with<TransformComponent> {
                position.x = 150f
                position.y = 150f
            }
            with<InputComponent>()
        }

        engine.add {
            entity { // Background
                with<RenderComponent> {
                    sprite.setRegion(assets.getAsset<Texture>("large-water.jpg"))
                }
                with<TransformComponent> {
                    position.x = 0f
                    position.y = 0f
                    position.z = 2f
                }
            }
        }

        engine.apply {
            addSystem(RenderingSystem(batch))
        }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun doAction(action: Action) {
        val state = action.type == Type.START
        when (action.name) {
            Name.UP -> InputComponent.mapper.get(player).up = state
            Name.DOWN -> InputComponent.mapper.get(player).down = state
            Name.LEFT -> InputComponent.mapper.get(player).left = state
            Name.RIGHT -> InputComponent.mapper.get(player).right = state
        }

        debug { "$action" }
        debug { "UP state: ${InputComponent.mapper.get(player).up}" }
    }

    override fun dispose() {
        batch.disposeSafely()
    }
}
