package library

import logger
import org.bukkit.configuration.file.YamlConfiguration
import plugin
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object CardPullCounterStorage {
    private val counts = ConcurrentHashMap<String, Long>()
    @Volatile
    private var loaded = false

    private val file: File
        get() = File(plugin.dataFolder, "card-pulls.yml")

    fun loadSync() {
        if (loaded) return

        try {
            if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()
            val backingFile = file
            if (!backingFile.exists()) {
                loaded = true
                return
            }

            val yaml = YamlConfiguration.loadConfiguration(backingFile)
            val section = yaml.getConfigurationSection("counts")
            if (section != null) {
                for (key in section.getKeys(false)) {
                    val count = section.getLong(key)
                    if (count > 0L) counts[key] = count
                }
            }
            loaded = true
        } catch (ex: Exception) {
            logger.warning("Failed to load card pull counts: ${ex.message}")
            loaded = true
        }
    }

    fun incrementAndGet(cardId: String): Long {
        if (!loaded) loadSync()
        return counts.merge(cardId, 1L, Long::plus) ?: 1L
    }

    fun get(cardId: String): Long {
        if (!loaded) loadSync()
        return counts[cardId] ?: 0L
    }

    fun flushAllSync() {
        if (!loaded) return

        try {
            if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()

            val yaml = YamlConfiguration()
            val section = yaml.createSection("counts")
            for ((cardId, count) in counts.toSortedMap()) {
                section.set(cardId, count)
            }
            yaml.save(file)
        } catch (ex: Exception) {
            logger.warning("Failed to save card pull counts: ${ex.message}")
        }
    }
}
