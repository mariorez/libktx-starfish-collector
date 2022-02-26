package starfishcollector.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import ktx.ashley.allOf
import starfishcollector.component.CameraComponent
import starfishcollector.component.TransformComponent

class CameraSystem : IteratingSystem(allOf(CameraComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {

        CameraComponent.mapper.get(entity).apply {

            val transform = TransformComponent.mapper.get(target)

            camera.position.set(transform.position.x, transform.position.y, 0f)

            camera.position.x = MathUtils.clamp(
                transform.position.x,
                camera.viewportWidth / 2,
                worldWidth - camera.viewportWidth / 2
            )

            camera.position.y = MathUtils.clamp(
                camera.position.y,
                camera.viewportHeight / 2,
                worldHeight - camera.viewportHeight / 2
            )

            camera.update()
        }
    }
}
