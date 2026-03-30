package event.entity

import chat.Formatting.allTags
import item.booster.BoosterPack
import item.booster.BoosterType
import item.crate.Crate
import item.crate.CrateType
import org.bukkit.Material
import org.bukkit.entity.Interaction
import org.bukkit.entity.Item
import org.bukkit.event.player.PlayerInteractEntityEvent
import util.areYouHoldingThisRemove
import util.secondsToTicks

object VendingMachineInteract {
    fun vendingMachineInteractEvent(event: PlayerInteractEntityEvent) {
        val vendingMachine = event.rightClicked as? Interaction ?: return
        if (!vendingMachine.scoreboardTags.contains("vending_machine")) return
        val player = event.player
        val location = vendingMachine.location.clone().add(vendingMachine.location.direction.setY(0).multiply(1)).add(0.0, 1.25, 0.0)

        if (player.areYouHoldingThisRemove(Material.COAL)) {
            player.playSound(vendingMachine.location, "minecraft:block.note_block.chime", 1f, 1f)
            player.world.spawn(location, Item::class.java).apply {
                itemStack = BoosterPack.create(BoosterType.STANDARD)
                velocity = location.direction.multiply(0.1)
                pickupDelay = 2.secondsToTicks()
                isInvulnerable = true
                setWillAge(false)
                isGlowing = true
            }
        } else if (player.areYouHoldingThisRemove(Material.IRON_INGOT)) {
            player.playSound(vendingMachine.location, "minecraft:block.note_block.chime", 1f, 1f)
            player.world.spawn(location, Item::class.java).apply {
                itemStack = BoosterPack.create(BoosterType.EPIC)
                velocity = location.direction.multiply(0.1)
                pickupDelay = 2.secondsToTicks()
                isInvulnerable = true
                setWillAge(false)
                isGlowing = true
            }
        } else if (player.areYouHoldingThisRemove(Material.DIAMOND)) {
            player.playSound(vendingMachine.location, "minecraft:block.note_block.chime", 1f, 1f)
            player.world.spawn(location, Item::class.java).apply {
                itemStack = BoosterPack.create(BoosterType.SUPER)
                velocity = location.direction.multiply(0.1)
                pickupDelay = 2.secondsToTicks()
                isInvulnerable = true
                setWillAge(false)
                isGlowing = true
            }
        } else {
            player.sendMessage(allTags.deserialize("<red>You need to be holding a valid currency item to use the vending machine!"))
        }
    }
}