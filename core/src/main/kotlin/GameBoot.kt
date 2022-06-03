import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import ktx.app.KtxGame
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import Action.Type.END
import Action.Type.START
import screen.GameScreen

class GameBoot : KtxGame<KtxScreen>() {

    companion object {
        const val WINDOW_WIDTH = 800
        const val WINDOW_HEIGHT = 600
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        Gdx.input.inputProcessor = InputMultiplexer(object : KtxInputAdapter {
            override fun keyDown(keycode: Int): Boolean {
                (currentScreen as BaseScreen).apply {
                    getActionMap()[keycode]?.let { doAction(Action(it, START)) }
                }
                return super.keyDown(keycode)
            }

            override fun keyUp(keycode: Int): Boolean {
                (currentScreen as BaseScreen).apply {
                    getActionMap()[keycode]?.let { doAction(Action(it, END)) }
                }
                return super.keyUp(keycode)
            }
        })

        KtxAsync.initiate()

        // ASSETS MANAGEMENT
        val assets = AssetStorage().apply {
            setLoader<TiledMap> { TmxMapLoader(fileResolver) }
            loadSync<TiledMap>("map.tmx")
            loadSync<TextureAtlas>("starfish-collector.atlas").apply {
                textures.forEach { it.setFilter(Linear, Linear) }
            }
            loadSync<Texture>("starfish.png").setFilter(Linear, Linear)
            loadSync<Texture>("rock.png").setFilter(Linear, Linear)
            loadSync<Texture>("sign.png").setFilter(Linear, Linear)
        }

        val labelStyle = LabelStyle().apply { font = fontGenerator() }

        // SCREEN MANAGEMENT
        addScreen(GameScreen(assets, labelStyle))
        setScreen<GameScreen>()
    }

    private fun fontGenerator(): BitmapFont {
        return FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"))
            .generateFont(
                FreeTypeFontParameter().apply {
                    size = 38
                    color = Color.WHITE
                    borderColor = Color.BLACK
                    borderWidth = 2f
                    borderStraight = true
                    minFilter = Linear
                    magFilter = Linear
                })
    }
}
