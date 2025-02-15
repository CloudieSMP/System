package cloud.cloudie.cloudsystem;

import cloud.cloudie.cloudsystem.commands.FlyCommand;
import cloud.cloudie.cloudsystem.commands.GiveCommand;
import cloud.cloudie.cloudsystem.commands.PissCommand;
import cloud.cloudie.cloudsystem.commands.ReskinCommand;
import cloud.cloudie.cloudsystem.commands.RulesGUICommand;
import cloud.cloudie.cloudsystem.commands.ShortenCommands;
import cloud.cloudie.cloudsystem.commands.SystemCommand;
import cloud.cloudie.cloudsystem.listeners.FlyListener;
import cloud.cloudie.cloudsystem.listeners.PlayerInteractListener;
import cloud.cloudie.cloudsystem.listeners.PlayerJoinListener;
import cloud.cloudie.cloudsystem.util.AutoRestart;

import cloud.cloudie.cloudsystem.util.UpdateChecker;
import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CloudSystem extends JavaPlugin {

    public static CloudSystem plugin = null;
    PaperCommandManager manager;
    public static Map<UUID, BukkitTask> particleTasks = new HashMap<>();

    @Override
    public void onEnable() {
        if(plugin == null){
            plugin = this;
        }
        manager = new PaperCommandManager(this);
        // Save and load the configuration
        saveDefaultConfig();

        getLogger().info("|---[ CloudieSMP ]---------------------------------------|");
        getLogger().info("|                                                        |");

        commandCompletions();
        registerClasses();
        registerCommands();
        registerEvents();

        getLogger().info("|                                                        |");
        getLogger().info("|-----------------------------[ ENABLED SUCCESSFULLY ]---|");

        new UpdateChecker(this).checkForUpdates();
    }

    void registerCommands(){
        manager.registerCommand(new SystemCommand());
        manager.registerCommand(new FlyCommand());
        manager.registerCommand(new ReskinCommand());
        manager.registerCommand(new PissCommand());
        manager.registerCommand(new RulesGUICommand());
        manager.registerCommand(new GiveCommand());

        //if renameCommands are enabled
        if(getConfig().getBoolean("ShortenCommands")){
            manager.registerCommand(new ShortenCommands());
        }

        getLogger().info("|   Enabled commands                                     |");
    }

    void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new FlyListener(), this);
        getServer().getPluginManager().registerEvents(new RulesGUICommand(), this);

    getLogger().info("|   Enabled listeners                                    |");

    }
    void registerClasses() {
        AutoRestart.onAutoRestart();
        getLogger().info("|   Enabled Classes                                      |");
    }
    void commandCompletions(){
        manager.getCommandCompletions().registerAsyncCompletion("skins", c -> {

            List<String> skins = getConfig().getStringList("skins");
            return ImmutableList.copyOf(skins);
        });

        manager.getCommandCompletions().registerCompletion("Items", context -> Arrays.stream(Material.values())
                .map(Material::name)
                .toList());
    }

    @Override
    public void onDisable() {
        getLogger().info("|---[ CloudieSMP ]---------------------------------------|");
        getLogger().info("|                                                        |");
        getLogger().info("|                                                        |");
        getLogger().info("|----------------------------[ DISABLED SUCCESSFULLY ]---|");
    }
}
