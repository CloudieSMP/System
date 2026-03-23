package command

import io.papermc.paper.command.brigadier.CommandSourceStack
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import util.requirePlayer
import util.ui.CrateBrowserWindow

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Crates {
    @Command("crates")
    @CommandDescription("Lists all the crates.")
    @Permission("cloudie.command.crates")
    fun crates(css: CommandSourceStack) {
        val player = css.requirePlayer() ?: return

        CrateBrowserWindow.openSelector(player)
    }
}