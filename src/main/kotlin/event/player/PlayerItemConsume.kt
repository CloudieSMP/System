package event.player

import item.crate.CrateType
import logger
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.persistence.PersistentDataType
import util.Keys.CRATE_TYPE
import util.ui.GamblingWindow

class PlayerItemConsume : Listener {

    @EventHandler
    fun playerItemConsume(event: PlayerItemConsumeEvent) {
        val crateType = event.item.persistentDataContainer.get(CRATE_TYPE, PersistentDataType.STRING)
        if (crateType != null) {
            val type = CrateType.fromStoredId(crateType)
            if (type == null) {
                logger.warning("Unknown crate type '$crateType' on consumed item from ${event.player.name}")
                return
            }
            GamblingWindow.open(event.player, type)
        }
    }
}