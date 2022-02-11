package starfishcollector.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import starfishcollector.component.InputComponent
import starfishcollector.component.TransformComponent

class MovementSystem : IteratingSystem(allOf(TransformComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (InputComponent.mapper.has(entity)) {
            val speed = Vector2()
            val playerInput = InputComponent.mapper.get(entity)

            if (playerInput.up) speed.y += 3f
            if (playerInput.down) speed.y -= 3f
            if (playerInput.left) speed.x -= 3f
            if (playerInput.right) speed.x += 3f

            val velocity = TransformComponent.mapper.get(entity).velocity
            velocity.set(speed.x, speed.y)
            TransformComponent.mapper.get(entity).position.add(velocity)
        }
    }
}
