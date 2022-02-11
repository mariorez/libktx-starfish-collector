package starfishcollector

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import ktx.app.KtxGame
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.assets.load
import ktx.async.KtxAsync
import starfishcollector.screen.FirstScreen

class GameBoot : KtxGame<KtxScreen>() {

    private val assets = AssetManager()

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        Gdx.input.inputProcessor = InputMultiplexer(object : KtxInputAdapter {

            override fun keyDown(keycode: Int): Boolean {
                val screen = currentScreen as Screen
                screen.getActionMap()[keycode]?.let {
                    screen.doAction(Action(it, Type.START))
                }
                return super.keyDown(keycode)
            }

            override fun keyUp(keycode: Int): Boolean {
                val screen = currentScreen as Screen
                screen.getActionMap()[keycode]?.let {
                    screen.doAction(Action(it, Type.END))
                }
                return super.keyUp(keycode)
            }
        })

        KtxAsync.initiate()

        // ASSETS MANAGEMENT
        assets.load<Texture>("turtle-1.png")
        assets.load<Texture>("large-water.jpg")
        assets.finishLoading()

        // SCREEN MANAGEMENT
        addScreen(FirstScreen(assets))
        setScreen<FirstScreen>()
    }
}
