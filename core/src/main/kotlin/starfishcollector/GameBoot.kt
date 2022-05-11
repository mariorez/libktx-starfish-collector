package starfishcollector

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import ktx.app.KtxGame
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import starfishcollector.screen.FirstScreen

class GameBoot : KtxGame<KtxScreen>() {

    private lateinit var assets: AssetStorage

    companion object {
        const val WINDOW_WIDTH = 800
        const val WINDOW_HEIGHT = 600
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
            setLoader<TiledMap> { TmxMapLoader(fileResolver) }
            loadSync<TiledMap>("map.tmx")
            loadSync<TextureAtlas>("starfish-collector.atlas")
            loadSync<Texture>("starfish.png")
            loadSync<Texture>("rock.png")
            loadSync<Texture>("sign.png")
        }

        // SCREEN MANAGEMENT
        addScreen(FirstScreen(assets))
        setScreen<FirstScreen>()
    }
}
