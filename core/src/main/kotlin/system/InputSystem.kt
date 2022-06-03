package system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.Vector2
import component.AnimationComponent
import component.InputComponent
import component.TransformComponent

class InputSystem(
    private val player: Entity
) : EntitySystem() {

    private val speedUp = Vector2()
    private val playerInput = InputComponent.mapper.get(player)
    private val animation = AnimationComponent.mapper.get(player)

    override fun update(deltaTime: Float) {
        if (playerInput.isMoving) {
            TransformComponent.mapper.get(player).apply {
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
}
