package starfishcollector.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class StarfishComponent : Component {
    companion object {
        val mapper = mapperFor<StarfishComponent>()
    }
}
