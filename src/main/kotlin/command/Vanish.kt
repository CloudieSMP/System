package command

import io.papermc.paper.command.brigadier.CommandSourceStack
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import library.VanishHelper
import util.requirePlayer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Vanish {
    @Command("vanish")
    @Permission("cloudie.command.vanish")
    fun vanish(css: CommandSourceStack) {
        val player = css.requirePlayer() ?: return
        VanishHelper.toggleVanish(player)
    }
}