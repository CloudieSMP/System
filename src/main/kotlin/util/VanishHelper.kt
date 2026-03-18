package util

import chat.ChatUtility
import chat.Formatting
import library.Translation
import net.kyori.adventure.audience.Audience
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

import plugin

object VanishHelper {
    private val vanishPlayers = mutableSetOf<Player>()
    fun toggleVanish(player: Player) {
        if(player in vanishPlayers) {
            vanishPlayers.remove(player)
            player.sendMessage(Formatting.allTags.deserialize("<dark_gray><i>You are now unvanished"))
            ChatUtility.broadcastAll( Translation.PlayerMessages.JOIN.replace("%player%", player.name))
        } else {
            vanishPlayers.add(player)
            vanishTask(player)
            player.sendMessage(Formatting.allTags.deserialize("<dark_gray><i>You are now vanished"))
            ChatUtility.broadcastAll( Translation.PlayerMessages.QUIT.replace("%player%", player.name))
        }
    }

    private fun vanishTask(player: Player) {
        object : BukkitRunnable() {
            override fun run() {
                if (!player.isOnline) {
                    cancel()
                    return
                }
                if (vanishPlayers.contains(player)) {
                    for (viewer in Bukkit.getOnlinePlayers()) {
                        if (viewer == player) continue
                        viewer.hidePlayer(plugin, player)
                    }
                } else {
                    for (viewer in Bukkit.getOnlinePlayers()) {
                        if (viewer == player) continue
                        viewer.showPlayer(plugin, player)
                    }
                    cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }
}