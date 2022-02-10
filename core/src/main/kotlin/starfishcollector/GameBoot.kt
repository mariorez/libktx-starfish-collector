package starfishcollector

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.load
import ktx.async.KtxAsync

class GameBoot : KtxGame<KtxScreen>() {

    private val assets = AssetManager()

    override fun create() {
        KtxAsync.initiate()

        assets.load<Texture>("turtle-1.png")
        assets.finishLoading()

        addScreen(FirstScreen(assets))
        setScreen<FirstScreen>()
    }
}
