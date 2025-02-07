package cloud.cloudie.cloudsystem.commands;
import cloud.cloudie.cloudsystem.CloudSystem;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("reskin")
public class ReskinCommand extends BaseCommand {

    @Default
    @CommandCompletion("@skins")
    public void reskin(CommandSender sender, String option) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.")
                    .color(TextColor.fromHexString("#FF5555")));
            return;
        }
        List<String> validOptions = new ArrayList<>(CloudSystem.plugin.getConfig().getStringList("skins"));
        // Validate the option
        if (option == null || !validOptions.contains(option)) {
            player.sendMessage(Component.text("Invalid or missing option. Valid options: " + String.join(", ", validOptions))
                    .color(TextColor.fromHexString("#FF5555")));
            return;
        }
        // Perform the reskin command logic
        String command = String.format(
                "item modify entity %s armor.head {function:\"minecraft:set_components\", components: {item_model:\"cookie:%s\", equippable:{slot: \"head\"}}}",
                player.getName(),
                option
        );
        try {
            // Execute the Minecraft command
            player.getServer().dispatchCommand(player.getServer().getConsoleSender(), command);
            player.sendMessage(Component.text("Successfully applied reskin: " + option)
                    .color(TextColor.fromHexString("#55FF55")));
        } catch (Exception e) {
            player.sendMessage(Component.text("Error applying reskin: " + e.getMessage())
                    .color(TextColor.fromHexString("#FF5555")));
        }
    }

}