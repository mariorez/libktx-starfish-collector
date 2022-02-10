package starfishcollector.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import ktx.ashley.allOf
import ktx.ashley.get
import starfishcollector.component.RenderComponent
import starfishcollector.component.TransformComponent

class RenderingSystem(
    private val batch: Batch
) : SortedIteratingSystem(
    allOf(RenderComponent::class, TransformComponent::class).get(),
    compareBy { it[TransformComponent.mapper] }
) {

    override fun update(deltaTime: Float) {
        batch.begin()
        super.update(deltaTime)
        batch.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val sprite = RenderComponent.mapper.get(entity).sprite
        val position = TransformComponent.mapper.get(entity).position

        sprite.run {
            setBounds(
                position.x,
                position.y,
                texture.width.toFloat(),
                texture.height.toFloat()
            )
            draw(batch)
        }
    }
}
