package starfishcollector

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class GameBoot : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()

        addScreen(FirstScreen())
        setScreen<FirstScreen>()
    }
}
