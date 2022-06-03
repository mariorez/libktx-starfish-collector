package component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class SignComponent : Component {
    companion object {
        val mapper = mapperFor<SignComponent>()
    }
}
