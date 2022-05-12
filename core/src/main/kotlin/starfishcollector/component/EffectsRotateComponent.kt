package starfishcollector.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import starfishcollector.component.EffectsRotateComponent.Direction.CLOCKWISE

class EffectsRotateComponent : Component, Pool.Poolable {

    enum class Direction { CLOCKWISE, COUNTERCLOCKWISE }

    var direction = CLOCKWISE
    var speed = 0

    override fun reset() {
        direction = CLOCKWISE
        speed = 0
    }

    companion object {
        val mapper = mapperFor<EffectsRotateComponent>()
    }
}
