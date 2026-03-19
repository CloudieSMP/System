package item.crate

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import plugin

object CrateRecipes {
    fun registerAll() {
        registerPlushieCrate()
        registerWearablesCrate()
        registerUtensilsCrate()
        registerPlayerCrate()
    }

    private fun registerPlushieCrate() {
        val crate = Crate.create(CrateType.PLUSHIE)
        val recipe = ShapedRecipe(NamespacedKey(plugin, "plushie_crate"), crate).apply {
            shape(
                "DDD",
                "DED",
                "DDD"
            )
            setIngredient('D', Material.OAK_WOOD)
            setIngredient('E', Material.PINK_WOOL)
        }
        Bukkit.addRecipe(recipe)
    }

    private fun registerWearablesCrate() {
        val crate = Crate.create(CrateType.WEARABLES)
        val recipe = ShapedRecipe(NamespacedKey(plugin, "wearables_crate"), crate).apply {
            shape(
                "DDD",
                "DED",
                "DDD"
            )
            setIngredient('D', Material.OAK_WOOD)
            setIngredient('E', Material.LIGHT_BLUE_WOOL)
        }
        Bukkit.addRecipe(recipe)
    }

    private fun registerUtensilsCrate() {
        val crate = Crate.create(CrateType.UTENSILS)
        val recipe = ShapedRecipe(NamespacedKey(plugin, "utensils_crate"), crate).apply {
            shape(
                "DDD",
                "DED",
                "DDD"
            )
            setIngredient('D', Material.OAK_WOOD)
            setIngredient('E', Material.ORANGE_WOOL)
        }
        Bukkit.addRecipe(recipe)
    }

    private fun registerPlayerCrate() {
        val crate = Crate.create(CrateType.PLAYER)
        val recipe = ShapedRecipe(NamespacedKey(plugin, "player_crate"), crate).apply {
            shape(
                "DDD",
                "DED",
                "DDD"
            )
            setIngredient('D', Material.OAK_WOOD)
            setIngredient('E', Material.PURPLE_WOOL)
        }
        Bukkit.addRecipe(recipe)
    }
}

