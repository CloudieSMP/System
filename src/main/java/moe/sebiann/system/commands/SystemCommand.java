package moe.sebiann.system.commands;
import moe.sebiann.system.System;

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
        System.plugin.reloadConfig();
        sender.sendMessage(Component.text("System configuration reloaded successfully!")
                .color(TextColor.fromHexString("#55FF55")));
    }
}
