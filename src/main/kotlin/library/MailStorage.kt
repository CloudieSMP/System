package library

import logger
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import plugin
import java.io.File
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object MailStorage {
    data class StoredMail(
        val id: Long,
        val senderId: UUID?,
        val body: String,
        val sentAtMillis: Long,
        val readAtMillis: Long?
    ) {
        val isRead: Boolean
            get() = readAtMillis != null

        fun withReadNow(): StoredMail {
            if (isRead) {
                return this
            }
            return copy(readAtMillis = System.currentTimeMillis())
        }
    }

    private data class PlayerMailCache(
        var nextId: Long = 1L,
        val mail: MutableList<StoredMail> = mutableListOf(),
        var dirty: Boolean = false
    )

    private const val MAX_MAILS_PER_PLAYER = 200

    private val mailDir: File
        get() = File(plugin.dataFolder, "mail")

    private val cache = ConcurrentHashMap<UUID, PlayerMailCache>()
    private val loadCallbacks = ConcurrentHashMap<UUID, MutableList<(PlayerMailCache) -> Unit>>()
    private val queuedSaves = ConcurrentHashMap.newKeySet<UUID>()

    private fun mailFile(playerId: UUID): File = File(mailDir, "$playerId.yml")

    fun preload(playerId: UUID) {
        ensureLoaded(playerId) { }
    }

    fun listMailAsync(playerId: UUID, callback: (List<StoredMail>) -> Unit) {
        ensureLoaded(playerId) { mailbox ->
            callback(synchronized(mailbox) { mailbox.mail.sortedByDescending { it.sentAtMillis } })
        }
    }

    fun hasNewMailAsync(playerId: UUID, callback: (Boolean) -> Unit) {
        ensureLoaded(playerId) { mailbox ->
            callback(synchronized(mailbox) { mailbox.mail.any { !it.isRead } })
        }
    }

    fun addMailAsync(playerId: UUID, senderId: UUID?, body: String, callback: (StoredMail) -> Unit) {
        ensureLoaded(playerId) { mailbox ->
            val mail = synchronized(mailbox) {
                val created = StoredMail(
                    id = mailbox.nextId++,
                    senderId = senderId,
                    body = body,
                    sentAtMillis = System.currentTimeMillis(),
                    readAtMillis = null
                )

                mailbox.mail += created
                while (mailbox.mail.size > MAX_MAILS_PER_PLAYER) {
                    mailbox.mail.removeAt(0)
                }

                mailbox.dirty = true
                created
            }

            scheduleSave(playerId)
            callback(mail)
        }
    }

    fun markReadAsync(playerId: UUID, id: Long, callback: (StoredMail?) -> Unit) {
        ensureLoaded(playerId) { mailbox ->
            var changed = false
            val resolved = synchronized(mailbox) {
                val index = mailbox.mail.indexOfFirst { it.id == id }
                if (index < 0) {
                    null
                } else {
                    val updated = mailbox.mail[index].withReadNow()
                    if (updated != mailbox.mail[index]) {
                        mailbox.mail[index] = updated
                        mailbox.dirty = true
                        changed = true
                    }
                    updated
                }
            }

            if (changed) {
                scheduleSave(playerId)
            }

            callback(resolved)
        }
    }

    fun deleteMailAsync(playerId: UUID, id: Long, callback: (Boolean) -> Unit) {
        ensureLoaded(playerId) { mailbox ->
            val deleted = synchronized(mailbox) {
                val removed = mailbox.mail.removeIf { it.id == id }
                if (removed) {
                    mailbox.dirty = true
                }
                removed
            }

            if (deleted) {
                scheduleSave(playerId)
            }

            callback(deleted)
        }
    }

    fun clearReadAsync(playerId: UUID, callback: (Int) -> Unit) {
        ensureLoaded(playerId) { mailbox ->
            val removedCount = synchronized(mailbox) {
                val before = mailbox.mail.size
                mailbox.mail.removeAll { it.isRead }
                val removed = before - mailbox.mail.size
                if (removed > 0) {
                    mailbox.dirty = true
                }
                removed
            }

            if (removedCount > 0) {
                scheduleSave(playerId)
            }

            callback(removedCount)
        }
    }

    fun flushAllSync() {
        cache.forEach { (playerId, mailbox) ->
            flushSync(playerId, mailbox)
        }
    }

    private fun ensureLoaded(playerId: UUID, callback: (PlayerMailCache) -> Unit) {
        val cached = cache[playerId]
        if (cached != null) {
            callback(cached)
            return
        }

        var shouldLoad = false
        loadCallbacks.compute(playerId) { _, existing ->
            val callbacks = existing ?: mutableListOf()
            callbacks += callback
            if (existing == null) {
                shouldLoad = true
            }
            callbacks
        }

        if (!shouldLoad) {
            return
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            val loaded = loadFromDisk(playerId)
            cache[playerId] = loaded
            val callbacks = loadCallbacks.remove(playerId).orEmpty()

            Bukkit.getScheduler().runTask(plugin, Runnable {
                callbacks.forEach { it(loaded) }
            })
        })
    }

    private fun scheduleSave(playerId: UUID) {
        if (!queuedSaves.add(playerId)) {
            return
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, Runnable {
            queuedSaves.remove(playerId)
            val mailbox = cache[playerId] ?: return@Runnable
            flushAsync(playerId, mailbox)
        }, 20L)
    }

    private fun loadFromDisk(playerId: UUID): PlayerMailCache {
        if (!mailDir.exists()) {
            mailDir.mkdirs()
        }

        val config = YamlConfiguration.loadConfiguration(mailFile(playerId))
        val mailEntries = config.getMapList("mail")

        val loadedMail = mailEntries.mapNotNull { map ->
            val id = (map["id"] as? Number)?.toLong() ?: return@mapNotNull null
            val body = map["body"] as? String ?: return@mapNotNull null
            val sender = (map["sender"] as? String)?.let {
                runCatching { UUID.fromString(it) }.getOrNull()
            }
            val sentAt = (map["sentAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
            val readAt = (map["readAt"] as? Number)?.toLong()

            StoredMail(
                id = id,
                senderId = sender,
                body = body,
                sentAtMillis = sentAt,
                readAtMillis = readAt
            )
        }.sortedBy { it.id }

        val storedNext = config.getLong("nextId")
        val nextId = if (storedNext > 0) {
            storedNext
        } else {
            (loadedMail.maxOfOrNull { it.id } ?: 0L) + 1L
        }

        return PlayerMailCache(nextId = nextId, mail = loadedMail.toMutableList())
    }

    private fun flushAsync(playerId: UUID, mailbox: PlayerMailCache) {
        val snapshot = synchronized(mailbox) {
            if (!mailbox.dirty) {
                null
            } else {
                mailbox.dirty = false
                mailbox.nextId to mailbox.mail.toList()
            }
        } ?: return

        if (!saveSnapshot(playerId, snapshot.first, snapshot.second)) {
            synchronized(mailbox) {
                mailbox.dirty = true
            }
        }
    }

    private fun flushSync(playerId: UUID, mailbox: PlayerMailCache) {
        val snapshot = synchronized(mailbox) {
            if (!mailbox.dirty) {
                null
            } else {
                mailbox.dirty = false
                mailbox.nextId to mailbox.mail.toList()
            }
        } ?: return

        if (!saveSnapshot(playerId, snapshot.first, snapshot.second)) {
            synchronized(mailbox) {
                mailbox.dirty = true
            }
        }
    }

    private fun saveSnapshot(playerId: UUID, nextId: Long, mail: List<StoredMail>): Boolean {
        return try {
            if (!mailDir.exists()) {
                mailDir.mkdirs()
            }

            if (mail.isEmpty()) {
                val file = mailFile(playerId)
                if (file.exists()) {
                    file.delete()
                }
                return true
            }

            val config = YamlConfiguration()
            config.set("nextId", nextId)
            config.set(
                "mail",
                mail.sortedBy { it.id }.map { entry ->
                    buildMap<String, Any> {
                        put("id", entry.id)
                        put("body", entry.body)
                        put("sentAt", entry.sentAtMillis)
                        entry.senderId?.let { put("sender", it.toString()) }
                        entry.readAtMillis?.let { put("readAt", it) }
                    }
                }
            )

            config.save(mailFile(playerId))
            true
        } catch (exception: Exception) {
            logger.warning("Could not save mail for player $playerId: ${exception.message}")
            false
        }
    }
}

