package system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.ashley.allOf
import ktx.collections.gdxArrayOf
import component.AnimationComponent
import component.RenderComponent

class AnimationSystem : IteratingSystem(
    allOf(AnimationComponent::class, RenderComponent::class).get()
) {

    private val animationCache = mutableMapOf<TextureRegion, Animation<TextureRegion>>()

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val animationData = AnimationComponent.mapper.get(entity).apply {
            stateTime += deltaTime
        }

        val animationFrame = getAnimation(animationData).getKeyFrame(animationData.stateTime)

        RenderComponent.mapper.get(entity).sprite.apply {
            setRegion(animationFrame)
            setSize(animationFrame.regionWidth.toFloat(), animationFrame.regionHeight.toFloat())
        }
    }

    private fun getAnimation(animationData: AnimationComponent): Animation<TextureRegion> {

        return animationCache.getOrPut(animationData.region) {

            val regions = animationData.region.split(
                (animationData.region.regionWidth / animationData.frames),
                animationData.region.regionHeight
            )

            val textureArray = gdxArrayOf<TextureRegion>().apply {
                (0 until animationData.frames).forEach { col ->
                    add(TextureRegion(regions[0][col]))
                }
            }

            Animation(animationData.frameDuration, textureArray).apply {
                playMode = animationData.playMode
            }
        }.apply {
            playMode = animationData.playMode
        }
    }
}
