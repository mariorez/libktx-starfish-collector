package starfishcollector.screen

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
import starfishcollector.component.RenderComponent
import starfishcollector.component.TransformComponent
import starfishcollector.system.RenderingSystem

class FirstScreen(
    private val assets: AssetManager
) : Screen() {
    private val engine = PooledEngine()
    private val batch = SpriteBatch()

    init {
        registerAction(Input.Keys.W, Name.UP)
        registerAction(Input.Keys.S, Name.DOWN)
        registerAction(Input.Keys.A, Name.LEFT)
        registerAction(Input.Keys.D, Name.RIGHT)

        engine.add {
            entity { // Background
                with<RenderComponent> {
                    sprite.setRegion(assets.getAsset<Texture>("large-water.jpg"))
                }
                with<TransformComponent> {
                    position.x = 0f
                    position.y = 0f
                }
            }
            entity { // Turtle
                with<RenderComponent> {
                    sprite.setRegion(assets.getAsset<Texture>("turtle-1.png"))
                }
                with<TransformComponent> {
                    position.x = 150f
                    position.y = 150f
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
        debug { "$action" }
    }

    override fun dispose() {
        batch.disposeSafely()
    }
}
