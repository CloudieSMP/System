package library

import org.bukkit.Bukkit

object Translation {
    object TabList {
        const val SERVER_LIST_TITLE = "<gradient:#DF6F69:#C45889:#823BC6><bold>Cloudie S10<reset>"
        val SERVER_LIST_VERSION = "<dark_gray>V${Bukkit.getMinecraftVersion()}<reset>"
    }

    object PlayerMessages {
        const val JOIN = "<dark_gray>[<light_purple>+<dark_gray>] <cloudiecolor>%player%<reset> joined the game."
        const val QUIT = "<dark_gray>[<dark_purple>-<dark_gray>] <cloudiecolor>%player%<reset> left the game."
    }
}