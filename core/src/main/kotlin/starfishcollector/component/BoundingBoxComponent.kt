package starfishcollector.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import starfishcollector.component.BoundingBoxComponent.BoxType.NONE

class BoundingBoxComponent : Component, Pool.Poolable {

    enum class BoxType {
        NONE,
        TURTLE,
        STARFISH,
        ROCK,
        SIGN
    }

    var type = NONE
    var polygon = Polygon()

    override fun reset() {
        type = NONE
        polygon = Polygon()
    }

    companion object {
        val mapper = mapperFor<BoundingBoxComponent>()
    }
}