package cloud.cloudie.cloudsystem.commands;
import cloud.cloudie.cloudsystem.CloudSystem;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;

@CommandAlias("system")
public class SystemCommand extends BaseCommand {

    @Default
    @Subcommand("reload")
    @CommandPermission("system.admin.reload")
    public void reload(CommandSender sender) {
        CloudSystem.plugin.reloadConfig();
        sender.sendMessage(Component.text("CloudSystem configuration reloaded successfully!")
                .color(TextColor.fromHexString("#55FF55")));
    }
}
