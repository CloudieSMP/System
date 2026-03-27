package command

import chat.Formatting.allTags
import io.papermc.paper.command.brigadier.CommandSourceStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import plugin
import util.DiscordWebhook

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Report {
    private val scope = CoroutineScope(Dispatchers.IO)

    @Command("report <reason>")
    @CommandDescription("Report a player or issue to the staff team.")
    @Permission("cloudie.cmd.report")
    fun report(css: CommandSourceStack, @Argument("reason") reason: Array<String>) {
        val sender = css.sender
        sender.sendMessage(allTags.deserialize("<yellow>Your report has been sent to the staff team. They will review it as soon as possible."))
        val fullReason = reason.joinToString(" ")

        scope.launch {
            DiscordWebhook.sendReport(
                webhookUrl = plugin.config.discord.reportWebhookUrl,
                playerName = sender.name,
                reason = fullReason
            )
        }
    }
}

