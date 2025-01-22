package moe.sebiann.system.commands;
import moe.sebiann.system.System;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

@CommandAlias("piss")
@CommandPermission("system.piss")
public class PissCommand extends BaseCommand {

    @Default
    @CommandCompletion("public")
    public void onPissCommand(Player player, @Optional String mode) {
        boolean isPublic = mode != null && mode.equalsIgnoreCase("public");

        if (isPublic) {
            // Broadcast to the entire server
            System.plugin.getServer().broadcast(Component.text(player.getName() + " just marked their territory! 🟡💦"));
        } else {
            // Send only to the player
            String[] messages = {
                    "You just marked your territory! 🟡",
                    "Feeling relieved? 💦",
                    "Nature calls! 🌿"
            };
            player.sendMessage(messages[(int) (Math.random() * messages.length)]);
        }

        Location startLocation = player.getLocation().add(0, 0.7, 0); // Start at player's waist level
        Vector direction = player.getLocation().getDirection().normalize();
        direction.setY(-0.2); // Fixed downward angle
        direction.normalize();

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 20) { // Effect lasts 1 seconds
                    cancel();
                    return;
                }

                for (int i = 0; i < 5; i++) {
                    double spread = 0.1;
                    Vector randomOffset = new Vector(
                            (Math.random() - 0.5) * spread,
                            (Math.random() - 0.5) * spread,
                            (Math.random() - 0.5) * spread
                    );

                    Location particleLocation = startLocation.clone().add(direction.clone().multiply(ticks * 0.2)).add(randomOffset);
                    player.getWorld().spawnParticle(
                            Particle.DUST,
                            particleLocation,
                            1,
                            new Particle.DustOptions(org.bukkit.Color.YELLOW, 1.2F)
                    );

                    // Create a splash effect where the particle lands
                    if (ticks % 5 == 0) { // Add splash effect periodically
                        player.getWorld().spawnParticle(
                                Particle.SPLASH,
                                particleLocation,
                                5, // Splash density
                                0.2, 0.1, 0.2 // Small area spread
                        );
                        player.getWorld().playSound(particleLocation, Sound.ENTITY_GENERIC_SPLASH, 0.5F, 1.0F);
                    }
                }

                ticks++;
            }
        }.runTaskTimer(System.plugin, 0L, 1L);

        // Add sound effect
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1.0F, 1.0F);
    }
}
