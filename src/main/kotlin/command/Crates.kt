package command

import chat.Formatting
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import util.ui.CrateBrowserWindow

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Crates {
    @Command("crates")
    @CommandDescription("Lists all the crates.")
    @Permission("cloudie.command.crates")
    fun crates(css: CommandSourceStack) {
        val player = css.sender as? Player ?: run {
            css.sender.sendMessage(Formatting.allTags.deserialize("<red>Only players can use this command.</red>"))
            return
        }

        CrateBrowserWindow.openSelector(player)
    }
}