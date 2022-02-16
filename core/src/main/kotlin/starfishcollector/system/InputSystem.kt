package starfishcollector.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import starfishcollector.component.InputComponent
import starfishcollector.component.PlayerComponent
import starfishcollector.component.TransformComponent

class InputSystem : IteratingSystem(allOf(PlayerComponent::class).get()) {

    private val accelerate = Vector2()

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val playerInput = InputComponent.mapper.get(entity)

        TransformComponent.mapper.get(entity).apply {
            accelerate.set(acceleration, 0f).also {
                if (playerInput.right) accelerator.add(it.setAngleDeg(0f))
                if (playerInput.up) accelerator.add(it.setAngleDeg(90f))
                if (playerInput.left) accelerator.add(it.setAngleDeg(180f))
                if (playerInput.down) accelerator.add(it.setAngleDeg(270f))
            }
        }
    }
}
