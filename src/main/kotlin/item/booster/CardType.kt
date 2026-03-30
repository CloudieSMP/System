package item.booster

/**
 * The three categories of trading cards.
 *
 * Every card type has its own block of 100 000 custom-model-data numbers so that
 * the resource pack models for mobs, structures, and items never overlap each other.
 *
 *   Mob cards       → 100 000 – 199 999
 *   Structure cards → 200 000 – 299 999
 *   Item cards      → 300 000 – 399 999
 */
enum class CardType(
    /** The first number in this type's custom-model-data block. */
    val modelDataBase: Int,
    /** A short lowercase word describing the type (used in card lore). */
    val label: String,
    /** The sub-folder name used when exporting resource-pack models, e.g. "mobs". */
    val folderName: String,
) {
    MOB(100_000, "mob", "mobs"),
    STRUCTURE(200_000, "structure", "structures"),
    ITEM(300_000, "item", "items"),
}
