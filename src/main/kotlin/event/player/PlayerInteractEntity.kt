package event.player

import event.entity.ItemFrameInteract.itemFrameInteractEvent
import event.entity.SnifferInteract.snifferInteractEvent
import event.entity.VendingMachineInteract.vendingMachineInteractEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent

class PlayerInteractEntity : Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEntityEvent) {
        if (event.hand == org.bukkit.inventory.EquipmentSlot.HAND) { // The reason for this is really funny
            itemFrameInteractEvent(event)
            snifferInteractEvent(event)
            vendingMachineInteractEvent(event)
        }
    }
}