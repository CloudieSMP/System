package event.player

import Config
import ResourcePack
import chat.Formatting
import command.LiveUtil
import library.HomeStorage
import library.Translation
import logger
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.resource.ResourcePackInfo
import net.kyori.adventure.resource.ResourcePackRequest
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import library.VanishHelper
import util.sha1
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import org.bukkit.Bukkit

@Suppress("UnstableApiUsage")
class PlayerJoin : Listener {
    private val mm = MiniMessage.miniMessage()
    private var resourcePacks: List<ResourcePackInfo> = emptyList()

    fun updateConfig(config: Config) {
        resourcePacks = buildResourcePacks(config.resourcePacks)
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        HomeStorage.preload(e.player.uniqueId)
        sendTabList(e.player)

        val brand = e.player.clientBrandName
            ?.replaceFirstChar { it.uppercaseChar() }
            ?: "Unknown"
        logger.info("(BRAND) ${e.player.name} joined using $brand.")

        val resourcePackRequest = ResourcePackRequest.resourcePackRequest()
            .packs(resourcePacks)
            .prompt(mm.deserialize("<gradient:#DF6F69:#C45889:#823BC6>Please download the required resource packs for Cloudie</gradient>"))
            .build()

        e.player.sendResourcePacks(resourcePackRequest)
        e.player.sendMessage(mm.deserialize("<red>⚠ <reset>Please <b>do not</b> break loot chests!"))
        if(e.player.hasPermission("cloudie.silent.join")) {
            e.joinMessage(null)
        } else {
            e.joinMessage(Formatting.allTags.deserialize(Translation.PlayerMessages.JOIN.replace("%player%", e.player.name)))
        }

        LiveUtil.onPlayerJoin(e.player)
        VanishHelper.syncVisibilityForJoin(e.player)

        e.player.sendLinks(Bukkit.getServerLinks())
    }

    private fun sendTabList(audience: Audience) {
        audience.sendPlayerListHeader(mm.deserialize("<newline><newline><newline><newline><newline>     \uE000    <newline>"))
        audience.sendPlayerListFooter(mm.deserialize("<newline><gradient:#DF6F69:#C45889:#823BC6>  Cloudie SMP<white>: Season 10  <newline>"))
    }

    private fun buildResourcePacks(configPacks: List<ResourcePack>): List<ResourcePackInfo> {
        return configPacks.sortedBy { it.priority }.map {
            val hash = it.hash.ifEmpty {
                logger.info("Hash missing for ${it.uri}, downloading...")
                getHashForUri(it.uri).also { calculatedHash ->
                    logger.info("Calculated hash $calculatedHash for pack ${it.uri}")
                }
            }

            ResourcePackInfo.resourcePackInfo()
                .uri(it.uri)
                .hash(hash)
                .build()
        }
    }

    private fun getHashForUri(packURI: URI): String {
        val client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build()
        val request = HttpRequest.newBuilder(packURI).build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofByteArray())

        if (response.statusCode() != 200) throw RuntimeException("Resourcepack download $packURI failed")

        return sha1(response.body())
    }

}