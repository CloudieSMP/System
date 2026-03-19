package item.crate

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import plugin

object CrateRecipes {
    fun registerAll() {
        CrateType.entries.forEach(::registerCrateRecipe)
    }

    private fun registerCrateRecipe(crateType: CrateType) {
        val crate = Crate.create(crateType)
        val recipe = ShapedRecipe(NamespacedKey(plugin, crateType.recipeKey), crate).apply {
            shape(
                "DDD",
                "DED",
                "DDD"
            )
            setIngredient('D', Material.OAK_WOOD)
            setIngredient('E', crateType.recipeAccentMaterial)
        }
        Bukkit.addRecipe(recipe)
    }
}

