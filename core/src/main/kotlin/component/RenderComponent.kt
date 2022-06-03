package component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor

class RenderComponent : Component, Poolable {

    val sprite: Sprite = Sprite()

    override fun reset() {
    }

    companion object {
        val mapper = mapperFor<RenderComponent>()
    }
}

