package command

import chat.ChatUtility
import chat.Formatting
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import org.incendo.cloud.processors.confirmation.annotation.Confirmation
import util.ResourcePacker
import java.time.Instant

@Suppress("unused", "unstableApiUsage")
@CommandContainer
@Permission("cloudie.cmd.pack")
class Pack {

    @Command("pack status")
    fun packStatus(css: CommandSourceStack) {
        val status = ResourcePacker.cacheStatus()
        css.sender.sendMessage(Formatting.allTags.deserialize("<notifcolor><b>Resource Packs</b></notifcolor> <gray>Status</gray>"))
        css.sender.sendMessage(
            Formatting.allTags.deserialize(
                "<gray>Configured:</gray> <white>${status.configuredCount}</white> <dark_gray>|</dark_gray> <gray>Cached:</gray> <white>${status.cachedCount}</white>"
            )
        )

        val lastRefresh = status.lastRefreshAtMillis?.let { Instant.ofEpochMilli(it).toString() } ?: "never"
        css.sender.sendMessage(Formatting.allTags.deserialize("<gray>Last refresh:</gray> <white>$lastRefresh</white>"))

        status.lastError?.let { error ->
            css.sender.sendMessage(Formatting.allTags.deserialize("<red>Last error:</red> <white>$error</white>"))
        }

        if (status.cached.isEmpty()) {
            css.sender.sendMessage(Formatting.allTags.deserialize("<yellow>No cached packs available.</yellow>"))
            return
        }

        status.cached.forEachIndexed { index, pack ->
            val shortHash = if (pack.hash.length > 12) "${pack.hash.take(12)}..." else pack.hash
            css.sender.sendMessage(
                Formatting.allTags.deserialize(
                    "<aqua>#${index + 1}</aqua> <gray>prio</gray>=<white>${pack.priority}</white> <gray>source</gray>=<white>${pack.hashSource}</white> <gray>hash</gray>=<white>$shortHash</white>"
                )
            )
            css.sender.sendMessage(Formatting.allTags.deserialize("<dark_gray>   ${pack.uri}</dark_gray>"))
        }
    }

    @Command("pack refresh")
    @Confirmation
    fun packRefresh(css: CommandSourceStack) {
        if (!ResourcePacker.refreshFromUrl()) {
            css.sender.sendMessage(Component.text("Failed to refresh resource pack from URL. Check console logs.", NamedTextColor.RED))
            return
        }

        ChatUtility.broadcastDev("<yellow>${css.sender.name} <green>refreshed<reset> the <notifcolor>resource packs<reset> for all online users.", false)
        val online = Bukkit.getOnlinePlayers()
        online.forEach { onlinePlayer -> ResourcePacker.removePackPlayer(onlinePlayer) }
        online.forEach { onlinePlayer -> ResourcePacker.applyPackPlayer(onlinePlayer) }
    }

    @Command("pack refresh <player>")
    @Confirmation
    fun packRefresh(css: CommandSourceStack, player: Player) {
        if (!ResourcePacker.refreshFromUrl()) {
            css.sender.sendMessage(Component.text("Failed to refresh resource pack from URL. Check console logs.", NamedTextColor.RED))
            return
        }

        ChatUtility.broadcastDev("<yellow>${css.sender.name} <green>refreshed<reset> the <notifcolor>resource packs<reset> for ${player.name}.", false)
        ResourcePacker.removePackPlayer(player)
        ResourcePacker.applyPackPlayer(player)
    }

    @Command("pack push")
    @Confirmation
    fun packPush(css: CommandSourceStack) {
        ChatUtility.broadcastDev("<yellow>${css.sender.name} <green>pushed<reset> the <notifcolor>resource packs<reset> to all online users.", false)
        Bukkit.getOnlinePlayers().forEach { online -> ResourcePacker.applyPackPlayer(online) }
    }

    @Command("pack push <player>")
    @Confirmation
    fun packPush(css: CommandSourceStack, player: Player) {
        ChatUtility.broadcastDev("<yellow>${css.sender.name} <green>pushed<reset> the <notifcolor>resource packs<reset> to ${player.name}.", false)
        ResourcePacker.applyPackPlayer(player)
    }

    @Command("pack pop")
    @Confirmation
    fun packPop(css: CommandSourceStack) {
        ChatUtility.broadcastDev("<yellow>${css.sender.name} <red>popped<reset> the <notifcolor>resource packs<reset> from all online users.", false)
        Bukkit.getOnlinePlayers().forEach { online -> ResourcePacker.removePackPlayer(online) }
    }

    @Command("pack pop <player>")
    @Confirmation
    fun packPop(css: CommandSourceStack, player: Player) {
        ChatUtility.broadcastDev("<yellow>${css.sender.name} <green>popped<reset> the <notifcolor>resource packs<reset> from ${player.name}.", false)
        ResourcePacker.removePackPlayer(player)
    }
}