package item.booster

import chat.Formatting.allTags
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Equippable
import item.ItemRarity
import item.ItemRarity.*
import item.ItemType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType.STRING
import util.Keys.GENERIC_RARITY

@Suppress("UnstableApiUsage")
private fun createBoosterPackItem(
    displayName: String,
    rarity: ItemRarity,
    description: String,
    modelPath: String,
): ItemStack {
    return ItemStack(Material.PAPER).apply {
        editMeta { meta ->
            meta.displayName(
                Component.text(displayName)
                    .color(TextColor.color(rarity.color.asRGB()))
                    .decoration(TextDecoration.ITALIC, false)
            )
            meta.lore(
                buildList {
                    add(allTags.deserialize("<!i><white>${rarity.rarityGlyph}"))
                    description.split("\n").forEach { line ->
                        add(Component.text(line).decoration(TextDecoration.ITALIC, false))
                    }
                }
            )
            meta.persistentDataContainer.set(GENERIC_RARITY, STRING, rarity.name)
        }
        setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", modelPath))
    }
}

enum class BoosterPackItem(
    val rollWeight: Int,
    private val itemName: String,
    private val rarity: ItemRarity,
    private val itemDescription: String,
    private val modelPath: String,
) {
    TEST(10, "Penguin Plushie", COMMON, "A cute penguin plushie", "plushies/penguin");

    fun createItemStack(): ItemStack = createBoosterPackItem(itemName, rarity, itemDescription, modelPath)
}