package starfishcollector.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import starfishcollector.component.AnimationComponent
import starfishcollector.component.InputComponent
import starfishcollector.component.PlayerComponent
import starfishcollector.component.TransformComponent

class InputSystem : IteratingSystem(allOf(PlayerComponent::class).get()) {

    private val speedUp = Vector2()

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val playerInput = InputComponent.mapper.get(entity)
        val animation = AnimationComponent.mapper.get(entity)

        if (isMoving(playerInput)) {
            TransformComponent.mapper.get(entity).apply {
                speedUp.set(acceleration, 0f).also { speed ->
                    if (playerInput.right) accelerator.add(speed.setAngleDeg(0f))
                    if (playerInput.up) accelerator.add(speed.setAngleDeg(90f))
                    if (playerInput.left) accelerator.add(speed.setAngleDeg(180f))
                    if (playerInput.down) accelerator.add(speed.setAngleDeg(270f))
                }
            }

            animation.playMode = PlayMode.LOOP
        } else {
            animation.playMode = PlayMode.NORMAL
        }
    }

    private fun isMoving(input: InputComponent): Boolean {
        return input.up || input.down || input.left || input.right
    }
}
