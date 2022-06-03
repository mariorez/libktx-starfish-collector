package component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import component.FadeEffectComponent.Mode.OUT

class FadeEffectComponent : Component, Pool.Poolable {

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
        val mapper = mapperFor<FadeEffectComponent>()
    }
}
