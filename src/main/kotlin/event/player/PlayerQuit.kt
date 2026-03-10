package event.player

import chat.Formatting
import command.LiveUtil

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuit: Listener {
    @EventHandler
    private fun onQuit(e: PlayerQuitEvent) {
        if(e.player.hasPermission("cloudie.silent.quit")) {
            e.quitMessage(null)
        } else {
            e.quitMessage(Formatting.allTags.deserialize("<dark_gray>[<dark_purple>-<dark_gray>] <cloudiecolor>${e.player.name}<reset> left the game."))
        }

        if(LiveUtil.isLive(e.player)) {
            LiveUtil.onPlayerQuit(e.player)
        }
    }
}