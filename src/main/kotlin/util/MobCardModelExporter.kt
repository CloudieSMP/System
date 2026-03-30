package util

import item.SubRarity
import item.booster.CardCatalog
import java.io.File
import java.util.Base64

object MobCardModelExporter {
    data class ExportResult(
        val itemDefinitionFile: File,
        val stubsRootDir: File,
        val texturesRootDir: File,
        val dispatchEntries: Int,
        val generatedStubs: Int,
        val generatedTexturePlaceholders: Int,
    )

    fun exportPaperItemDefinition(baseDir: File, generateTexturePlaceholders: Boolean = true): ExportResult {
        val itemDefinitionFile = File(baseDir, "assets/minecraft/items/paper.json")
        itemDefinitionFile.parentFile.mkdirs()

        val stubsRootDir = File(baseDir, "assets/cloudie/models/item").also { it.mkdirs() }
        val texturesRootDir = File(baseDir, "assets/cloudie/textures/item").also { it.mkdirs() }

        // Build a flat list of every dispatch entry: one per card × per sub-rarity variant.
        // Sorted by model data number so the JSON range_dispatch entries are in ascending order.
        data class DispatchEntry(val modelData: Int, val modelPath: String)

        val allEntries = CardCatalog.cards
            .flatMap { card ->
                // Only include SHINY / SHADOW / OBFUSCATED variants for cards that can actually roll them.
                // Cards without sub-rarities get a single NONE entry.
                val variants = if (card.canHaveSubRarity) SubRarity.entries else listOf(SubRarity.NONE)
                variants.map { subRarity ->
                    DispatchEntry(card.modelDataFor(subRarity), card.modelPathFor(subRarity))
                }
            }
            .sortedBy { it.modelData }

        // ── paper.json (range_dispatch) ───────────────────────────────────
        val json = buildString {
            appendLine("{")
            appendLine("  \"model\": {")
            appendLine("    \"type\": \"minecraft:range_dispatch\",")
            appendLine("    \"property\": \"minecraft:custom_model_data\",")
            appendLine("    \"entries\": [")
            allEntries.forEachIndexed { index, entry ->
                append("      { \"threshold\": ${entry.modelData}, \"model\": { \"type\": \"minecraft:model\", \"model\": \"cloudie:item/${entry.modelPath}\" } }")
                if (index < allEntries.lastIndex) append(',')
                appendLine()
            }
            appendLine("    ],")
            appendLine("    \"fallback\": { \"type\": \"minecraft:model\", \"model\": \"minecraft:item/paper\" }")
            appendLine("  }")
            appendLine("}")
        }
        itemDefinitionFile.writeText(json)

        // ── Model stubs + texture placeholders ────────────────────────────
        var generatedStubs = 0
        var generatedTexturePlaceholders = 0

        allEntries.distinctBy { it.modelPath }.forEach { entry ->
            // Model stub JSON
            val stubFile = File(stubsRootDir, "${entry.modelPath}.json")
            stubFile.parentFile.mkdirs()
            stubFile.writeText(buildString {
                appendLine("{")
                appendLine("  \"parent\": \"minecraft:item/generated\",")
                appendLine("  \"textures\": {")
                appendLine("    \"layer0\": \"cloudie:item/${entry.modelPath}\"")
                appendLine("  }")
                appendLine("}")
            })
            generatedStubs++

            // Placeholder texture (only created if it doesn't exist yet)
            if (generateTexturePlaceholders) {
                val textureFile = File(texturesRootDir, "${entry.modelPath}.png")
                if (!textureFile.exists()) {
                    textureFile.parentFile.mkdirs()
                    textureFile.writeBytes(PLACEHOLDER_TEXTURE_PNG)
                    generatedTexturePlaceholders++
                }
            }
        }

        return ExportResult(
            itemDefinitionFile = itemDefinitionFile,
            stubsRootDir = stubsRootDir,
            texturesRootDir = texturesRootDir,
            dispatchEntries = allEntries.size,
            generatedStubs = generatedStubs,
            generatedTexturePlaceholders = generatedTexturePlaceholders,
        )
    }

    // 1×1 transparent PNG used as a safe placeholder so the resource pack doesn't crash on missing textures.
    private val PLACEHOLDER_TEXTURE_PNG = Base64.getDecoder().decode(
        "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAusB9oN6A+gAAAAASUVORK5CYII="
    )
}
