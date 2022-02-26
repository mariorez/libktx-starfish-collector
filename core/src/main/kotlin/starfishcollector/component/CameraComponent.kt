package starfishcollector.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.OrthographicCamera
import ktx.ashley.mapperFor

class CameraComponent : Component {

    lateinit var camera: OrthographicCamera
    lateinit var target: Entity
    var worldWidth = 0f
    var worldHeight = 0f

    companion object {
        val mapper = mapperFor<CameraComponent>()
    }
}
