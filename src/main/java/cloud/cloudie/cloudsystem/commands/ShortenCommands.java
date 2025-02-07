package cloud.cloudie.cloudsystem.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import cloud.cloudie.cloudsystem.enums.Severity;
import cloud.cloudie.cloudsystem.handler.message.MessageManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@CommandPermission("system.admin.gmswitcher")
public class ShortenCommands extends BaseCommand {

    @CommandAlias("gmc")
    public void onGmc(Player player){
        player.setGameMode(GameMode.CREATIVE);
        player.sendMessage(MessageManager.messageGenerator(Severity.WARNING, "GameMode", "Set your GameMode to creative!"));
    }

    @CommandAlias("gms")
    public void onGms(Player player){
        player.setGameMode(GameMode.SURVIVAL);
        player.sendMessage(MessageManager.messageGenerator(Severity.WARNING, "GameMode", "Set your GameMode to survival!"));
    }

    @CommandAlias("gma")
    public void onGma(Player player){
        player.setGameMode(GameMode.ADVENTURE);
        player.sendMessage(MessageManager.messageGenerator(Severity.WARNING, "GameMode", "Set your GameMode to adventure!"));
    }

    @CommandAlias("gmsp")
    public void onGmsp(Player player){
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(MessageManager.messageGenerator(Severity.WARNING, "GameMode", "Set your GameMode to spectator!"));
    }
}
