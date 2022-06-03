package component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class BoundingBoxComponent : Component, Pool.Poolable {
    var polygon = Polygon()

    override fun reset() {
        polygon = Polygon()
    }

    companion object {
        val mapper = mapperFor<BoundingBoxComponent>()
    }
}