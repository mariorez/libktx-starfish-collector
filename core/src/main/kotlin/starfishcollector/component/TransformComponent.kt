package starfishcollector.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor

class TransformComponent : Component, Poolable, Comparable<TransformComponent> {
    val position = Vector3()

    override fun reset() {
        position.set(0f, 0f, 0f)
    }

    override fun compareTo(other: TransformComponent): Int {
        return other.position.z.compareTo(position.z)
    }

    companion object {
        val mapper = mapperFor<TransformComponent>()
    }
}
