package starfishcollector

import ktx.app.KtxScreen

abstract class Screen : KtxScreen {

    private val actionMap = mutableMapOf<Int, Name>()

    fun registerAction(inputKey: Int, actionName: Name) {
        actionMap[inputKey] = actionName
    }

    fun getActionMap(): MutableMap<Int, Name> = actionMap

    abstract fun doAction(action: Action)
}
