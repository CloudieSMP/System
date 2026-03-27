package library

import io.ktor.http.content.CachingProperty

object HelpHelper {
    private data class CommandHelp(
        val usage: List<String>,
        val description: List<String>,
        val shortDescription: String = description.firstOrNull() ?: "No  short description available."
        )

    private val commands = mapOf(
        "help" to CommandHelp(
            listOf("/help <command>"),
            listOf("Get help for a specific command."),
            "Get help for a specific command."
        ),
        "report" to CommandHelp(
            listOf("/report <reason>"),
            listOf("Report anything to Staff like bugs, issues, feedback or players."),
            "Report anything to Staff."
        ),
        "book" to CommandHelp(
            listOf("/book author <name>", "/book title <name>"),
            listOf("Set the author of the book you're holding in your main hand.", "Set the title of the book you're holding in your main hand."),
            "Set author or title of a book."
        ),
        "crates" to CommandHelp(
            listOf("/crates"),
            listOf("View the available crates.")
        ),
        "echo" to CommandHelp(
            listOf("/echo <message>"),
            listOf("Echo a message back to you.")
        ),
        "flex" to CommandHelp(
            listOf("/flex"),
            listOf("Flex the item in your hand!")
        ),
        "hat" to CommandHelp(
            listOf("/hat"),
            listOf("Wear the item in your main hand as a hat.")
        ),
        "height" to CommandHelp(
            usage = listOf("/height <cm>", "/height reset"),
            description = listOf("Set your in-game scale based on your real-life height in cm.", "Reset your in-game height to the default."),
            "Change your in-game height."
        ),
        "home" to CommandHelp(
            listOf("/homes", "/sethome <name>", "/delhome", "/home <name>"),
            listOf("List all of your homes.", "Sets a home with the given name (if already exists, overwrites it).", "Deletes the home.", "Teleport to the home with the given name."),
            "Manage your homes."
        ),
        "mail" to CommandHelp(
            listOf("/mail"),
            listOf("test."),
            "test."
        ),
        "msg" to CommandHelp(
            listOf("/msg <player> <message>", "/reply <message>"),
            listOf("Send a private message to another player.", "Reply to the last person who"),
            "Message people privately."
        ),
        "renameitem" to CommandHelp(
            listOf("/renameitem <name>"),
            listOf("Rename the item in your main hand to the given name.")
        ),
        "spawn" to CommandHelp(
            listOf("/spawn"),
            listOf("Tp to spawn.")
        ),
        "tpa" to CommandHelp(
            listOf("/tpa <player>", "/tpahere <player>", "/tpaccept", "/tpdeny"),
            listOf("Send a TPA Request to a player.", "Send a TPA Here Request to a player.", "Accept the pending TPA Request.", "Deny the pending TPA Request."),
            "TPA to players."
        )
    )
    private val staffCommands = mapOf(
        "staffhelp" to CommandHelp(
            listOf("/staffhelp"),
            listOf("Get help for staff commands.")
        )
    )

    val featuredCommands = commands.keys.toList()
    val featuredStaffCommands = staffCommands.keys.toList()

    fun getCommandShortHelp(command: String, isStaff: Boolean): String {
        val cmd = command.lowercase()
        val help = if (isStaff) {
            staffCommands[cmd] ?: return ""
        } else {
            commands[cmd] ?: return ""
        }
        return help.shortDescription
    }

    fun getCommandHelp(command: String, isStaff: Boolean): String {
        val cmd = command.lowercase()
        val help = if (isStaff) {
            staffCommands[cmd] ?: return "<red>No help available for that staff command."
        } else {
            commands[cmd] ?: return "<red>No help available for that command."
        }
        val title = "<yellow>--------- <white>Help: /$cmd</white> ---------<reset>\n"
        val pairs = help.usage.zip(help.description).mapIndexed { i, (usage, desc) ->
            "<gold>Usage: <white>$usage\n<gold>Description: <white>$desc${if (i < help.usage.size - 1) "\n\n" else ""}"
        }.joinToString("")

        return "$title$pairs"
    }
}