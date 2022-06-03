package system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import component.RotateEffectComponent
import component.RotateEffectComponent.Direction.CLOCKWISE
import component.RotateEffectComponent.Direction.COUNTERCLOCKWISE
import component.TransformComponent

class RotateEffectSystem : IteratingSystem(
    allOf(RotateEffectComponent::class, TransformComponent::class).get()
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val rotate = RotateEffectComponent.mapper.get(entity)

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
