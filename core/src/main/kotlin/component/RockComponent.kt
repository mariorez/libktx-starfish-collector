package component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class RockComponent : Component {
    companion object {
        val mapper = mapperFor<RockComponent>()
    }
}
