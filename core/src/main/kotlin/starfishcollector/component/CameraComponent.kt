package starfishcollector.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.OrthographicCamera
import ktx.ashley.mapperFor

class CameraComponent : Component {

    lateinit var camera: OrthographicCamera
    lateinit var target: Entity

    companion object {
        val mapper = mapperFor<CameraComponent>()
    }
}
