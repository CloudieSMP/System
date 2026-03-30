package item

import logger
import kotlin.random.Random

enum class SubRarity(val weight: Double, val subRarityGlyph: String, val modelDataOffset: Int) {
    NONE      (99.95,  "",        0), // plain card — uses the card's base model data
    SHINY     (0.025, "\uE151",  1), // base + 1
    SHADOW    (0.015, "\uE152",  2), // base + 2
    OBFUSCATED(0.01,  "\uE153",  3); // base + 3

    companion object {
        fun getRandomSubRarity(): SubRarity {
            val totalWeight = SubRarity.entries.sumOf { it.weight }
            val randomValue = Random.nextDouble(totalWeight)

            var cumulativeWeight = 0.0
            for (rarity in SubRarity.entries) {
                cumulativeWeight += rarity.weight
                if (randomValue < cumulativeWeight) {
                    return rarity
                }
            }

            logger.warning("Unreachable code hit! No sub rarity selected")
            return NONE // Should be unreachable but default to null in case of issue
        }
    }
}