package starfishcollector.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor

class WorldComponent : Component, Poolable {

    var width = 0f
    var height = 0f

    override fun reset() {
        width = 0f
        height = 0f
    }

    companion object {
        val mapper = mapperFor<WorldComponent>()
    }
}
