package event.player

import Config
import Link
import ResourcePack
import chat.Formatting
import command.LiveUtil
import library.Translation
import logger
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.resource.ResourcePackInfo
import net.kyori.adventure.resource.ResourcePackRequest
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import util.sha1
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import org.bukkit.Bukkit

@Suppress("UnstableApiUsage")
class PlayerJoin : Listener {
    private val mm = MiniMessage.miniMessage()
    val resourcePacks = mutableListOf<ResourcePackInfo>()
    val links: List<Link>

    constructor(config: Config) {
        loadResourcePacks(config.resourcePacks)
        links = config.links.sortedBy { it.order }
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
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

        if(LiveUtil.isLive(e.player)) {
            e.player.sendMessage("Live mode enabled.")
            LiveUtil.onPlayerJoin(e.player)
        }

        e.player.sendLinks(Bukkit.getServerLinks())
    }

    private fun sendTabList(audience: Audience) {
        audience.sendPlayerListHeader(mm.deserialize("<newline><newline><newline><newline><newline>     \uE000    <newline>"))
        audience.sendPlayerListFooter(mm.deserialize("<newline><gradient:#DF6F69:#C45889:#823BC6>  Cloudie SMP<white>: Season 10  <newline>"))
    }

    private fun loadResourcePacks(configPacks: List<ResourcePack>) {
        configPacks.sortedBy { it.priority }.forEach {
            if (it.hash.isEmpty()) {
                logger.info("Hash missing for ${it.uri}, downloading...")
                val hash = getHashForUri(it.uri)
                logger.info("Calculated hash $hash for pack ${it.uri}")
                resourcePacks.add(
                    ResourcePackInfo.resourcePackInfo()
                        .uri(it.uri)
                        .hash(hash)
                        .build()
                )
            } else {
                resourcePacks.add(
                    ResourcePackInfo.resourcePackInfo()
                        .uri(it.uri)
                        .hash(it.hash)
                        .build()
                )
            }
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