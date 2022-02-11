package starfishcollector.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor

class TransformComponent : Component, Poolable, Comparable<TransformComponent> {
    var zIndex = 0f
    val position = Vector2()
    val velocity = Vector2()

    override fun reset() {
        zIndex = 0f
        position.set(0f, 0f)
        velocity.set(0f, 0f)
    }

    override fun compareTo(other: TransformComponent): Int {
        return other.zIndex.compareTo(zIndex)
    }

    companion object {
        val mapper = mapperFor<TransformComponent>()
    }
}
