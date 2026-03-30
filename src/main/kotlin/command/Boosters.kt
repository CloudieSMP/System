package command

import io.papermc.paper.command.brigadier.CommandSourceStack
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import util.requirePlayer
import util.ui.BoosterPackBrowserWindow

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Boosters {
    @Command("boosters")
    @CommandDescription("Lists all the booster packs.")
    @Permission("cloudie.cmd.boosters")
    fun boosters(css: CommandSourceStack) {
        val player = css.requirePlayer() ?: return
        BoosterPackBrowserWindow.openSelector(player)
    }
}

