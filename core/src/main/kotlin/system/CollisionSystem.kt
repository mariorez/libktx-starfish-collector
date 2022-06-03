package system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.Polygon
import component.AnimationComponent
import component.BoundingBoxComponent
import component.FadeEffectComponent
import component.PlayerComponent
import component.RenderComponent
import component.RockComponent
import component.SignComponent
import component.StarfishComponent
import component.TransformComponent
import ktx.ashley.add
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.remove
import ktx.ashley.with
import ktx.assets.async.AssetStorage

class CollisionSystem(
    private val player: Entity,
    private val assets: AssetStorage
) : IteratingSystem(
    allOf(BoundingBoxComponent::class).exclude(PlayerComponent::class.java).get()
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val playerSprite = RenderComponent.mapper.get(player).sprite
        val playerBox = BoundingBoxComponent.mapper.get(player).polygon.apply {
            setPosition(playerSprite.x, playerSprite.y)
            setOrigin(playerSprite.originX, playerSprite.originY)
            rotation = playerSprite.rotation
            setScale(playerSprite.scaleX, playerSprite.scaleY)
        }

        val currentSprite = RenderComponent.mapper.get(entity).sprite
        val currentBox = BoundingBoxComponent.mapper.get(entity).polygon

        val mtv = MinimumTranslationVector()

        if (!overlaps(playerBox, currentBox, mtv)) return

        if (RockComponent.mapper.has(entity) || SignComponent.mapper.has(entity)) {
            TransformComponent.mapper.get(player).apply {
                position.x += mtv.normal.x * mtv.depth
                position.y += mtv.normal.y * mtv.depth
            }
        }

        if (StarfishComponent.mapper.has(entity)) {
            entity.apply {
                remove<BoundingBoxComponent>()
                add(FadeEffectComponent().apply { removeEntityOnEnd = true })
            }
            engine.add {
                entity {
                    with<RenderComponent>()
                    with<TransformComponent> {
                        position.x = currentSprite.x - 15
                        position.y = currentSprite.y - 7
                    }
                    with<AnimationComponent> {
                        region = assets
                            .get<TextureAtlas>("starfish-collector.atlas")
                            .findRegion("whirlpool")
                        frames = 10
                        frameDuration = 0.1f
                    }
                    with<FadeEffectComponent> {
                        removeEntityOnEnd = true
                    }
                }
            }
        }
    }

    private fun overlaps(playerBox: Polygon, otherBox: Polygon, mtv: MinimumTranslationVector): Boolean {
        // initial test to improve performance
        if (playerBox.boundingRectangle.overlaps(otherBox.boundingRectangle)) {
            return Intersector.overlapConvexPolygons(playerBox, otherBox, mtv)
        }
        return false
    }
}
