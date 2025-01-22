package moe.sebiann.system.listeners;

import moe.sebiann.system.System;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String welcomeMessage = System.plugin.getConfig().getString("messages.welcome", "Hello, {player}!");
        String personalizedMessage = welcomeMessage.replace("{player}", event.getPlayer().getName());
        event.getPlayer().sendMessage(Component.text(personalizedMessage));
    }
}
