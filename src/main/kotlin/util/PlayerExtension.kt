package util

import org.bukkit.Material
import org.bukkit.Material.AIR
import org.bukkit.Material.LAPIS_LAZULI
import org.bukkit.entity.Player

/**
 * Check if the Player is holding something in their main hand. (!= AIR)
 */
fun Player.areYouHoldingSomething(): Boolean {
    return this.inventory.itemInMainHand.type != AIR
}

/**
 * Get the Material that the Player is holding in their main hand.
 */
fun Player.whatAreYouHoldingInMainHand(): Material {
    return this.inventory.itemInMainHand.type
}
/**
 * Check if the Player is holding a specific Material in their main hand.
 */
fun Player.areYouHoldingThis(holding: Material): Boolean {
    return this.inventory.itemInMainHand.type == holding
}

/**
 * Check if the Player is holding a specific Material in their main hand.
 */
fun Player.areYouHoldingThisRemove(holding: Material): Boolean {
    if (this.inventory.itemInMainHand.type != holding) return false
    this.inventory.itemInMainHand.amount --
    return true
}

/**
 * Checks if the Player has at least 1 lapis lazuli in their inventory.
 */
fun Player.hasLapisInInventory(): Boolean {
    return this.inventory.contains(LAPIS_LAZULI, 1)
}