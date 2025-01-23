package moe.sebiann.system.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class RulesGUICommand extends BaseCommand implements Listener {

    private static final String GUI_TITLE = "Cloudie SMP Rules";

    @CommandAlias("rules")
    public void onRules(Player player) {
        Inventory gui = Bukkit.createInventory(null, 45, GUI_TITLE);

        ItemStack rule1 = new ItemStack(Material.BOOK);
        ItemMeta meta1 = rule1.getItemMeta();
        meta1.setDisplayName("§9No Hacking");
        meta1.setLore(List.of("§7Includes X-raying,", "§7seed breakers, and exploits."));
        rule1.setItemMeta(meta1);
        gui.setItem(11, rule1);

        ItemStack commandsmarriage = new ItemStack(Material.COMPASS);
        ItemMeta meta4 = commandsmarriage.getItemMeta();
        meta4.setDisplayName("§9Marriages");
        meta4.setLore(List.of(
                "§7/marry propose",
                "§7/marry adopt"
        ));
        commandsmarriage.setItemMeta(meta4);
        gui.setItem(13, commandsmarriage);

        ItemStack rule2 = new ItemStack(Material.BOOK);
        ItemMeta meta2 = rule2.getItemMeta();
        meta2.setDisplayName("§9No Trolling");
        meta2.setLore(List.of("§7No griefing,", "§7stealing, or inappropriate builds."));
        rule2.setItemMeta(meta2);
        gui.setItem(29, rule2);

        ItemStack commands = new ItemStack(Material.COMPASS);
        ItemMeta meta3 = commands.getItemMeta();
        meta3.setDisplayName("§9SystemHomes");
        meta3.setLore(List.of(
                "§7/tpa - Teleport to friends",
                "§7/sethome - Set your home",
                "§7/setpwarp - Set player warp"
        ));
        commands.setItemMeta(meta3);
        gui.setItem(15, commands);

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(GUI_TITLE)) {
            event.setCancelled(true);
        }
    }
}
