package starfishcollector.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import starfishcollector.component.InputComponent
import starfishcollector.component.PlayerComponent
import starfishcollector.component.TransformComponent

class InputSystem : IteratingSystem(allOf(PlayerComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val playerInput = InputComponent.mapper.get(entity)

        TransformComponent.mapper.get(entity).apply {
            if (playerInput.right) accelerator.add(Vector2(acceleration, 0f)).setAngleDeg(0f)
            if (playerInput.up) accelerator.add(Vector2(acceleration, 0f)).setAngleDeg(90f)
            if (playerInput.left) accelerator.add(Vector2(acceleration, 0f)).setAngleDeg(180f)
            if (playerInput.down) accelerator.add(Vector2(acceleration, 0f)).setAngleDeg(270f)
        }
    }
}
