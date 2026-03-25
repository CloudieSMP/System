package command

import chat.Formatting.allTags
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.Bukkit
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import library.MailStorage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import util.requirePlayer
import java.time.Duration
import java.time.Instant
import java.util.UUID
import kotlin.math.ceil

@Suppress("unused", "unstableApiUsage")
@CommandContainer
class Mail {
    private val pageSize = 10

    @Command("mail send <player> <text>")
    @CommandDescription("Send somebody a mail message that they can read later.")
    @Permission("cloudie.cmd.mail")
    fun sendMail(css: CommandSourceStack, @Argument("player") recipientName: String, @Argument("text") text: Array<String>) {
        val sender = css.requirePlayer() ?: return
        val message = text.joinToString(" ").trim()
        if (message.isEmpty()) {
            sender.sendMessage(allTags.deserialize("<red>Mail message cannot be empty.</red>"))
            return
        }

        val recipient = resolveRecipient(recipientName) ?: run {
            sender.sendMessage(allTags.deserialize("<red>Could not find a known player named <white>$recipientName</white>.</red>"))
            return
        }

        if (sender.uniqueId == recipient.first) {
            sender.sendMessage(allTags.deserialize("<yellow>You cannot send mail to yourself.</yellow>"))
            return
        }

        MailStorage.addMailAsync(recipient.first, sender.uniqueId, message) { mail ->
            val onlineSender = Bukkit.getPlayer(sender.uniqueId) ?: return@addMailAsync
            onlineSender.sendMessage(allTags.deserialize("<green>Mail sent to <white>${recipient.second}</white> <dark_gray>(#${mail.id})</dark_gray>."))

            val onlineRecipient = Bukkit.getPlayer(recipient.first)
            onlineRecipient?.sendMessage(
                allTags.deserialize(
                    "<notifcolor>You received a new mail from <white>${onlineSender.name}</white><notifcolor>. Use <white>/mail inbox</white><notifcolor>."
                )
            )
        }
    }

    @Command("mail inbox")
    @CommandDescription("Open your mailbox overview.")
    @Permission("cloudie.cmd.mail")
    fun inbox(css: CommandSourceStack) {
        renderInbox(css, 1)
    }

    @Command("mail inbox <page>")
    @CommandDescription("Open a specific mailbox page.")
    @Permission("cloudie.cmd.mail")
    fun inbox(css: CommandSourceStack, @Argument("page") page: Int) {
        renderInbox(css, page)
    }

    @Command("mail read <id>")
    @CommandDescription("Read a specific mail message.")
    @Permission("cloudie.cmd.mail")
    fun read(css: CommandSourceStack, @Argument("id") id: Long) {
        val player = css.requirePlayer() ?: return
        val playerId = player.uniqueId

        MailStorage.markReadAsync(playerId, id) { mail ->
            val onlinePlayer = Bukkit.getPlayer(playerId) ?: return@markReadAsync
            if (mail == null) {
                onlinePlayer.sendMessage(allTags.deserialize("<red>No mail found with id <white>$id</white>.</red>"))
                return@markReadAsync
            }

            val senderName = resolveSenderName(mail.senderId)
            val age = formatRelative(mail.sentAtMillis)
            onlinePlayer.sendMessage(allTags.deserialize("<notifcolor><b>Mail #${mail.id}</b></notifcolor> <gray>from</gray> <white>$senderName</white> <dark_gray>($age ago)</dark_gray>"))
            onlinePlayer.sendMessage(allTags.deserialize("<white>${mail.body}</white>"))
        }
    }

    @Command("mail delete <id>")
    @CommandDescription("Delete one message from your mailbox.")
    @Permission("cloudie.cmd.mail")
    fun delete(css: CommandSourceStack, @Argument("id") id: Long) {
        val player = css.requirePlayer() ?: return
        val playerId = player.uniqueId

        MailStorage.deleteMailAsync(playerId, id) { deleted ->
            val onlinePlayer = Bukkit.getPlayer(playerId) ?: return@deleteMailAsync
            if (deleted) {
                onlinePlayer.sendMessage(allTags.deserialize("<green>Deleted mail <white>#$id</white>.</green>"))
            } else {
                onlinePlayer.sendMessage(allTags.deserialize("<red>No mail found with id <white>$id</white>.</red>"))
            }
        }
    }

    @Command("mail clearread")
    @CommandDescription("Delete all read messages from your mailbox.")
    @Permission("cloudie.cmd.mail")
    fun clearRead(css: CommandSourceStack) {
        val player = css.requirePlayer() ?: return
        val playerId = player.uniqueId

        MailStorage.clearReadAsync(playerId) { removed ->
            val onlinePlayer = Bukkit.getPlayer(playerId) ?: return@clearReadAsync
            if (removed == 0) {
                onlinePlayer.sendMessage(allTags.deserialize("<gray>You have no read mail to clear.</gray>"))
            } else {
                onlinePlayer.sendMessage(allTags.deserialize("<green>Cleared <white>$removed</white> read messages.</green>"))
            }
        }
    }

