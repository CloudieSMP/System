package cloud.cloudie.cloudsystem.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("fly")
@CommandPermission("system.admin.fly")
public class FlyCommand extends BaseCommand {

    @Default
    @CommandCompletion("@Players")
    public void onFlyCommand(CommandSender sender, @Optional String targetName) {
        if (targetName == null) {
            // Handle self toggle
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("Only players can toggle their own flight mode.")
                        .color(TextColor.fromHexString("#FF5555")));
                return;
            }

            toggleFly(player);
            return;
        }

        // Handle flight toggle for a target player
        handleFlyToggleOthers(sender, targetName);
    }

    @CommandPermission("system.admin.fly.others") //Mainly here so it autocompletes in MC
    private void handleFlyToggleOthers(CommandSender sender, String targetName) {
        // Check if the sender has permission
        if (!sender.hasPermission("system.admin.fly.others")) {
            sender.sendMessage(Component.text("You don't have permission to toggle fly for others.")
                    .color(TextColor.fromHexString("#FF5555")));
            return;
        }

        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            sender.sendMessage(Component.text("Player not found.")
                    .color(TextColor.fromHexString("#FF5555")));
            return;
        }

        toggleFly(target);
        sender.sendMessage(Component.text("Fly mode toggled for " + target.getName() + ".")
                .color(TextColor.fromHexString("#55FF55")));
    }

    private void toggleFly(Player player) {
        boolean newFlyState = !player.getAllowFlight();
        player.setAllowFlight(newFlyState);
        player.setFlying(newFlyState);

        player.sendMessage(Component.text("Fly mode " + (newFlyState ? "enabled" : "disabled") + ".")
                .color(newFlyState ? TextColor.fromHexString("#55FF55") : TextColor.fromHexString("#FF5555")));
    }
}
