package library

import logger
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import plugin
import java.io.File
import java.util.UUID

object HomeStorage {
    enum class SaveOutcome {
        CREATED,
        UPDATED,
        FAILED
    }

    private val homesDir: File
        get() = File(plugin.dataFolder, "homes")

    private fun homeFile(playerId: UUID): File = File(homesDir, "$playerId.yml")

    fun listHomeNames(playerId: UUID): List<String> {
        val config = loadConfig(playerId)
        val section = config.getConfigurationSection("homes") ?: return emptyList()
        return section.getKeys(false).sorted()
    }

    fun loadHome(playerId: UUID, name: String): Location? {
        val key = "homes.${name.lowercase()}"
        val config = loadConfig(playerId)
        val section = config.getConfigurationSection(key) ?: return null

        val worldName = section.getString("world") ?: return null
        val world = Bukkit.getWorld(worldName) ?: return null

        return Location(
            world,
            section.getDouble("x"),
            section.getDouble("y"),
            section.getDouble("z"),
            section.getDouble("yaw").toFloat(),
            section.getDouble("pitch").toFloat()
        )
    }

    fun saveHome(playerId: UUID, name: String, location: Location): SaveOutcome {
        val normalizedName = name.lowercase()
        val key = "homes.$normalizedName"

        val config = loadConfig(playerId)
        val alreadyExists = config.isConfigurationSection(key)

        config.set("$key.world", location.world?.name ?: return SaveOutcome.FAILED)
        config.set("$key.x", location.x)
        config.set("$key.y", location.y)
        config.set("$key.z", location.z)
        config.set("$key.yaw", location.yaw.toDouble())
        config.set("$key.pitch", location.pitch.toDouble())

        return if (saveConfig(playerId, config)) {
            if (alreadyExists) SaveOutcome.UPDATED else SaveOutcome.CREATED
        } else {
            SaveOutcome.FAILED
        }
    }

    fun deleteHome(playerId: UUID, name: String): Boolean {
        val key = "homes.${name.lowercase()}"
        val config = loadConfig(playerId)

        if (!config.isConfigurationSection(key)) {
            return false
        }

        config.set(key, null)
        if (!saveConfig(playerId, config)) {
            return false
        }

        val homesSection = config.getConfigurationSection("homes")
        if (homesSection == null || homesSection.getKeys(false).isEmpty()) {
            homeFile(playerId).delete()
        }

        return true
    }

    private fun loadConfig(playerId: UUID): YamlConfiguration {
        if (!homesDir.exists()) {
            homesDir.mkdirs()
        }

        val file = homeFile(playerId)
        return YamlConfiguration.loadConfiguration(file)
    }

    private fun saveConfig(playerId: UUID, config: YamlConfiguration): Boolean {
        return try {
            if (!homesDir.exists()) {
                homesDir.mkdirs()
            }
            config.save(homeFile(playerId))
            true
        } catch (exception: Exception) {
            logger.warning("Could not save homes for player $playerId: ${exception.message}")
            false
        }
    }
}