package item.booster

import chat.Formatting.allTags
import item.ItemRarity
import item.ItemRarity.*
import item.ItemType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

/**
 * Boosters
 * @param boosterName The display name of the booster
 * @param boosterDescription The description of the booster
 * @param boosterRarity The rarity of the booster, which determines the color of the booster's name and lore
 * @param boosterMaterial The item model path used for this booster
 * @param lootPool The loot pool to pull from when generating booster rewards
 */
enum class BoosterType(
    private val boosterName: String,
    private val boosterDescription: String,
    val boosterRarity: ItemRarity,
    val boosterMaterial: String,
    val lootPool: BoosterLootPool,
) {
    TEST(
        "Test Booster",
        "A booster used for testing",
        COMMON,
        "boosters/standard_booster_pack",
        BoosterLootPool.STANDARD,
    );

    val displayName: Component
        get() = Component.text(boosterName)
            .color(TextColor.color(boosterRarity.color.asRGB()))
            .decoration(TextDecoration.ITALIC, false)
            .decoration(TextDecoration.BOLD, true)

    val loreLines: List<Component>
        get() = listOf(
            allTags.deserialize("<reset><!i><white>${boosterRarity.rarityGlyph}${ItemType.CONSUMABLE.typeGlyph}"),
            Component.text(boosterDescription)
                .color(TextColor.color(0xFFFF55))
                .decoration(TextDecoration.ITALIC, false),
        )
    val storedId: String
        get() = name

    val recipeKey: String
        get() = "${name.lowercase()}_crate"

    companion object {
        fun fromStoredId(storedId: String?): BoosterType? {
            return BoosterType.entries.firstOrNull { it.storedId == storedId }
        }
    }
}