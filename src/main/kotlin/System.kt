import chat.Formatting.allTags
import chat.VisualChat
import event.ServerListEvent
import event.player.*
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.PaperCommandManager
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import util.ui.GamblingWindow
import util.ui.CrateBrowserWindow
import java.io.File

@Suppress( "unstableApiUsage")
class System : JavaPlugin() {
    private lateinit var commandManager: PaperCommandManager<CommandSourceStack>
    lateinit var config: Config
        private set

    override fun onEnable() {
        this.logger.info("Starting the Cloudie System plugin!")
        reloadConfig()
        setupEvents()
        registerCommands()
        VisualChat.clearChatEntities()
        config.links.forEach {
            Bukkit.getServerLinks().addLink(allTags.deserialize(it.component), it.uri)
        }
    }

    override fun onDisable() {
        this.logger.info("Stopping the Cloudie System plugin!")
        VisualChat.clearChatEntities()
    }

    private fun registerCommands() {
        commandManager = PaperCommandManager.builder()
            .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
            .buildOnEnable(this)

        val annotationParser = AnnotationParser(commandManager, CommandSourceStack::class.java)
        annotationParser.parseContainers()
    }

    private fun setupEvents() {
        server.pluginManager.registerEvents(ServerListEvent(), this)
        server.pluginManager.registerEvents(PlayerJoin(config), this)
        server.pluginManager.registerEvents(PlayerQuit(), this)
        server.pluginManager.registerEvents(ChatEvent(), this)
        server.pluginManager.registerEvents(PlayerInteractEntity(), this)
        server.pluginManager.registerEvents(PlayerItemConsume(), this)
        server.pluginManager.registerEvents(CrateBrowserWindow, this)
        server.pluginManager.registerEvents(GamblingWindow, this)
    }

    override fun reloadConfig() {
        if (!dataFolder.exists()) dataFolder.mkdir()
        val configFile = File(dataFolder, "config.yml")

        if (!configFile.exists()) {
            getResource("config.yml").use { inputStream ->
                configFile.outputStream().use { outputStream ->
                    inputStream!!.copyTo(outputStream)
                }
            }
        }

        val loader = YamlConfigurationLoader.builder()
            .file(configFile)
            .defaultOptions { options ->
                options.serializers { builder ->
                    builder.registerAnnotatedObjects(objectMapperFactory())
                }
            }
            .build()

        val node = loader.load()
        config = node.get(Config::class)!!
        logger.info("Loaded configuration.")
    }
}

val plugin = Bukkit.getPluginManager().getPlugin("system")!!
val logger = plugin.logger
//val config: Config get() = (plugin as System).config