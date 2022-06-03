@file:JvmName("Lwjgl3Launcher")

package starfishcollector.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import GameBoot

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(GameBoot(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Starfish Collector")
        setWindowedMode(GameBoot.WINDOW_WIDTH, GameBoot.WINDOW_HEIGHT)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
