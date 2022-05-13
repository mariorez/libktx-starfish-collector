package starfishcollector.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import starfishcollector.component.RotateComponent.Direction.CLOCKWISE

class RotateComponent : Component, Pool.Poolable {

    enum class Direction { CLOCKWISE, COUNTERCLOCKWISE }

    var direction = CLOCKWISE
    var speed = 0f

    override fun reset() {
        direction = CLOCKWISE
        speed = 0f
    }

    companion object {
        val mapper = mapperFor<RotateComponent>()
    }
}
