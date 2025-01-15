package moe.sebiann.system.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractListener implements Listener {

    private final FileConfiguration config;

    public PlayerInteractListener(FileConfiguration config) {
        this.config = config;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        // Ensure interaction is only from the main hand
        if (!event.getHand().equals(org.bukkit.inventory.EquipmentSlot.HAND)) {
            return; // Ignore off-hand interactions
        }
        // Check if the entity is an ItemFrame
        Entity entity = event.getRightClicked();
        if (entity instanceof ItemFrame) {
            ItemFrame itemFrame = (ItemFrame) entity;

            // Check if the player is sneaking and using the main hand
            if (event.getPlayer().isSneaking() && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR) {
                boolean newVisibility = !itemFrame.isVisible();
                itemFrame.setVisible(newVisibility);

                // event.getPlayer().sendMessage("Item Frame is now " + (newVisibility ? "visible" : "invisible"));

                event.setCancelled(true);
            }
        }
    }
}
