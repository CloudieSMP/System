package util

import org.bukkit.Material.AIR
import org.bukkit.Material.LAPIS_LAZULI
import org.bukkit.entity.Player

/**
 * Check if the Player is holding something in their main hand. (!= AIR)
 */
fun Player.isHoldingItemInMainHand(): Boolean {
    return this.inventory.itemInMainHand.type != AIR
}

/**
 * Checks if the Player has at least 1 lapis lazuli in their inventory.
 */
fun Player.hasLapisInInventory(): Boolean {
    return this.inventory.contains(LAPIS_LAZULI, 1)
}