package command

import chat.Formatting
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import item.crate.Crate
import item.crate.CrateItem
import item.crate.CrateType
import org.incendo.cloud.annotations.Argument
import util.requirePlayer

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Debug {
    @Command("debug crate <type>")
    @Permission("cloudie.cmd.debug")
    fun debugCrate(css: CommandSourceStack, @Argument("type") type: CrateType) {
        val player = css.requirePlayer() ?: return

        player.inventory.addItem(Crate.create(type))
        player.sendMessage(Formatting.allTags.deserialize("<cloudiecolor>Given a crate!"))
    }
    @Command("debug crate item <type>")
    @Permission("cloudie.cmd.debug")
    fun debugCrateItem(css: CommandSourceStack, @Argument("type") item: CrateItem) {
        val player = css.requirePlayer() ?: return

        player.inventory.addItem(item.createItemStack())
        player.sendMessage(Formatting.allTags.deserialize("<cloudiecolor>Given a crate item!"))
    }
}