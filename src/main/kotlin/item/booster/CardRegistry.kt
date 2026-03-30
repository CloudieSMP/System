package item.booster

import item.CardRarity
import item.CardRarity.*

/**
 * ─────────────────────────────────────────────────────────────────────────────
 *  CARD REGISTRY  –  the only file you need to edit to add or change cards
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * Every line in the [cards] map below is one trading card.
 * Cards that are NOT listed here simply do not exist in the game.
 *
 * HOW TO ADD A CARD
 * ─────────────────
 * Pick a unique snake_case ID and add a line inside the mapOf block:
 *
 *   "my_card_id" to CardEntry(CardType.MOB, RARE),
 *
 * For MOB cards the ID must exactly match the Bukkit EntityType key
 * (e.g. "ender_dragon", "creeper"). You can look these up with /debug card.
 * For STRUCTURE and ITEM cards the ID can be anything you choose.
 *
 * CARD ENTRY FIELDS
 * ─────────────────
 *   type            – required. CardType.MOB, STRUCTURE, or ITEM.
 *   rarity          – required. The card's base rarity (COMMON, RARE, LEGENDARY, etc.)
 *   canHaveSubRarity – optional, default false.
 *                      Set to true if this card should be able to roll
 *                      a SHINY, SHADOW, or OBFUSCATED variant on pull.
 *   allowedBoosters – optional, default null (= auto-chosen based on rarity).
 *                      Override to pin the card to specific booster types, e.g.:
 *                      allowedBoosters = setOf(BoosterType.SUPER)
 */
object CardRegistry {
    data class CardEntry(
        val type: CardType,
        val rarity: CardRarity,
        /** Whether this card can roll a SHINY, SHADOW, or OBFUSCATED variant. Default: false. */
        val canHaveSubRarity: Boolean = false,
        /** The booster packs this card can appear in. null = derived automatically from rarity. */
        val allowedBoosters: Set<BoosterType>? = null,
    )

    val cards: Map<String, CardEntry> = mapOf(
        // ── Mob cards — Bosses ────────────────────────────────────────────
        "ender_dragon"     to CardEntry(CardType.MOB, LEGENDARY, allowedBoosters = setOf(BoosterType.SUPER)),
        "wither"           to CardEntry(CardType.MOB, LEGENDARY, allowedBoosters = setOf(BoosterType.SUPER)),
        "elder_guardian"   to CardEntry(CardType.MOB, EPIC,      allowedBoosters = setOf(BoosterType.EPIC, BoosterType.SUPER)),
        "warden"           to CardEntry(CardType.MOB, EPIC,      allowedBoosters = setOf(BoosterType.EPIC, BoosterType.SUPER)),

        // ── Mob cards — Hostile ───────────────────────────────────────────
        "creeper"          to CardEntry(CardType.MOB, COMMON),
        "zombie"           to CardEntry(CardType.MOB, COMMON),
        "skeleton"         to CardEntry(CardType.MOB, COMMON),
        "spider"           to CardEntry(CardType.MOB, COMMON),
        "enderman"         to CardEntry(CardType.MOB, UNCOMMON),
        "blaze"            to CardEntry(CardType.MOB, UNCOMMON),
        "witch"            to CardEntry(CardType.MOB, UNCOMMON),
        "guardian"         to CardEntry(CardType.MOB, UNCOMMON),

        // ── Mob cards — Passive / neutral ─────────────────────────────────
        "cow"              to CardEntry(CardType.MOB, COMMON),
        "pig"              to CardEntry(CardType.MOB, COMMON),
        "sheep"            to CardEntry(CardType.MOB, COMMON),
        "chicken"          to CardEntry(CardType.MOB, COMMON),
        "horse"            to CardEntry(CardType.MOB, COMMON),
        "wolf"             to CardEntry(CardType.MOB, COMMON),
        "cat"              to CardEntry(CardType.MOB, COMMON),
        "villager"         to CardEntry(CardType.MOB, COMMON),
        "iron_golem"       to CardEntry(CardType.MOB, UNCOMMON),
        "snow_golem"       to CardEntry(CardType.MOB, UNCOMMON),
        "wandering_trader" to CardEntry(CardType.MOB, UNCOMMON),

        // ── Mob cards — Rare / exotic ─────────────────────────────────────
        "axolotl"          to CardEntry(CardType.MOB, RARE),
        "panda"            to CardEntry(CardType.MOB, RARE),
        "polar_bear"       to CardEntry(CardType.MOB, RARE),
        "fox"              to CardEntry(CardType.MOB, RARE),
        "allay"            to CardEntry(CardType.MOB, RARE),
        "sniffer"          to CardEntry(CardType.MOB, EPIC),

        // ── Structure cards ───────────────────────────────────────────────
        "desert_temple"    to CardEntry(CardType.STRUCTURE, UNCOMMON),
        "jungle_temple"    to CardEntry(CardType.STRUCTURE, UNCOMMON),
        "stronghold"       to CardEntry(CardType.STRUCTURE, RARE),
        "end_city"         to CardEntry(CardType.STRUCTURE, EPIC),
        "ancient_city"     to CardEntry(CardType.STRUCTURE, EPIC),
        "woodland_mansion" to CardEntry(CardType.STRUCTURE, LEGENDARY, allowedBoosters = setOf(BoosterType.EPIC, BoosterType.SUPER)),

        // ── Item cards ────────────────────────────────────────────────────
        "diamond_sword"    to CardEntry(CardType.ITEM, RARE),
        "bow"              to CardEntry(CardType.ITEM, COMMON),
        "trident"          to CardEntry(CardType.ITEM, EPIC, allowedBoosters = setOf(BoosterType.EPIC, BoosterType.SUPER)),
    )
}
