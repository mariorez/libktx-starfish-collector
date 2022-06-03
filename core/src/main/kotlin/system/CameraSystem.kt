package system

import WorldSize
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils.clamp
import component.RenderComponent
import component.TransformComponent

class CameraSystem(
    private val camera: OrthographicCamera,
    private val player: Entity,
    private val worldSize: WorldSize
) : EntitySystem() {

    private val sprite = RenderComponent.mapper.get(player).sprite
    private val middleWidth = camera.viewportWidth / 2
    private val middleHeight = camera.viewportHeight / 2
    private val middlePlayerWidth = sprite.width / 2
    private val middlePlayerHeight = sprite.height / 2

    override fun update(deltaTime: Float) {
        TransformComponent.mapper.get(player).position.apply {
            camera.position.x = clamp(x + middlePlayerWidth, middleWidth, worldSize.width - middleWidth)
            camera.position.y = clamp(y + middlePlayerHeight, middleHeight, worldSize.height - middleHeight)
            camera.update()
        }
    }
}
