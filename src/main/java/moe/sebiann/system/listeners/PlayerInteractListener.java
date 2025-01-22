package moe.sebiann.system.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!event.getHand().equals(org.bukkit.inventory.EquipmentSlot.HAND)) {
            return;
        }
        Entity entity = event.getRightClicked();
        if (entity instanceof ItemFrame) {
            ItemFrame itemFrame = (ItemFrame) entity;

            if (event.getPlayer().isSneaking() && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR) {
                boolean newVisibility = !itemFrame.isVisible();
                itemFrame.setVisible(newVisibility);

                // event.getPlayer().sendMessage("Item Frame is now " + (newVisibility ? "visible" : "invisible"));

                event.setCancelled(true);
            }
        }
    }
}
