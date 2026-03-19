package item.crate

import chat.Formatting.allTags
import io.papermc.paper.datacomponent.DataComponentTypes
import item.ItemRarity.COMMON
import item.ItemType
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import util.Keys.GENERIC_RARITY
import org.bukkit.persistence.PersistentDataType.STRING

@Suppress("UnstableApiUsage")
internal fun createPlushie(name: String, modelKey: String): ItemStack {
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

