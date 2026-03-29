package item.booster

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Consumable
import io.papermc.paper.datacomponent.item.FoodProperties
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import util.Keys
import util.Sounds.SILENT

@Suppress("unstableApiUsage")
object BoosterPack {
    fun create(type: BoosterType): ItemStack {
        val boosterPack = ItemStack(Material.PAPER).apply {
            editMeta { meta ->
                meta.displayName(type.displayName)
                meta.lore(type.loreLines)
                meta.persistentDataContainer.set(Keys.BOOSTER_TYPE, PersistentDataType.STRING, type.storedId)
                meta.persistentDataContainer.set(Keys.GENERIC_RARITY, PersistentDataType.STRING, type.boosterRarity.name)
            }
            setData(DataComponentTypes.FOOD, FoodProperties.food().nutrition(0).saturation(0f).canAlwaysEat(true).build())
            setData(DataComponentTypes.CONSUMABLE, Consumable.consumable().consumeSeconds(1f).hasConsumeParticles(false).sound(SILENT.name()).build())
            setData(DataComponentTypes.ITEM_MODEL, NamespacedKey("cloudie", type.boosterMaterial))
        }

        return boosterPack
    }
}