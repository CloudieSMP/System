package item.booster

import item.CardRarity
import item.SubRarity
import logger
import org.bukkit.entity.EntityType
import java.util.Locale

/**
 * A fully-resolved trading card definition.
 * Built by [CardCatalog] at startup from the raw entries in [CardRegistry].
 */
data class CardDefinition(
    /** Unique snake_case ID, e.g. "creeper" or "desert_temple". */
    val id: String,
    /** Display name shown on the card item, e.g. "Creeper Card". */
    val displayName: String,
    /** Whether this is a Mob, Structure, or Item card. */
    val cardType: CardType,
    /** The in-game entity this card represents. Only set for MOB cards; null for all others. */
    val entityType: EntityType?,
    /** The card's base rarity, used for weighting during booster pulls. */
    val baseRarity: CardRarity,
    /** When true, this card can roll a SHINY, SHADOW, or OBFUSCATED sub-rarity on pull. */
    val canHaveSubRarity: Boolean,
    /** Which booster packs this card can appear in. */
    val allowedBoosters: Set<BoosterType>,
    /** Resource-pack model path for the plain (NONE) variant, e.g. "cards/mobs/creeper". */
    val modelPath: String,
    /** The base custom-model-data number (for the NONE sub-rarity). Sub-rarity variants add their offset on top. */
    val customModelData: Int,
) {
    /**
     * Returns the custom-model-data number for a specific sub-rarity variant of this card.
     *   NONE       → base number  (e.g. 100042)
     *   SHINY      → base + 1     (e.g. 100043)
     *   SHADOW     → base + 2     (e.g. 100044)
     *   OBFUSCATED → base + 3     (e.g. 100045)
     */
    fun modelDataFor(subRarity: SubRarity): Int = customModelData + subRarity.modelDataOffset

    /**
     * Returns the resource-pack model path for a specific sub-rarity variant.
     *   NONE       → "cards/mobs/creeper"
     *   SHINY      → "cards/mobs/creeper_shiny"
     *   SHADOW     → "cards/mobs/creeper_shadow"
     *   OBFUSCATED → "cards/mobs/creeper_obfuscated"
     */
    fun modelPathFor(subRarity: SubRarity): String =
        if (subRarity == SubRarity.NONE) modelPath else "${modelPath}_${subRarity.name.lowercase()}"
}

/**
 * Builds and holds the complete list of [CardDefinition]s at runtime.
 * Reads from [CardRegistry] — every entry there becomes one card here.
 *
 * The list is built lazily the first time it is accessed (usually on first booster open).
 */
object CardCatalog {
    val cards: List<CardDefinition> by lazy {
        // Track which custom-model-data numbers have already been used per card type,
        // so we never give two cards the same number.
        val usedModelData = CardType.entries.associateWith { mutableSetOf<Int>() }

        CardRegistry.cards.entries
            .sortedBy { it.key } // alphabetical order for stable custom-model-data assignment
            .mapNotNull { (cardId, entry) ->
                // MOB cards must match a real Bukkit EntityType that is alive and spawnable.
                // If the ID in CardRegistry is a typo, we log a warning and skip the card.
                val entityType: EntityType? = if (entry.type == CardType.MOB) {
                    EntityType.entries
                        .firstOrNull { it.key.key == cardId && it.isAlive && it.isSpawnable && it != EntityType.PLAYER }
                        ?: run {
                            logger.warning("CardRegistry: MOB '$cardId' does not match any alive spawnable EntityType — skipping.")
                            return@mapNotNull null
                        }
                } else null // STRUCTURE and ITEM cards have no entity type

                CardDefinition(
                    id = cardId,
                    displayName = "${cardId.toDisplayName()} Card",
                    cardType = entry.type,
                    entityType = entityType,
                    baseRarity = entry.rarity,
                    canHaveSubRarity = entry.canHaveSubRarity,
                    // Use the explicitly set booster list, or fall back to the rarity-based default.
                    allowedBoosters = entry.allowedBoosters ?: packsFor(entry.rarity),
                    modelPath = "cards/${entry.type.folderName}/$cardId",
                    customModelData = allocateCustomModelData(cardId, entry.type, usedModelData),
                )
            }
    }

    /** Returns all cards that are allowed to appear in the given [boosterType]. */
    fun eligibleCards(boosterType: BoosterType): List<CardDefinition> =
        cards.filter { boosterType in it.allowedBoosters }

    /** Finds the card for a specific Bukkit EntityType. Returns null if no card is registered for it. */
    fun findByEntityType(entityType: EntityType): CardDefinition? =
        cards.firstOrNull { it.entityType == entityType }

    /** Finds a card by its string ID (case-insensitive). Returns null if not found. */
    fun findById(id: String): CardDefinition? =
        cards.firstOrNull { it.id.equals(id, ignoreCase = true) }

    /**
     * Returns which booster packs a card of this rarity should appear in by default.
     * This is only used when [CardRegistry.CardEntry.allowedBoosters] is left null.
     *
     *   COMMON / UNCOMMON → Standard only
     *   RARE              → Standard + Epic
     *   EPIC / LEGENDARY  → Epic + Super
     *   MYTHIC and above  → Super only
     */
    private fun packsFor(rarity: CardRarity): Set<BoosterType> = when (rarity) {
        CardRarity.COMMON, CardRarity.UNCOMMON -> setOf(BoosterType.STANDARD)
        CardRarity.RARE                        -> setOf(BoosterType.STANDARD, BoosterType.EPIC)
        CardRarity.EPIC, CardRarity.LEGENDARY  -> setOf(BoosterType.EPIC, BoosterType.SUPER)
        CardRarity.MYTHIC,
        CardRarity.UNREAL,
        CardRarity.TRANSCENDENT,
        CardRarity.CELESTIAL,
        CardRarity.SPECIAL                     -> setOf(BoosterType.SUPER)
    }

    /** Converts a snake_case ID like "ender_dragon" into "Ender Dragon". */
    private fun String.toDisplayName(): String =
        split('_').joinToString(" ") { it.lowercase(Locale.ENGLISH).replaceFirstChar { c -> c.uppercase(Locale.ENGLISH) } }

    /**
     * Picks a unique block of consecutive custom-model-data numbers for this card within the type's range.
     *
     * Each card needs one slot per SubRarity variant (NONE, SHINY, SHADOW, OBFUSCATED = 4 slots).
     * The block's starting position is derived from a hash of the card ID for stability.
     * If that block is already taken by another card, we advance to the next block.
     *
     * Returns the first (base) slot of the claimed block. Sub-rarity variants are base + their offset.
     */
    private fun allocateCustomModelData(
        cardId: String,
        type: CardType,
        usedValues: Map<CardType, MutableSet<Int>>,
    ): Int {
        val set = usedValues.getValue(type)
        val slotsPerCard = SubRarity.entries.size          // 4 — one slot per sub-rarity variant
        val numBlocks = 99_999 / slotsPerCard              // how many card-blocks fit in the range
        val startBlock = Math.floorMod(cardId.hashCode(), numBlocks)
        var block = startBlock
        while (true) {
            val base = type.modelDataBase + block * slotsPerCard
            // Only use this block if all 4 slots are still free.
            if ((0 until slotsPerCard).none { base + it in set }) {
                (0 until slotsPerCard).forEach { set.add(base + it) }
                return base
            }
            block = (block + 1) % numBlocks
            if (block == startBlock) break // entire range is full — should never happen
        }
        return type.modelDataBase + block * slotsPerCard // last-resort fallback
    }
}
