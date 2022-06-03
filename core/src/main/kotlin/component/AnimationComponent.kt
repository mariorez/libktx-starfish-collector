package component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode.NORMAL
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool.Poolable
import ktx.ashley.mapperFor

class AnimationComponent : Component, Poolable {

    var region = TextureRegion()
    var stateTime = 0f
    var playMode = NORMAL
    var frames = 1
    var frameDuration = 0f

    override fun reset() {
        region = TextureRegion()
        stateTime = 0f
        playMode = NORMAL
        frames = 1
        frameDuration = 1f
    }

    companion object {
        val mapper = mapperFor<AnimationComponent>()
    }
}
