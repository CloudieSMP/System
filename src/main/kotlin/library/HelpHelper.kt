package library

object HelpHelper {
    private data class CommandHelp(
        val usage: List<String>,
        val description: List<String>,
    )

    private val commands = mapOf(
        "help" to CommandHelp(
            usage = listOf("/help <command>"),
            description = listOf("Get help for a specific command.")
        ),
        "book" to CommandHelp(
            usage = listOf("/book author <name>", "/book title <name>"),
            description = listOf("Set the author of the book you're holding in your main hand.", "Set the title of the book you're holding in your main hand.")
        ),
        "height" to CommandHelp(
            usage = listOf("/height <cm>"),
            description = listOf("Set your in-game scale based on your real-life height in cm.")
        ),
        "test" to CommandHelp(
            usage = listOf("/test", "/test <arg1>", "/test <arg1> <arg2>", "/test <arg1> <arg2> <arg3>"),
            description = listOf("A test command for demonstration purposes.", "A test command for demonstration purposes.", "A test command for demonstration purposes.", "A test command for demonstration purposes.")
        )
    )
    private val staffCommands = mapOf(
        "staffhelp" to CommandHelp(
            usage = listOf("/staffhelp"),
            description = listOf("Get help for staff commands.")
        )
    )

    val featuredCommands = commands.keys.toList()
    val featuredStaffCommands = staffCommands.keys.toList()

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