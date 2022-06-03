package component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import component.RotateEffectComponent.Direction.CLOCKWISE

class RotateEffectComponent : Component, Pool.Poolable {

    enum class Direction { CLOCKWISE, COUNTERCLOCKWISE }

    var direction = CLOCKWISE
    var speed = 0f

    override fun reset() {
        direction = CLOCKWISE
        speed = 0f
    }

    companion object {
        val mapper = mapperFor<RotateEffectComponent>()
    }
}
