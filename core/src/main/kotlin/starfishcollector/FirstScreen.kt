package starfishcollector

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxScreen
import ktx.ashley.add
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import starfishcollector.component.RenderComponent
import starfishcollector.component.TransformComponent
import starfishcollector.system.RenderingSystem

class FirstScreen : KtxScreen {
    private val engine = PooledEngine()
    private val batch = SpriteBatch()
    private val turtle = Texture("turtle-1.png".toInternalFile()).apply {
        setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        )
    }

    init {
        engine.add {
            entity { // Turtle
                with<RenderComponent> {
                    sprite.setRegion(turtle)
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
