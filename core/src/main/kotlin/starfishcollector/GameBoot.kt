package starfishcollector

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.app.KtxGame
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import starfishcollector.screen.FirstScreen

class GameBoot : KtxGame<KtxScreen>() {

    private lateinit var assets: AssetStorage

    companion object {
        const val SCREEN_WIDTH = 800
        const val SCREEN_HEIGHT = 600
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        Gdx.input.inputProcessor = InputMultiplexer(object : KtxInputAdapter {
            override fun keyDown(keycode: Int): Boolean {
                (currentScreen as Screen).apply {
                    getActionMap()[keycode]?.let { name ->
                        doAction(Action(name, Action.Type.START))
                    }
                }
                return super.keyDown(keycode)
            }

            override fun keyUp(keycode: Int): Boolean {
                (currentScreen as Screen).apply {
                    getActionMap()[keycode]?.let { name ->
                        doAction(Action(name, Action.Type.END))
                    }
                }
                return super.keyUp(keycode)
            }
        })

        KtxAsync.initiate()

        // ASSETS MANAGEMENT
        assets = AssetStorage().apply {
            loadSync<Texture>("large-water.jpg")
            loadSync<TextureAtlas>("starfish-collector.atlas")
        }

        // SCREEN MANAGEMENT
        addScreen(FirstScreen(assets))
        setScreen<FirstScreen>()
    }
}
