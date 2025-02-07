package cloud.cloudie.cloudsystem.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onItemFrameInteract(PlayerInteractEntityEvent event) {
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

    @EventHandler
    public void onDoorInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) {
            return;
        }
        if (event.getPlayer().isSneaking()) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();

        if (!isDoor(clickedBlock.getType())) {
            return;
        }

        BlockData data = clickedBlock.getBlockData();
        if (!(data instanceof Door)) {
            return;
        }

        Door clickedDoor = (Door) data;

        boolean newOpenState = !clickedDoor.isOpen();
        clickedDoor.setOpen(newOpenState);
        clickedBlock.setBlockData(clickedDoor, true);

        Block otherDoorBlock = findAdjacentDoor(clickedBlock);
        if (otherDoorBlock != null) {
            Door otherDoor = (Door) otherDoorBlock.getBlockData();
            otherDoor.setOpen(newOpenState);
            otherDoorBlock.setBlockData(otherDoor, true);
        }

        event.setCancelled(true);
    }

    private boolean isDoor(Material material) {
        return material == Material.OAK_DOOR || material == Material.IRON_DOOR || material == Material.SPRUCE_DOOR
                || material == Material.BIRCH_DOOR || material == Material.JUNGLE_DOOR || material == Material.CHERRY_DOOR
                || material == Material.DARK_OAK_DOOR || material == Material.PALE_OAK_DOOR || material == Material.ACACIA_DOOR
                || material == Material.CRIMSON_DOOR || material == Material.WARPED_DOOR || material == Material.MANGROVE_DOOR
                || material == Material.BAMBOO_DOOR || material == Material.COPPER_DOOR || material == Material.EXPOSED_COPPER_DOOR
                || material == Material.WEATHERED_COPPER_DOOR || material == Material.OXIDIZED_COPPER_DOOR;
    }


    private Block findAdjacentDoor(Block doorBlock) {
        // Check all adjacent blocks for another door
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (Math.abs(dx) == Math.abs(dz)) {
                    continue; // Skip diagonals and the same block
                }

                Block adjacentBlock = doorBlock.getRelative(dx, 0, dz);
                if (isDoor(adjacentBlock.getType())) {
                    return adjacentBlock;
                }
            }
        }
        return null;
    }
}
