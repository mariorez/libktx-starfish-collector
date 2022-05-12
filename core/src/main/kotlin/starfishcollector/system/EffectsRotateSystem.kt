package starfishcollector.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import starfishcollector.component.EffectsRotateComponent
import starfishcollector.component.EffectsRotateComponent.Direction.CLOCKWISE
import starfishcollector.component.TransformComponent

class EffectsRotateSystem : IteratingSystem(allOf(EffectsRotateComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val rotate = EffectsRotateComponent.mapper.get(entity)

        TransformComponent.mapper.get(entity).apply {
            if (rotate.direction == CLOCKWISE) {
                if (rotation <= 0) rotation = 360f
                else rotation -= rotate.speed
            } else {
                if (rotation > 360) rotation -= 360
                else rotation += rotate.speed
            }
        }
    }
}
