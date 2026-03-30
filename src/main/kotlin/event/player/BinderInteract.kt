package event.player

import item.binder.BinderItem
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import util.ui.BinderWindow

class BinderInteract : Listener {
    // ignoreCancelled intentionally absent — other listeners may cancel the event
    // before our HIGH-priority handler runs (e.g. when clicking near blocks).
    @EventHandler(priority = EventPriority.HIGH)
    fun onRightClick(event: PlayerInteractEvent) {
        val action = event.action
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return

        // Only handle one hand per interaction to avoid the double-fire Paper emits for air clicks.
        val hand = event.hand ?: return

        // event.item can be null for air right-clicks in some Paper builds;
        // read directly from the player's inventory to be safe.
        val player = event.player
        val item = when (hand) {
            EquipmentSlot.HAND -> player.inventory.itemInMainHand
            EquipmentSlot.OFF_HAND -> player.inventory.itemInOffHand
            else -> return
        }

        if (!BinderItem.isBinder(item)) return

        event.isCancelled = true
        BinderWindow.open(player, hand)
    }
}

