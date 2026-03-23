package command

import chat.Formatting.allTags
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.processing.CommandContainer
import plugin
import util.requirePlayer
import java.util.UUID


@Suppress("unused")
@CommandContainer
class Tpa {
    private val tpaRequests = mutableListOf<TpaRequest>()

    private val requestExpireTime: Int
        get() = plugin.config.tpa.requestExpireTime

    private val tpaDelay: Int
        get() = plugin.config.tpa.tpaDelay

    @Command("tpa <player>")
    @CommandDescription("Request to teleport to another player.")
    @Permission("cloudie.command.tpa")
    fun tpa(css: CommandSourceStack, @Argument("player") targetPlayer: Player) {
        createRequest(css, targetPlayer, TpaType.TPA_THERE)
    }

    @Command("tpahere <player>")
    @CommandDescription("Request to teleport to another player.")
    @Permission("cloudie.command.tpa")
    fun tpahere(css: CommandSourceStack, @Argument("player") targetPlayer: Player) {
        createRequest(css, targetPlayer, TpaType.TPA_HERE)
    }

    @Command("tpaccept|tpyes")
    @CommandDescription("Accept a tpa request.")
    @Permission("cloudie.command.tpa")
    fun tpaccept(css: CommandSourceStack) {
        val player = css.requirePlayer() ?: return

        val tpaRequest = findIncomingRequest(player.uniqueId)
        if (tpaRequest == null) {
            player.sendMessage(allTags.deserialize("<yellow>You don't have any incoming TPA requests."))
            return
        }

        val targetPlayer: Player = Bukkit.getPlayer(tpaRequest.requester) ?: run {
            player.sendMessage(allTags.deserialize("<yellow>Player is no longer online."))
            tpaRequests.remove(tpaRequest)
            return
        }

        tpaRequests.remove(tpaRequest)

        if (tpaRequest.type == TpaType.TPA_THERE) {
            player.sendMessage(allTags.deserialize(
                "<yellow>TPA request from <green>${targetPlayer.name}</green> accepted.\nThey will be teleported in <green>$tpaDelay</green> seconds."
            ))
            targetPlayer.sendMessage(allTags.deserialize(
                "<yellow>TPA request to <green>${player.name}</green> accepted.\nYou will be teleported to them in <green>$tpaDelay</green> seconds."
            ))

            Bukkit.getScheduler().runTaskLater(plugin, Runnable { targetPlayer.teleport(player) }, tpaDelay * 20L)
        } else {
            player.sendMessage(allTags.deserialize(
                "<yellow>TPA request from <green>${targetPlayer.name}</green> accepted.\nYou will be teleported in <green>$tpaDelay</green> seconds."
            ))
            targetPlayer.sendMessage(allTags.deserialize(
                "<yellow>TPA request to <green>${player.name}</green> accepted.\nThey will be teleported to you in <green>$tpaDelay</green> seconds."
            ))

            Bukkit.getScheduler().runTaskLater(plugin, Runnable { player.teleport(targetPlayer) }, tpaDelay * 20L)
        }
    }

    @Command("tpdeny|tpno")
    @CommandDescription("Deny a tpa request.")
    @Permission("cloudie.command.tpa")
    fun tpdeny(css: CommandSourceStack) {
        val player = css.requirePlayer() ?: return

        val tpaRequest = findIncomingRequest(player.uniqueId)
        if (tpaRequest == null) {
            player.sendMessage(allTags.deserialize("<yellow>You don't have any incoming TPA requests."))
            return
        }

        val target = Bukkit.getPlayer(tpaRequest.requester)
        val targetOffline = Bukkit.getOfflinePlayer(tpaRequest.requester)

        tpaRequests.remove(tpaRequest)

        player.sendMessage(allTags.deserialize(
            "<red>TPA request from <yellow>${targetOffline.name}</yellow> denied."
        ))

        target?.sendMessage(allTags.deserialize(
            "<red>TPA request to <yellow>${player.name}</yellow> denied."
        ))
    }

    private fun createRequest(css: CommandSourceStack, targetPlayer: Player, type: TpaType) {
        val player = css.requirePlayer() ?: return
        if (player.uniqueId == targetPlayer.uniqueId) {
            player.sendMessage(allTags.deserialize("<red>You cannot send a TPA request to yourself."))
            return
        }

        if (hasOutgoingRequest(player.uniqueId)) {
            player.sendMessage(allTags.deserialize("<red>You already have an outgoing TPA request to this player."))
            return
        }

        val tpaRequest = TpaRequest(player.uniqueId, targetPlayer.uniqueId, type)
        tpaRequests.add(tpaRequest)
        deleteTpaAfterDelay(player, targetPlayer, requestExpireTime, tpaRequest)

        player.sendMessage(allTags.deserialize(
            "<yellow>Teleport request sent to <green>${targetPlayer.name}</green>.\nRequest will time out in <green>$requestExpireTime</green> seconds."
        ))
        targetPlayer.sendMessage(allTags.deserialize(
            "<yellow>You have an incoming TPA request from <green>${player.name}</green>. Type <click:run_command:'/tpaccept'><hover:show_text:'Accepts the TPA request.'><white>/tpaccept</white></hover></click> to accept or <click:run_command:'/tpdeny'><hover:show_text:'Denies the TPA request.'><white>/tpdeny</white></hover></click> to deny."
        ))
    }

    private fun hasOutgoingRequest(requesterId: UUID): Boolean {
        return tpaRequests.any { it.requester == requesterId }
    }

    private fun findIncomingRequest(targetId: UUID): TpaRequest? {
        return tpaRequests.firstOrNull { it.target == targetId }
    }

    private fun deleteTpaAfterDelay(player: Player, target: Player, requestTimeout: Int, tpaRequest: TpaRequest) {
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            if (tpaRequests.remove(tpaRequest)) {
                player.sendMessage(allTags.deserialize("<yellow>TPA Request to ${target.name} has timed out."))

                target.sendMessage(allTags.deserialize("<yellow>TPA Request from <green>${player.name}</green> has timed out."))
            }
        }, requestTimeout * 20L)
    }

    data class TpaRequest(
        val requester: UUID,
        val target: UUID,
        val type: TpaType = TpaType.TPA_THERE
    )

    enum class TpaType {
        TPA_THERE,
        TPA_HERE
    }
}
