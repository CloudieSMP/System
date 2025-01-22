package moe.sebiann.system;

import moe.sebiann.system.commands.*;
import moe.sebiann.system.listeners.*;
import moe.sebiann.system.util.*;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class System extends JavaPlugin {

    public static System plugin = null;
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

        registerClasses();
        registerCommands();
        registerEvents();
        commandCompletions();

        getLogger().info("|                                                        |");
        getLogger().info("|-----------------------------[ ENABLED SUCCESSFULLY ]---|");
    }

    void registerCommands(){
        manager.registerCommand(new SystemCommand());
        manager.registerCommand(new FlyCommand());
        manager.registerCommand(new ReskinCommand());
        manager.registerCommand(new PissCommand());
        manager.registerCommand(new RulesCommand());

        //if renameCommands are enabled
        if(getConfig().getBoolean("ShortenCommands")){
            manager.registerCommand(new ShortenCommands());
        }

        getLogger().info("|   Enabled commands                                     |");
    }

    void registerEvents() {
        // this one is for saying welcome
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(getConfig()), this);

        // this one is for invisible Item Frames
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);

        this.getServer().getPluginManager().registerEvents(new FlyListener(), this);

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
    }

    @Override
    public void onDisable() {
        getLogger().info("|---[ CloudieSMP ]---------------------------------------|");
        getLogger().info("|                                                        |");
        getLogger().info("|                                                        |");
        getLogger().info("|----------------------------[ DISABLED SUCCESSFULLY ]---|");
    }
}
