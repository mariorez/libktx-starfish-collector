package starfishcollector.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import starfishcollector.component.RotateComponent
import starfishcollector.component.RotateComponent.Direction.CLOCKWISE
import starfishcollector.component.RotateComponent.Direction.COUNTERCLOCKWISE
import starfishcollector.component.TransformComponent

class RotateSystem : IteratingSystem(
    allOf(RotateComponent::class, TransformComponent::class).get()
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val rotate = RotateComponent.mapper.get(entity)

        TransformComponent.mapper.get(entity).apply {
            when (rotate.direction) {
                CLOCKWISE -> {
                    if (rotation <= 0) rotation = 360f
                    else rotation -= rotate.speed
                }
                COUNTERCLOCKWISE -> {
                    if (rotation > 360) rotation -= 360
                    else rotation += rotate.speed
                }
            }
        }
    }
}
