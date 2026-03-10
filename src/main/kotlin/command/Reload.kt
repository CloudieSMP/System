package command

import System
import chat.Formatting
import io.papermc.paper.command.brigadier.CommandSourceStack
import logger
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import plugin

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Reload {
    @Command("cloudie reload")
    @Permission("cloudie.command.reload")
    fun reloadConfig(css: CommandSourceStack) {
        try {
            (plugin as System).reloadConfig()
            css.sender.sendMessage(Formatting.allTags.deserialize("<green>Configuration reloaded successfully!"))
        } catch (e: Exception) {
            css.sender.sendMessage(Formatting.allTags.deserialize("<red>Failed to reload configuration: ${e.message}"))
            logger.severe("Error reloading configuration: ${e.message}")
            e.printStackTrace()
        }
    }
}