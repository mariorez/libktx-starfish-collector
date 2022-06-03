package system

import WorldSize
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import component.RenderComponent
import component.TransformComponent

class MovementSystem(
    private val player: Entity,
    private val worldSize: WorldSize
) : EntitySystem() {

    override fun update(deltaTime: Float) {

        TransformComponent.mapper.get(player).apply {
            // apply acceleration
            velocity.add(
                accelerator.x * deltaTime,
                accelerator.y * deltaTime
            )

            var speed = velocity.len()

            // decrease speed (decelerate) when not accelerating
            if (accelerator.len() == 0f) {
                speed -= deceleration * deltaTime
            }

            // keep speed within set bounds
            speed = MathUtils.clamp(speed, 0f, maxSpeed)

            // update velocity
            if (velocity.len() == 0f) {
                velocity.set(speed, 0f)
            } else {
                velocity.setLength(speed)
            }

            // move by
            if (velocity.x != 0f || velocity.y != 0f) {
                position.add(velocity.x * deltaTime, velocity.y * deltaTime)
                boundToWorld(
                    position,
                    RenderComponent.mapper.get(player).sprite.width,
                    RenderComponent.mapper.get(player).sprite.height
                )
            }

            // set rotation when moving
            if (velocity.len() > 0) {
                rotation = velocity.angleDeg()
            }

            // reset acceleration
            accelerator.set(0f, 0f)
        }
    }

    private fun boundToWorld(position: Vector2, entityWidth: Float, entityHeight: Float) {
        if (position.x < 0f) position.x = 0f
        if (position.x + entityWidth > worldSize.width) position.x = worldSize.width - entityWidth
        if (position.y < 0f) position.y = 0f
        if (position.y + entityHeight > worldSize.height) position.y = worldSize.height - entityHeight
    }
}
