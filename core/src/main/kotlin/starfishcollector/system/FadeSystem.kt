package starfishcollector.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import ktx.ashley.allOf
import ktx.ashley.remove
import starfishcollector.component.FadeComponent
import starfishcollector.component.FadeComponent.Mode.IN
import starfishcollector.component.FadeComponent.Mode.OUT
import starfishcollector.component.RenderComponent

class FadeSystem : IteratingSystem(
    allOf(FadeComponent::class, RenderComponent::class).get()
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val sprite = RenderComponent.mapper.get(entity).sprite

        FadeComponent.mapper.get(entity).apply {

            val fadeAmount = (1f / Gdx.graphics.framesPerSecond) / duration

            when (mode) {
                IN -> {
                    alpha += fadeAmount
                    if (alpha >= 1f) cleanUp(entity, removeEntityOnEnd)
                }
                OUT -> {
                    alpha -= fadeAmount
                    if (alpha <= 0f) cleanUp(entity, removeEntityOnEnd)
                }
            }

            sprite.setAlpha(alpha)
        }
    }

    private fun cleanUp(entity: Entity, remove: Boolean) {
        entity.remove<FadeComponent>()
        if (remove) engine.removeEntity(entity)
    }
}
