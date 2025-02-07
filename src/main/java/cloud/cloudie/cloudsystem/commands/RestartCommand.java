package cloud.cloudie.cloudsystem.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import cloud.cloudie.cloudsystem.CloudSystem;
import cloud.cloudie.cloudsystem.util.CalculateDates;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;

@CommandAlias("systemrestart")
public class RestartCommand extends BaseCommand {

    @Default
    @Subcommand("start")
    @CommandPermission("system.admin.restart")
    public void onRestart(Player player, String[] args){
        if(args.length < 1){
            player.sendMessage(Component.text("Please provide in how many seconds/minutes/hours... you want to restart").color(TextColor.fromHexString("#FFAA00")));
            return;
        }

        String timestamp = args[0];
        Date endDate = CalculateDates.calculateDate(timestamp);

        if (endDate != null) {
            long currentTime = java.lang.System.currentTimeMillis();
            long durationInSeconds = (endDate.getTime() - currentTime) / 1000;

            if (durationInSeconds > 0) {
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    //noinspection DuplicatedCode
                    String endPrefix = null;
                    int time = 0;
                    if(args[0].endsWith("s")){
                        endPrefix = "seconds";
                        time = (int) durationInSeconds;
                    }else if(args[0].endsWith("m")){
                        endPrefix = "minutes";
                        time = (int) (durationInSeconds / 60);
                    }

                    player1.sendMessage(Component.text("Restarting in " + time +  " " + endPrefix).color(TextColor.fromHexString("#FFAA00")));
                    player1.sendMessage(Component.text("To cancel this restart click here or write: /systemrestart cancel").color(TextColor.fromHexString("#FFAA00")).clickEvent(ClickEvent.runCommand("/systemrestart cancel")));
                }
                CountdownTask countdownTask = new CountdownTask(durationInSeconds);
                countdownTask.runTaskTimer(CloudSystem.plugin, 0, 20); // 20 ticks = 1 second
            } else {
                player.sendMessage(Component.text("Invalid timer value").color(TextColor.fromHexString("#FF0000")));
            }
        } else {
            player.sendMessage(Component.text("Invalid timer format, please only use s (seconds), m (minutes)").color(TextColor.fromHexString("#FF0000")));
        }
    }

    @Subcommand("start-forced")
    @CommandPermission("system.admin.restart.forced")
    public void onRestartForced(Player player, String[] args){
        if(args.length < 1){
            player.sendMessage(Component.text("Please provide in how many seconds/minutes/hours... you want to restart").color(TextColor.fromHexString("#FFAA00")));
            return;
        }

        String timestamp = args[0];
        Date endDate = CalculateDates.calculateDate(timestamp);

        if (endDate != null) {
            long currentTime = java.lang.System.currentTimeMillis();
            long durationInSeconds = (endDate.getTime() - currentTime) / 1000;

            if (durationInSeconds > 0) {
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    //noinspection DuplicatedCode
                    String endPrefix = null;
                    int time = 0;
                    if(args[0].endsWith("s")){
                        endPrefix = "seconds";
                        time = (int) durationInSeconds;
                    }else if(args[0].endsWith("m")){
                        endPrefix = "minutes";
                        time = (int) (durationInSeconds / 60);
                    }

                    player1.sendMessage(Component.text("Restarting in " + time +  " " + endPrefix).color(TextColor.fromHexString("#FFAA00")));
                    player1.sendMessage(Component.text("This restart can not be canceled.").color(TextColor.fromHexString("#FFAA00")));
                }
                CountdownTaskForced countdownTaskForced = new CountdownTaskForced(durationInSeconds);
                countdownTaskForced.runTaskTimer(CloudSystem.plugin, 0, 20); // 20 ticks = 1 second
            } else {
                player.sendMessage(Component.text("Invalid timer value").color(TextColor.fromHexString("#FF0000")));
            }
        } else {
            player.sendMessage(Component.text("Invalid timer format, please only use s (seconds), m (minutes)").color(TextColor.fromHexString("#FF0000")));
        }
    }

    @Subcommand("cancel")
    public void onCancel(Player player){
        if (CountdownTask.isCountdownActive()) {
            // Cancel the countdown task
            CountdownTask.cancelCountdown();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(Component.text("Restart has been canceled by: " + player.getName()).color(TextColor.fromHexString("#FF0000")));
            }
        } else {
            player.sendMessage(Component.text("No restart countdown is currently active, or can not be cancelled.").color(TextColor.fromHexString("#FF0000")));
        }
    }
    private static class CountdownTask extends BukkitRunnable {
        private long remainingTime;
        private static CountdownTask activeTask;
        public CountdownTask(long durationInSeconds) {
            this.remainingTime = durationInSeconds;
            activeTask = this;
        }
        public static boolean isCountdownActive() {
            return activeTask != null && activeTask.remainingTime > 0;
        }

        public static void cancelCountdown() {
            if (activeTask != null) {
                activeTask.cancel();
                activeTask = null;
            }
        }

        @Override
        public void run() {
            if (remainingTime <= 10 && remainingTime >= 1) {
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    player1.sendMessage(Component.text("Restarting in " + remainingTime + " seconds").color(TextColor.fromHexString("#FFAA00")));
                }
            }else if(remainingTime == 60){
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    player1.sendMessage(Component.text("Restarting in 1 minute").color(TextColor.fromHexString("#FFAA00")));
                    player1.sendMessage(Component.text("To cancel this restart click here or write: /systemrestart cancel").color(TextColor.fromHexString("#FFAA00")).clickEvent(ClickEvent.runCommand("/systemrestart cancel")));
                }
            }else if(remainingTime == 300){
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    player1.sendMessage(Component.text("Restarting in 5 minutes").color(TextColor.fromHexString("#FFAA00")));
                    player1.sendMessage(Component.text("To cancel this restart click here or write: /systemrestart cancel").color(TextColor.fromHexString("#FFAA00")).clickEvent(ClickEvent.runCommand("/systemrestart cancel")));
                }
            }else if(remainingTime == 900){
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    player1.sendMessage(Component.text("Restarting in 15 minutes").color(TextColor.fromHexString("#FFAA00")));
                    player1.sendMessage(Component.text("To cancel this restart click here or write: /systemrestart cancel").color(TextColor.fromHexString("#FFAA00")).clickEvent(ClickEvent.runCommand("/systemrestart cancel")));
                }
            } else if(remainingTime == 1800){
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    player1.sendMessage(Component.text("Restarting in 30 minutes").color(TextColor.fromHexString("#FFAA00")));
                    player1.sendMessage(Component.text("To cancel this restart click here or write: /systemrestart cancel").color(TextColor.fromHexString("#FFAA00")).clickEvent(ClickEvent.runCommand("/systemrestart cancel")));
                }
            } else if(remainingTime == 3600){
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    player1.sendMessage(Component.text("Restarting in 1 hour").color(TextColor.fromHexString("#FFAA00")));
                    player1.sendMessage(Component.text("To cancel this restart click here or write: /systemrestart cancel").color(TextColor.fromHexString("#FFAA00")).clickEvent(ClickEvent.runCommand("/systemrestart cancel")));
                }
            }
            else if(remainingTime == 4140){
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    player1.sendMessage(Component.text("Restarting in 69 minutes").color(TextColor.fromHexString("#FFAA00")));
                    player1.sendMessage(Component.text("To cancel this restart click here or write: /systemrestart cancel").color(TextColor.fromHexString("#FFAA00")).clickEvent(ClickEvent.runCommand("/systemrestart cancel")));
                }
            } else if(remainingTime == 0) {
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    player1.sendMessage(Component.text("Restarting now!").color(TextColor.fromHexString("#FFAA00")));
                }
                // Restart the server
                Bukkit.shutdown();
                this.cancel();
                activeTask = null;
            }
            remainingTime--;
        }
    }

    private static class CountdownTaskForced extends BukkitRunnable {
        private long remainingTime;
        public CountdownTaskForced(long durationInSeconds) {
            this.remainingTime = durationInSeconds;
        }

        @Override
        public void run() {
            if (remainingTime <= 10 && remainingTime >= 1) {
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    player1.sendMessage(Component.text("Restarting in " + remainingTime + " seconds").color(TextColor.fromHexString("#FFAA00")));
                }
            }else if(remainingTime == 30){
                for(Player player1 : Bukkit.getOnlinePlayers()) {
                    player1.sendMessage(Component.text("Restarting in 30 seconds").color(TextColor.fromHexString("#FFAA00")));
                    player1.sendMessage(Component.text("This restart can not be canceled.").color(TextColor.fromHexString("#FFAA00")));
                }
            }else if(remainingTime == 60){
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    player1.sendMessage(Component.text("Restarting in 1 minute").color(TextColor.fromHexString("#FFAA00")));
                    player1.sendMessage(Component.text("This restart can not be canceled.").color(TextColor.fromHexString("#FFAA00")));
                }
            }else if(remainingTime == 300) {
                for (Player player1 : Bukkit.getOnlinePlayers()) {
                    player1.sendMessage(Component.text("Restarting in 5 minutes").color(TextColor.fromHexString("#FFAA00")));
                    player1.sendMessage(Component.text("This restart can not be canceled.").color(TextColor.fromHexString("#FFAA00")));
                }
            }else if(remainingTime == 900){
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    player1.sendMessage(Component.text("Restarting in 15 minutes").color(TextColor.fromHexString("#FFAA00")));
                    player1.sendMessage(Component.text("This restart can not be canceled.").color(TextColor.fromHexString("#FFAA00")));
                }
            } else if(remainingTime == 1800){
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    player1.sendMessage(Component.text("Restarting in 30 minutes").color(TextColor.fromHexString("#FFAA00")));
                    player1.sendMessage(Component.text("This restart can not be canceled.").color(TextColor.fromHexString("#FFAA00")));
                }
            } else if(remainingTime == 3600){
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    player1.sendMessage(Component.text("Restarting in 1 hour").color(TextColor.fromHexString("#FFAA00")));
                    player1.sendMessage(Component.text("This restart can not be canceled.").color(TextColor.fromHexString("#FFAA00")));
                }
            }
            else if(remainingTime == 4140){
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    player1.sendMessage(Component.text("Restarting in 69 minutes").color(TextColor.fromHexString("#FFAA00")));
                    player1.sendMessage(Component.text("This restart can not be canceled.").color(TextColor.fromHexString("#FFAA00")));
                }
            } else if(remainingTime == 0) {
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    player1.sendMessage(Component.text("Restarting now!").color(TextColor.fromHexString("#FFAA00")));
                }
                // Restart the server
                Bukkit.shutdown();
                this.cancel();
            }
            remainingTime--;
        }
    }
}
