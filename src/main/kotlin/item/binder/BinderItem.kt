package item.binder

import chat.Formatting.allTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import util.Keys

/**
 * The physical binder item. Stores trading cards in its PDC as serialized ItemStacks.
 */
object BinderItem {
    const val MAX_CAPACITY = 180

    fun create(): ItemStack {
        return ItemStack(Material.BOOK).apply {
            editMeta { meta ->
                meta.displayName(
                    Component.text("Card Binder")
                        .color(TextColor.color(0x5b9df5))
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true)
                )
                updateLore(meta, 0)
                meta.persistentDataContainer.set(
                    Keys.BINDER_CARDS,
                    PersistentDataType.LIST.listTypeFrom(PersistentDataType.BYTE_ARRAY),
                    emptyList(),
                )
            }
        }
    }

    fun isBinder(item: ItemStack?): Boolean {
        if (item == null || item.isEmpty) return false
        return item.itemMeta?.persistentDataContainer?.has(Keys.BINDER_CARDS) == true
    }

    fun isCard(item: ItemStack?): Boolean {
        if (item == null || item.isEmpty) return false
        return item.itemMeta?.persistentDataContainer?.has(Keys.CARD_MOB_ID) == true
    }

    fun readCards(item: ItemStack): List<ItemStack> {
        val bytes = item.itemMeta
            ?.persistentDataContainer
            ?.get(Keys.BINDER_CARDS, PersistentDataType.LIST.listTypeFrom(PersistentDataType.BYTE_ARRAY))
            ?: return emptyList()

        return bytes.mapNotNull { runCatching { ItemStack.deserializeBytes(it) }.getOrNull() }
    }

    fun saveCards(player: Player, slot: EquipmentSlot, cards: List<ItemStack>) {
        val currentItem = when (slot) {
            EquipmentSlot.HAND -> player.inventory.itemInMainHand
            EquipmentSlot.OFF_HAND -> player.inventory.itemInOffHand
            else -> return
        }

        val updated = currentItem.clone()
        updated.editMeta { meta ->
            meta.persistentDataContainer.set(
                Keys.BINDER_CARDS,
                PersistentDataType.LIST.listTypeFrom(PersistentDataType.BYTE_ARRAY),
                cards.map { it.serializeAsBytes() },
            )
            updateLore(meta, cards.size)
        }

        when (slot) {
            EquipmentSlot.HAND -> player.inventory.setItemInMainHand(updated)
            EquipmentSlot.OFF_HAND -> player.inventory.setItemInOffHand(updated)
        }
    }

    private fun updateLore(meta: org.bukkit.inventory.meta.ItemMeta, cardCount: Int) {
        meta.lore(
            listOf(
                allTags.deserialize("<!i><gray>Cards: <white>$cardCount<gray>/$MAX_CAPACITY"),
                allTags.deserialize("<!i><dark_gray>Right-click to open"),
            )
        )
    }
}

