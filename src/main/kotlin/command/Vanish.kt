package command

import chat.Formatting

import io.papermc.paper.command.brigadier.CommandSourceStack

import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import util.VanishHelper

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Vanish {
    @Command("vanish")
    @Permission("cloudie.command.vanish")
    fun vanish(css: CommandSourceStack) {
        val player = css.sender as? Player ?: run {
            css.sender.sendMessage(Formatting.allTags.deserialize("<red>Only players can use this command.</red>"))
            return
        }
        VanishHelper.toggleVanish(player)
    }
}