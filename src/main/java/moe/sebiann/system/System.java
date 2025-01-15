package moe.sebiann.system;

import moe.sebiann.system.commands.*;
import moe.sebiann.system.events.*;
import org.bukkit.plugin.java.JavaPlugin;

public class System extends JavaPlugin {

    @Override
    public void onEnable() {
        // Save and load the configuration
        saveDefaultConfig();

        // Register commands
        getCommand("system").setExecutor(new SystemCommand(this));
        getCommand("system").setTabCompleter(new SystemTabCompleter());

        // Register event listeners
        // this one is for saying welcome
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(getConfig()), this);

        // this one is for invisible Item Frames
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(getConfig()), this);

    }
}
