package util

import chat.Formatting
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.entity.Player

fun CommandSourceStack.requirePlayer(): Player? {
    return this.sender as? Player ?: run {
        this.sender.sendMessage(Formatting.allTags.deserialize("<red>Only players can use this command.</red>"))
        null
    }
}

