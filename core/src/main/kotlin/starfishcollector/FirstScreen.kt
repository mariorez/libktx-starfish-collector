package starfishcollector

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxScreen
import ktx.ashley.add
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.disposeSafely
import ktx.assets.getAsset
import starfishcollector.component.RenderComponent
import starfishcollector.component.TransformComponent
import starfishcollector.system.RenderingSystem

class FirstScreen(
    private val assets: AssetManager
) : KtxScreen {
    private val engine = PooledEngine()
    private val batch = SpriteBatch()

    init {
        engine.add {
            entity { // Turtle
                with<RenderComponent> {
                    sprite.setRegion(assets.getAsset<Texture>("turtle-1.png"))
                }
                with<TransformComponent> {
                    position.x = 170f
                    position.y = 300f
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

    override fun dispose() {
        batch.disposeSafely()
    }
}
