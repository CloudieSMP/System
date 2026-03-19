package command

import chat.Formatting
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import item.crate.Crate
import item.crate.CrateItem
import item.crate.CrateType
import org.incendo.cloud.annotations.Argument

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Debug {
    @Command("debug crate <type>")
    @Permission("cloudie.command.debug")
    fun debugCrate(css: CommandSourceStack, @Argument("type") type: CrateType) {
        val player = css.sender as? Player ?: run {
            css.sender.sendMessage(Formatting.allTags.deserialize("<red>Only players can use this command.</red>"))
            return
        }

        player.inventory.addItem(Crate.create(type))
        player.sendMessage(Formatting.allTags.deserialize("<cloudiecolor>Given a crate!"))
    }
    @Command("debug crate item <type>")
    @Permission("cloudie.command.debug")
    fun debugCrateItem(css: CommandSourceStack, @Argument("type") item: CrateItem) {
        val player = css.sender as? Player ?: run {
            css.sender.sendMessage(Formatting.allTags.deserialize("<red>Only players can use this command.</red>"))
            return
        }

        player.inventory.addItem(item.createItemStack())
        player.sendMessage(Formatting.allTags.deserialize("<cloudiecolor>Given a crate item!"))
    }
}