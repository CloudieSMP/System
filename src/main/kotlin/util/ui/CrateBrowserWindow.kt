package util.ui

import chat.Formatting.allTags
import item.crate.Crate
import item.crate.CrateType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

private class CrateSelectorHolder : InventoryHolder {
    var backingInventory: Inventory? = null
    val slots = mutableMapOf<Int, CrateType>()

    override fun getInventory(): Inventory {
        return requireNotNull(backingInventory) { "Crate selector inventory has not been initialized." }
    }
}

private class CrateLootHolder(val crateType: CrateType) : InventoryHolder {
    var backingInventory: Inventory? = null

    override fun getInventory(): Inventory {
        return requireNotNull(backingInventory) { "Crate loot inventory has not been initialized." }
    }
}

object CrateBrowserWindow : Listener {
    private val fillerPane = ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
    private val backButton = ItemStack(Material.ARROW).apply {
        editMeta { meta ->
            meta.displayName(allTags.deserialize("<yellow><bold>Back to Crates"))
        }
    }

    fun openSelector(player: Player) {
        val holder = CrateSelectorHolder()
        val crateTypes = CrateType.entries
        val size = ((crateTypes.size + 8) / 9).coerceAtLeast(1) * 9
        val inventory = Bukkit.createInventory(holder, size, allTags.deserialize("<gradient:#DF6F69:#823BC6><bold>Crates</bold></gradient>"))
        holder.backingInventory = inventory

        for (slot in 0 until size) {
            inventory.setItem(slot, fillerPane)
        }

        crateTypes.forEachIndexed { index, crateType ->
            holder.slots[index] = crateType
            inventory.setItem(index, Crate.create(crateType).clone())
        }

        player.openInventory(inventory)
    }

    private fun openLootPreview(player: Player, crateType: CrateType) {
        val holder = CrateLootHolder(crateType)
        val loot = crateType.lootPool.possibleItems
        val size = ((loot.size + 1 + 8) / 9).coerceAtLeast(1) * 9
        val inventory = Bukkit.createInventory(holder, size, crateType.displayName)
        val backSlot = size - 1
        holder.backingInventory = inventory

        for (slot in 0 until size) {
            inventory.setItem(slot, fillerPane)
        }

        val totalWeight = loot.sumOf { it.pctChanceToRoll.coerceAtLeast(0) }

        loot.forEachIndexed { index, crateItem ->
            if (index == backSlot) return@forEachIndexed
            val sanitizedWeight = crateItem.pctChanceToRoll.coerceAtLeast(0)
            val actualChance = if (totalWeight > 0)
                sanitizedWeight.toDouble() / totalWeight * 100.0
            else 0.0
            val chanceText = "%.1f".format(actualChance)

            val preview = crateItem.itemStack.clone()
            preview.editMeta { meta ->
                val updatedLore = (meta.lore() ?: emptyList()) +
                    allTags.deserialize("<!i><gray>Chance: <white>$chanceText%")
                meta.lore(updatedLore)
            }
            inventory.setItem(index, preview)
        }

        inventory.setItem(backSlot, backButton.clone())

        player.openInventory(inventory)
    }

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val topInventory = event.view.topInventory

        when (val holder = topInventory.holder) {
            is CrateSelectorHolder -> {
                event.isCancelled = true
                if (event.clickedInventory != topInventory) return
                val crateType = holder.slots[event.slot] ?: return
                openLootPreview(player, crateType)
            }

            is CrateLootHolder -> {
                event.isCancelled = true
                if (event.clickedInventory != topInventory) return
                if (event.slot == topInventory.size - 1) {
                    openSelector(player)
                }
            }
        }
    }
}

