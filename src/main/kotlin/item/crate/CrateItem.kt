package item.crate

import chat.Formatting.allTags
import io.papermc.paper.datacomponent.DataComponentTypes
import item.ItemRarity.*
import item.ItemType
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import util.Keys.GENERIC_RARITY
import org.bukkit.persistence.PersistentDataType.*

/**
 * @param pctChanceToRoll How likely this item is to be rolled as a percentage (Int 0-100)
 * @param itemStack The actual itemStack of a single crate item
 */
@Suppress("UnstableApiUsage")
enum class CrateItem(val pctChanceToRoll: Int, val itemStack: ItemStack) {
    PENGUIN(10,
        createPlushie("Penguin", "plushies/penguin_plushie")
    ),
    MUSHROOM(10,
        createPlushie("Mushroom", "plushies/mushroom_plushie")
    ),
    BEE(10,
        createPlushie("Bee", "plushies/bee_plushie")
    ),
    STAR(10,
        createPlushie("Star", "plushies/star_plushie")
    ),
    HEART(10,
        ItemStack(Material.PAPER).apply {
            val plushieMeta = this.itemMeta
            plushieMeta.displayName(allTags.deserialize("<!i><red>Heart"))
            plushieMeta.lore(listOf(
                allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                allTags.deserialize("<!i><yellow>A Plushie.")
            ))
            plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
            this.itemMeta = plushieMeta
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", "plushies/heart_plushie"))
        }
    ),
    SEBIANN(10,
        createPlushie("Sebiann", "plushies/sebiann")
    );

    companion object {
        private fun createPlushie(name: String, modelKey: String): ItemStack {
            return ItemStack(Material.PAPER).apply {
                val plushieMeta = this.itemMeta
                plushieMeta.displayName(allTags.deserialize("<!i><red>$name"))
                plushieMeta.lore(listOf(
                    allTags.deserialize("<!i><white>${COMMON.rarityGlyph}${ItemType.PLUSHIE.typeGlyph}"),
                    allTags.deserialize("<!i><yellow>A Plushie.")
                ))
                plushieMeta.persistentDataContainer.set(GENERIC_RARITY, STRING, COMMON.name)
                this.itemMeta = plushieMeta
                setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", modelKey))
            }
        }
    }
}