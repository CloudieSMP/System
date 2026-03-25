package util

import plugin
import logger

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

import kotlinx.coroutines.runBlocking

import net.kyori.adventure.resource.ResourcePackInfo
import net.kyori.adventure.resource.ResourcePackRequest
import org.bukkit.entity.Player

import java.net.URI
import java.security.MessageDigest
import java.util.*

object ResourcePacker {
    data class CachedPackMeta(
        val uri: URI,
        val priority: Int,
        val hash: String,
        val hashSource: String
    )

    data class CacheStatus(
        val configuredCount: Int,
        val cachedCount: Int,
        val lastRefreshAtMillis: Long?,
        val lastError: String?,
        val cached: List<CachedPackMeta>
    )

    private val client = HttpClient(CIO)

    @Volatile
    private var cachedPacks: List<ResourcePackInfo> = emptyList()

    @Volatile
    private var cachedMeta: List<CachedPackMeta> = emptyList()

    @Volatile
    private var lastRefreshAtMillis: Long? = null

    @Volatile
    private var lastError: String? = null

    fun applyPackPlayer(player: Player) = runBlocking {
        val packs = cachedPacks
        if (packs.isEmpty()) {
            logger.warning("Resource pack cache is empty; skipping apply for ${player.name}.")
            return@runBlocking
        }

        player.sendResourcePacks(
            ResourcePackRequest.resourcePackRequest().packs(packs)
        )
    }

    fun removePackPlayer(player: Player) {
        player.removeResourcePacks()
        player.clearResourcePacks()
    }

    fun refreshFromUrl(): Boolean = runBlocking {
        val configured = plugin.config.resourcePacks.sortedByDescending { it.priority }
        if (configured.isEmpty()) {
            cachedPacks = emptyList()
            cachedMeta = emptyList()
            lastRefreshAtMillis = System.currentTimeMillis()
            lastError = null
            logger.warning("No resource packs configured; cache cleared.")
            return@runBlocking true
        }

        val nextPacks = mutableListOf<ResourcePackInfo>()
        val nextMeta = mutableListOf<CachedPackMeta>()

        try {
            configured.forEach { pack ->
                val configuredHash = pack.hash.trim()
                val resolvedHash = if (configuredHash.isNotEmpty()) {
                    configuredHash
                } else {
                    hash(fetch(pack.uri.toString()))
                }

                nextPacks += ResourcePackInfo.resourcePackInfo(UUID.randomUUID(), pack.uri, resolvedHash)
                nextMeta += CachedPackMeta(
                    uri = pack.uri,
                    priority = pack.priority,
                    hash = resolvedHash,
                    hashSource = if (configuredHash.isNotEmpty()) "config" else "download"
                )
            }

            cachedPacks = nextPacks
            cachedMeta = nextMeta
            lastRefreshAtMillis = System.currentTimeMillis()
            lastError = null
            true
        } catch (e: Exception) {
            lastError = e.message ?: e::class.simpleName ?: "Unknown refresh error"
            logger.severe("Failed to refresh resource pack cache from URL\nStack Trace:\n${e.stackTrace}\nMessage:\n${e.message}")
            false
        }
    }

    fun cacheStatus(): CacheStatus {
        return CacheStatus(
            configuredCount = plugin.config.resourcePacks.size,
            cachedCount = cachedPacks.size,
            lastRefreshAtMillis = lastRefreshAtMillis,
            lastError = lastError,
            cached = cachedMeta
        )
    }

    private suspend fun fetch(url: String): ByteArray {
        val response: HttpResponse = client.get(url)
        return response.readBytes()
    }

    private fun hash(data: ByteArray): String {
        val messageDigest = MessageDigest.getInstance("SHA-1")
        val hashBytes = messageDigest.digest(data)
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}