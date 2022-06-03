package system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer
import ktx.ashley.allOf
import ktx.ashley.get
import component.RenderComponent
import component.TransformComponent

class RenderingSystem(
    private val batch: Batch,
    private val camera: OrthographicCamera,
    private val mapRenderer: OrthoCachedTiledMapRenderer
) : SortedIteratingSystem(
    allOf(RenderComponent::class, TransformComponent::class).get(),
    compareBy { it[TransformComponent.mapper] }
) {

    override fun update(deltaTime: Float) {
        mapRenderer.setView(camera)
        mapRenderer.render()
        batch.projectionMatrix = camera.combined
        batch.begin()
        super.update(deltaTime)
        batch.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val sprite = RenderComponent.mapper.get(entity).sprite
        val transform = TransformComponent.mapper.get(entity)

        sprite.apply {
            setOriginCenter()
            rotation = transform.rotation
            setBounds(
                transform.position.x,
                transform.position.y,
                width,
                height
            )
            draw(batch)
        }
    }
}
