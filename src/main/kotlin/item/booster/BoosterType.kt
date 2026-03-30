package item.booster

import chat.Formatting.allTags
import item.CardRarity
import item.ItemRarity
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
 * @param minimumCardRarity Guaranteed minimum card rarity this booster can roll
 * @param luckModifier Multiplier applied to rarities above the guaranteed minimum
 */
enum class BoosterType(
    private val boosterName: String,
    private val boosterDescription: String,
    val boosterRarity: ItemRarity,
    val boosterMaterial: String,
    val minimumCardRarity: CardRarity,
    val luckModifier: Double,
) {
    STANDARD(
        "Standard Booster",
        "A balanced pack with a broad card pool",
        ItemRarity.COMMON,
        "boosters/standard_booster_pack",
        CardRarity.COMMON,
        1.0,
    ),
    EPIC(
        "Epic Booster",
        "Higher floor and boosted odds for stronger cards",
        ItemRarity.RARE,
        "boosters/epic_booster_pack",
        CardRarity.RARE,
        3.0,
    ),
    SUPER(
        "Super Booster",
        "High-stakes pack with top-tier drop odds",
        ItemRarity.EPIC,
        "boosters/super_booster_pack",
        CardRarity.EPIC,
        5.0,
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
        get() = "${name.lowercase()}_booster"

    companion object {
        fun fromStoredId(storedId: String?): BoosterType? {
            return BoosterType.entries.firstOrNull { it.storedId == storedId }
        }
    }
}