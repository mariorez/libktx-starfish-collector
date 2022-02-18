package starfishcollector

import ktx.app.KtxScreen

abstract class Screen : KtxScreen {

    private val actionMap = mutableMapOf<Int, Action.Name>()

    fun registerAction(inputKey: Int, actionName: Action.Name) {
        actionMap[inputKey] = actionName
    }

    fun getActionMap(): MutableMap<Int, Action.Name> = actionMap

    abstract fun doAction(action: Action)
}
