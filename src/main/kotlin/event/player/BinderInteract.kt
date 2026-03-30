package event.player

import item.binder.BinderItem
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.EquipmentSlot
import util.ui.BinderWindow
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class BinderInteract : Listener {
    private val openBinders: MutableSet<UUID> = ConcurrentHashMap.newKeySet()

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
        openBinders += player.uniqueId
        BinderWindow.open(player, hand)
    }

    @EventHandler(ignoreCancelled = true)
    fun onDrop(event: PlayerDropItemEvent) {
        if (event.player.uniqueId !in openBinders) return
        if (BinderItem.isBinder(event.itemDrop.itemStack)) {
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? org.bukkit.entity.Player ?: return
        if (player.uniqueId !in openBinders) return

        if (BinderItem.isBinder(event.currentItem) || BinderItem.isBinder(event.cursor)) {
            event.isCancelled = true
            return
        }

        // Number-key swaps can move the binder without it being the clicked/cursor item.
        if (event.click == ClickType.NUMBER_KEY) {
            val hotbarSlot = event.hotbarButton
            if (hotbarSlot in 0..8 && BinderItem.isBinder(player.inventory.getItem(hotbarSlot))) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        val player = event.player as? org.bukkit.entity.Player ?: return
        openBinders.remove(player.uniqueId)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        openBinders.remove(event.player.uniqueId)
    }
}