    private fun renderInbox(css: CommandSourceStack, requestedPage: Int) {
        val player = css.requirePlayer() ?: return
        val playerId = player.uniqueId

        if (requestedPage < 1) {
            player.sendMessage(allTags.deserialize("<red>Page number must be at least 1.</red>"))
            return
        }

        MailStorage.listMailAsync(playerId) { messages ->
            val onlinePlayer = Bukkit.getPlayer(playerId) ?: return@listMailAsync
            if (messages.isEmpty()) {
                onlinePlayer.sendMessage(allTags.deserialize("<gray>Your mailbox is empty.</gray>"))
                return@listMailAsync
            }

            val totalPages = ceil(messages.size.toDouble() / pageSize.toDouble()).toInt().coerceAtLeast(1)
            if (requestedPage > totalPages) {
                onlinePlayer.sendMessage(allTags.deserialize("<red>Page <white>$requestedPage</white> does not exist. Max page is <white>$totalPages</white>.</red>"))
                return@listMailAsync
            }

            val startIndex = (requestedPage - 1) * pageSize
            val endIndex = minOf(startIndex + pageSize, messages.size)
            val pageMessages = messages.subList(startIndex, endIndex)

            onlinePlayer.sendMessage(
                allTags.deserialize(
                    "<notifcolor><b>Mailbox</b></notifcolor> <gray>(${messages.size} messages | page $requestedPage/$totalPages)</gray>"
                )
            )

            pageMessages.forEach { message ->
                onlinePlayer.sendMessage(toInboxLine(message))
            }

            val controls = mutableListOf<Component>()
            if (requestedPage > 1) {
                controls += Component.text("[Prev]", NamedTextColor.YELLOW)
                    .clickEvent(ClickEvent.runCommand("/mail inbox ${requestedPage - 1}"))
                    .hoverEvent(HoverEvent.showText(Component.text("Go to page ${requestedPage - 1}", NamedTextColor.GRAY)))
            }

            if (requestedPage < totalPages) {
                controls += Component.text("[Next]", NamedTextColor.YELLOW)
                    .clickEvent(ClickEvent.runCommand("/mail inbox ${requestedPage + 1}"))
                    .hoverEvent(HoverEvent.showText(Component.text("Go to page ${requestedPage + 1}", NamedTextColor.GRAY)))
            }

            if (controls.isNotEmpty()) {
                val line = controls.drop(1).fold(controls.first()) { acc, component ->
                    acc.append(Component.text(" ", NamedTextColor.DARK_GRAY)).append(component)
                }
                onlinePlayer.sendMessage(line)
            }
        }
    }

    private fun toInboxLine(message: MailStorage.StoredMail): Component {
        val statusColor = if (message.isRead) NamedTextColor.DARK_GRAY else NamedTextColor.GREEN
        val senderName = resolveSenderName(message.senderId)
        val preview = message.body.replace("\n", " ").take(42)
        val suffix = if (message.body.length > 42) "..." else ""
        val age = formatRelative(message.sentAtMillis)

        return Component.text()
            .append(Component.text("#${message.id} ", NamedTextColor.AQUA))
            .append(Component.text(if (message.isRead) "[read] " else "[new] ", statusColor))
            .append(Component.text(senderName, NamedTextColor.WHITE))
            .append(Component.text(" - $preview$suffix", NamedTextColor.GRAY))
            .append(Component.text(" ($age ago)", NamedTextColor.DARK_GRAY))
            .clickEvent(ClickEvent.runCommand("/mail read ${message.id}"))
            .hoverEvent(HoverEvent.showText(Component.text("Click to read mail #${message.id} (delete: /mail delete ${message.id})", NamedTextColor.GREEN)))
            .build()
    }

    private fun resolveRecipient(input: String): Pair<UUID, String>? {
        val online = Bukkit.getPlayerExact(input)
        if (online != null) {
            return online.uniqueId to online.name
        }

        val offline = Bukkit.getOfflinePlayer(input)
        if (!offline.hasPlayedBefore() && !offline.isOnline) {
            return null
        }

        val name = offline.name ?: input
        return offline.uniqueId to name
    }

    private fun resolveSenderName(senderId: UUID?): String {
        if (senderId == null) {
            return "Server"
        }

        val online = Bukkit.getPlayer(senderId)
        if (online != null) {
            return online.name
        }

        return Bukkit.getOfflinePlayer(senderId).name ?: senderId.toString().take(8)
    }

    private fun formatRelative(sentAtMillis: Long): String {
        val age = Duration.between(Instant.ofEpochMilli(sentAtMillis), Instant.now())
        val days = age.toDays()
        val hours = age.toHours()
        val minutes = age.toMinutes()

        return when {
            days > 0 -> "${days}d"
            hours > 0 -> "${hours}h"
            minutes > 0 -> "${minutes}m"
            else -> "moments"
        }
    }
}