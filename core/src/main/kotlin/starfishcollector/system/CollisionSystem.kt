package starfishcollector.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.Polygon
import ktx.ashley.allOf
import starfishcollector.component.BoundingBoxComponent
import starfishcollector.component.BoundingBoxComponent.BoxType.ROCK
import starfishcollector.component.BoundingBoxComponent.BoxType.SIGN
import starfishcollector.component.BoundingBoxComponent.BoxType.STARFISH
import starfishcollector.component.PlayerComponent
import starfishcollector.component.RenderComponent
import starfishcollector.component.TransformComponent

class CollisionSystem(
    private val player: Entity
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
        val boxComponent = BoundingBoxComponent.mapper.get(entity)
        val currentBox = boxComponent.polygon.apply {
            setPosition(currentSprite.x, currentSprite.y)
            setOrigin(currentSprite.originX, currentSprite.originY)
            rotation = currentSprite.rotation
            setScale(currentSprite.scaleX, currentSprite.scaleY)
        }

        val mtv = MinimumTranslationVector()

        if (!overlaps(playerBox, currentBox, mtv)) return

        if (boxComponent.type == ROCK || boxComponent.type == SIGN) {
            TransformComponent.mapper.get(player).apply {
                position.x += mtv.normal.x * mtv.depth
                position.y += mtv.normal.y * mtv.depth
            }
        }

        if (boxComponent.type == STARFISH) {
            engine.removeEntity(entity)
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
