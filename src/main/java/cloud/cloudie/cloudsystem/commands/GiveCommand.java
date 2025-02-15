package cloud.cloudie.cloudsystem.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("i")
@CommandPermission("system.admin.give")
public class GiveCommand extends BaseCommand {

    @Default
    @CommandCompletion("@Items @range:1-64 @Players")
    public void onGiveCommand(CommandSender sender, String targetItem, @Optional Integer amount, @Optional String targetName) {
        // Ensure sender is a player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can give items.")
                    .color(TextColor.fromHexString("#FF5555")));
            return;
        }

        // Set amount (default to 1 if not provided)
        int itemAmount = (amount != null) ? Math.max(1, Math.min(amount, 64)) : 1; // Ensure it's between 1-64

        // Determine the target player
        Player target = (targetName != null) ? Bukkit.getPlayerExact(targetName) : player;
        if (target == null) {
            sender.sendMessage(Component.text("Player not found.")
                    .color(TextColor.fromHexString("#FF5555")));
            return;
        }

        // Check if item exists
        Material material = Material.matchMaterial(targetItem);
        if (material == null) {
            sender.sendMessage(Component.text("Invalid item name: " + targetItem)
                    .color(TextColor.fromHexString("#FF5555")));
            return;
        }

        // Give the item
        ItemStack item = new ItemStack(material, itemAmount);
        target.getInventory().addItem(item);

        sender.sendMessage(Component.text("Gave " + itemAmount + "x " + material.name() + " to " + target.getName() + ".")
                .color(TextColor.fromHexString("#55FF55")));    }
}
