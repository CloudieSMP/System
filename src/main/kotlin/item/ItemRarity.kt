package item

import org.bukkit.Color

enum class ItemRarity(val rarityName: String, val rarityGlyph: String, val color: Color, val colorHex: String) {
    SPECIAL("Special", "\uE100", Color.fromRGB(236, 28, 36), "#ec1c24"),
    COMMON("Common", "\uE101", Color.fromRGB(255, 255, 255), "#ffffff"),
    UNCOMMON("Uncommon", "\uE102", Color.fromRGB(14, 209, 69), "#0ed145"),
    RARE("Rare", "\uE103", Color.fromRGB(0, 168, 243), "#00a8f3"),
    EPIC("Epic", "\uE104", Color.fromRGB(184, 61, 186), "#b83dba"),
    LEGENDARY("Legendary", "\uE105", Color.fromRGB(255, 127, 39), "#ff7f27"),
    MYTHIC("Mythic", "\uE106", Color.fromRGB(255, 51, 116), "#ff3374"),
    UNREAL("Unreal", "\uE107", Color.fromRGB(134, 102, 230), "#8666e6"),
}