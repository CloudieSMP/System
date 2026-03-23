package command

import chat.Formatting
import library.HomeStorage
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Flag
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.context.CommandContext
import util.requirePlayer

@Suppress("unused")
@CommandContainer
class Home {
    private val maxHomes = 5

    @Command("homes")
    @CommandDescription("List your saved homes.")
    @Permission("cloudie.command.home")
    fun homes(css: CommandSourceStack) {
        val player = css.requirePlayer() ?: return
        val homes = HomeStorage.listHomeNames(player.uniqueId)

        if (homes.isEmpty()) {
            player.sendMessage(Formatting.allTags.deserialize("<gray>You have no homes yet. Use <white>/sethome <name></white> to create one."))
            return
        }

        val header = Component.text("Your homes ")
            .color(NamedTextColor.WHITE)
            .append(Component.text("(${homes.size}/$maxHomes)", NamedTextColor.DARK_GRAY))
            .append(Component.text(": ", NamedTextColor.WHITE))

        val homeComponents = homes.mapIndexed { index, homeName ->
            val component = Component.text(homeName, NamedTextColor.AQUA)
                .clickEvent(ClickEvent.runCommand("/home $homeName"))
                .hoverEvent(HoverEvent.showText(
                    Component.text("Click to teleport", NamedTextColor.GREEN)
                ))

            if (index < homes.size - 1) {
                component.append(Component.text(", ", NamedTextColor.DARK_GRAY))
            } else {
                component
            }
        }

        val message = homeComponents.fold(header) { acc, component -> acc.append(component) }
        player.sendMessage(message)
    }

    @Command("sethome <name>")
    @CommandDescription("Set a home at your current position.")
    @Permission("cloudie.command.home")
    fun sethome(
            css: CommandSourceStack,
            @Argument("name") name: String,
            @Flag("force", aliases = ["f"]) forced: Boolean = false) {
        val player = css.requirePlayer() ?: return
        val sanitizedName = sanitizeName(name) ?: run {
            player.sendMessage(Formatting.allTags.deserialize("<red>Home names must be 1-16 chars and use only letters, numbers, _ or -.</red>"))
            return
        }

        val homeCount = HomeStorage.listHomeNames(player.uniqueId).size
        val existingHome = HomeStorage.loadHome(player.uniqueId, sanitizedName)
        if (existingHome == null && homeCount >= maxHomes) {
            player.sendMessage(Formatting.allTags.deserialize("<red>You reached the home limit <dark_gray>($maxHomes)</dark_gray>. Delete one first with <white>/delhome <name></white>."))
            return
        }
        if (existingHome != null && !forced) {
            player.sendMessage(Formatting.allTags.deserialize("<yellow>Home <aqua>$sanitizedName</aqua> already exists. Use <white>/sethome $sanitizedName --force</white> to overwrite it.</yellow>"))
            return
        }

        val outcome = HomeStorage.saveHome(player.uniqueId, sanitizedName, player.location)
        when (outcome) {
            HomeStorage.SaveOutcome.CREATED -> player.sendMessage(Formatting.allTags.deserialize("<green>Home <aqua>$sanitizedName</aqua> created."))
            HomeStorage.SaveOutcome.UPDATED -> player.sendMessage(Formatting.allTags.deserialize("<yellow>Home <aqua>$sanitizedName</aqua> updated."))
            HomeStorage.SaveOutcome.FAILED -> player.sendMessage(Formatting.allTags.deserialize("<red>Could not save home right now. Please try again.</red>"))
        }
    }

    @Command("home <name>")
    @CommandDescription("Teleport to one of your homes.")
    @Permission("cloudie.command.home")
    fun homesTeleport(css: CommandSourceStack, @Argument(value = "name", suggestions = "player-homes") name: String) {
        val player = css.requirePlayer() ?: return
        val sanitizedName = sanitizeName(name) ?: run {
            player.sendMessage(Formatting.allTags.deserialize("<red>Invalid home name.</red>"))
            return
        }

        val home = HomeStorage.loadHome(player.uniqueId, sanitizedName) ?: run {
            player.sendMessage(Formatting.allTags.deserialize("<red>Home <aqua>$sanitizedName</aqua> does not exist.</red>"))
            return
        }

        val success = player.teleport(home)
        if (success) {
            player.sendMessage(Formatting.allTags.deserialize("<green>Teleported to <aqua>$sanitizedName</aqua>."))
        } else {
            player.sendMessage(Formatting.allTags.deserialize("<red>Teleport failed. The location may be invalid.</red>"))
        }
    }

    @Command("delhome <name>")
    @CommandDescription("Delete one of your homes.")
    @Permission("cloudie.command.home")
    fun delhome(
            css: CommandSourceStack,
            @Argument(value = "name", suggestions = "player-homes") name: String,
            @Flag("force", aliases = ["f"]) forced: Boolean = false) {
        val player = css.requirePlayer() ?: return
        val sanitizedName = sanitizeName(name) ?: run {
            player.sendMessage(Formatting.allTags.deserialize("<red>Invalid home name.</red>"))
            return
        }
        if (!forced) {
            player.sendMessage(Formatting.allTags.deserialize("<yellow>Are you sure you want to delete <aqua>$sanitizedName</aqua>? Use <white>/delhome $sanitizedName --force</white> to confirm.</yellow>"))
            return
        }

        val deleted = HomeStorage.deleteHome(player.uniqueId, sanitizedName)
        if (deleted) {
            player.sendMessage(Formatting.allTags.deserialize("<green>Deleted home <aqua>$sanitizedName</aqua>."))
        } else {
            player.sendMessage(Formatting.allTags.deserialize("<red>Home <aqua>$sanitizedName</aqua> does not exist.</red>"))
        }
    }


    private fun sanitizeName(input: String): String? {
        val trimmed = input.trim().lowercase()
        if (trimmed.length !in 1..16) return null
        if (!trimmed.matches(Regex("^[a-z0-9_-]+$"))) return null
        return trimmed
    }

    @Suggestions("player-homes")
    fun homeNameSuggestions(context: CommandContext<CommandSourceStack>, input: String): List<String> {
        val player = context.sender().sender as? Player ?: return emptyList()
        return HomeStorage.listHomeNames(player.uniqueId)
            .filter { it.startsWith(input, ignoreCase = true) }
    }
}