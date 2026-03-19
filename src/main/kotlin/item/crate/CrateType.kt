package item.crate

import chat.Formatting.allTags
import item.ItemRarity
import item.ItemRarity.*
import item.ItemType
import net.kyori.adventure.text.Component

/**
 * Crates
 * @property displayName The display name of the crate type.
 * @param loreLines The lore that should be attached to the bag
 * @param crateRarity The rarity of the crate, which determines the color of the crate's name and lore
 * @param crateMaterial The material that the crate should be represented by in item form
 * @param lootPool The loot pool to pull from when generating bag contents
 */
enum class CrateType(val displayName : Component, val loreLines: List<Component>, val crateRarity: ItemRarity, val crateMaterial: String, val lootPool: CrateLootPool) {
    PLUSHIE(
    allTags.deserialize("<gradient:#DF6F69:#C45889:#823BC6><bold>Plushie Crate</bold></gradient>"),
    listOf(
        allTags.deserialize("<reset><!i><white>${COMMON.rarityGlyph}${ItemType.CONSUMABLE.typeGlyph}"),
        allTags.deserialize("<reset><!i><yellow>IDK YET.")
    ),
        COMMON,
        "crates/blue",
        CrateLootPool.PLUSHIE
    ),
    WEARABLES(
        allTags.deserialize("<gradient:#DF6F69:#C45889:#823BC6><bold>Wearables Crate</bold></gradient>"),
        listOf(
            allTags.deserialize("<reset><!i><white>${COMMON.rarityGlyph}${ItemType.CONSUMABLE.typeGlyph}"),
            allTags.deserialize("<reset><!i><yellow>IDK YET.")
        ),
        COMMON,
        "crates/default",
        CrateLootPool.WEARABLES
    ),
    PLAYER(
        allTags.deserialize("<gradient:#DF6F69:#C45889:#823BC6><bold>Player Crate</bold></gradient>"),
        listOf(
            allTags.deserialize("<reset><!i><white>${UNCOMMON.rarityGlyph}${ItemType.CONSUMABLE.typeGlyph}"),
            allTags.deserialize("<reset><!i><yellow>IDK YET.")
        ),
        UNCOMMON,
        "crates/default",
        CrateLootPool.PLAYER
    )
}