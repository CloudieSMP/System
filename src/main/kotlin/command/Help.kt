package command

import chat.Formatting

import io.papermc.paper.command.brigadier.CommandSourceStack

import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Help {
    @Command("help")
    @Permission("cloudie.command.help")
    fun help(css: CommandSourceStack) {
        css.sender.sendMessage(Formatting.allTags.deserialize("<yellow>This is a fucking placeholder"))
    }
}