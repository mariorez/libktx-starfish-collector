package starfishcollector.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import starfishcollector.component.PlayerComponent
import starfishcollector.component.RenderComponent
import starfishcollector.component.TransformComponent
import starfishcollector.component.WorldComponent

class MovementSystem : IteratingSystem(allOf(PlayerComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {

        TransformComponent.mapper.get(entity).apply {
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
                    RenderComponent.mapper.get(entity).sprite,
                    WorldComponent.mapper.get(entity)
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

    private fun boundToWorld(position: Vector2, sprite: Sprite, world: WorldComponent) {
        if (position.x < 0f) position.x = 0f
        if (position.x + sprite.width > world.width) position.x = world.width - sprite.width
        if (position.y < 0f) position.y = 0f
        if (position.y + sprite.height > world.height) position.y = world.height - sprite.height
    }
}
