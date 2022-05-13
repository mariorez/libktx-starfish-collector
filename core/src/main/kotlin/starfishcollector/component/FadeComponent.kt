package starfishcollector.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import starfishcollector.component.FadeComponent.Mode.OUT

class FadeComponent : Component, Pool.Poolable {

    enum class Mode { IN, OUT }

    var mode = OUT
    var alpha = 1f
    var duration = 1
    var removeEntityOnEnd = false

    override fun reset() {
        mode = OUT
        alpha = 1f
        duration = 1
        removeEntityOnEnd = false
    }

    companion object {
        val mapper = mapperFor<FadeComponent>()
    }
}
