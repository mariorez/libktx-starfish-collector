@file:JvmName("Lwjgl3Launcher")

package starfishcollector.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import starfishcollector.GameBoot

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(GameBoot(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Starfish Collector")
        setWindowedMode(GameBoot.SCREEN_WIDTH, GameBoot.SCREEN_HEIGHT)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
