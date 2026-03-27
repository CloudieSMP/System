package event

import chat.ChatUtility
import com.destroystokyo.paper.event.server.PaperServerListPingEvent
import library.Translation

import org.bukkit.Bukkit

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ServerListEvent: Listener {
    @EventHandler
    private fun onServerPing(e: PaperServerListPingEvent) {
        e.version = "Cloudie v${Bukkit.getMinecraftVersion()}"
        // e.motd(ChatUtility.formatMessage("${Translation.TabList.SERVER_LIST_TITLE}<newline>${Translation.TabList.SERVER_LIST_VERSION}", false))
    }
}