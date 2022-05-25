package starfishcollector.listener

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener

class StarfishCounter : EntityListener {

    companion object {
        private var counter = 0
        fun getStarfishCounter(): Int = counter
    }

    override fun entityAdded(entity: Entity) {
        counter++
    }

    override fun entityRemoved(entity: Entity) {
        counter--
    }
}
