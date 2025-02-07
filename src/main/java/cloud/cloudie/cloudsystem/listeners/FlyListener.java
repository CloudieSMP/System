package cloud.cloudie.cloudsystem.listeners;

import cloud.cloudie.cloudsystem.CloudSystem;
import cloud.cloudie.cloudsystem.util.HexToRGB;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import static cloud.cloudie.cloudsystem.CloudSystem.particleTasks;

public class FlyListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.isFlying()) {
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            if (user == null) {
                return;
            }

            String primaryGroup = user.getPrimaryGroup();
            Group group = luckPerms.getGroupManager().getGroup(primaryGroup);
            if (group == null) {
                return;
            }

            String hexColor = "#FFFFFF";
            String prefix = group.getCachedData().getMetaData().getMetaValue("flycolor");
            if (prefix != null) {
                hexColor = prefix;
            }

            int[] rgb = HexToRGB.hexToRgb(hexColor);
            BukkitTask previousTask = particleTasks.get(player.getUniqueId());
            if (previousTask != null) {
                previousTask.cancel();
            }

            if(player.getGameMode().equals(GameMode.SPECTATOR)){
                return;
            }
            PotionEffect effect = event.getPlayer().getPotionEffect(PotionEffectType.INVISIBILITY);
            if (effect != null)
            {
                return;
            }

            BukkitTask particleTask = Bukkit.getScheduler().runTaskTimer(CloudSystem.plugin, () -> {
                Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(rgb[0], rgb[1], rgb[2]), Color.WHITE, 2.0F);
                player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, player.getLocation(), 1, dustTransition);
            }, 0L, 5L);
            particleTasks.put(player.getUniqueId(), particleTask);
        } else {
            BukkitTask particleTask = particleTasks.get(player.getUniqueId());
            if (particleTask != null) {
                particleTask.cancel();
                particleTasks.remove(player.getUniqueId());
            }
        }
    }
}