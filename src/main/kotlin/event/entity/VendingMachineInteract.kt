package event.entity

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
        if (!player.areYouHoldingThisRemove(Material.DIAMOND)) return player.sendMessage("You need to be holding a diamond to interact with the vending machine!")
        val location = vendingMachine.location.clone().add(vendingMachine.location.direction.setY(0).multiply(1)).add(0.0, 1.25, 0.0)

        player.playSound(vendingMachine.location, "minecraft:block.note_block.chime", 1f, 1f)
        player.world.spawn(location, Item::class.java).apply {
            itemStack = Crate.create(CrateType.PLUSHIE)
            velocity = location.direction.multiply(0.1)
            pickupDelay = 2.secondsToTicks()
            isInvulnerable = true
            setWillAge(false)
            isGlowing = true
        }
    }
}