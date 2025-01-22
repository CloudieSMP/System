package moe.sebiann.system.util;

import moe.sebiann.system.System;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AutoRestart {
    private static BukkitTask schedulerTask;

    public static void onAutoRestart() {
        Plugin plugin = System.plugin;
        String restartTime = plugin.getConfig().getString("autorestart");

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date restartDate;
        Date currentDate = new Date();
        final int[] timeDifference = new int[1]; // Use an array to store the mutable value

        try {
            restartDate = dateFormat.parse(restartTime);

            // Create a Calendar instance to handle date and time
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(restartDate);

            // Set the restart time with today's date
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(currentDate);
            calendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, currentCalendar.get(Calendar.DAY_OF_MONTH));

            // If the restart time has already passed for today, add one day
            if (calendar.before(currentCalendar)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            // Get the adjusted restartDate
            restartDate = calendar.getTime();

            long diffInMilliSeconds = restartDate.getTime() - currentDate.getTime();
            timeDifference[0] = (int) TimeUnit.MILLISECONDS.toSeconds(diffInMilliSeconds);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        plugin.getLogger().info("|   Server restarting at: " + restartDate + "   |"); //  + " (or in: " + timeDifference[0] + " seconds)"
        plugin.getLogger().info("|                                                        |");

        schedulerTask = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {

                if(timeDifference[0] == 1800){
                    plugin.getServer().broadcast(Component.text("Server is restarting in 30 minutes!").color(TextColor.fromHexString("#FFAA00")));
                }
                if(timeDifference[0] == 600){
                    plugin.getServer().broadcast(Component.text("Server is restarting in 10 minutes!").color(TextColor.fromHexString("#FFAA00")));
                }
                if(timeDifference[0] == 300){
                    plugin.getServer().broadcast(Component.text("Server is restarting in 5 minutes!").color(TextColor.fromHexString("#FFAA00")));
                }
                if(timeDifference[0] == 60){
                    plugin.getServer().broadcast(Component.text("Server is restarting in 1 minute!").color(TextColor.fromHexString("#FFAA00")));
                }
                if(timeDifference[0] <= 10 && timeDifference[0] >= 1){
                    plugin.getServer().broadcast(Component.text("Server is restarting in " + timeDifference[0] + " seconds!").color(TextColor.fromHexString("#FFAA00")));
                }
                if(timeDifference[0] == 0){
                    plugin.getServer().broadcast(Component.text("Server is restarting now!").color(TextColor.fromHexString("#FFAA00")));
                    Bukkit.shutdown();
                    schedulerTask.cancel();
                }
                timeDifference[0]--;
            }
        }, 0, 20); // Run the task every second (20 ticks)
    }
    public static void stopAutoRestart() {
        if (schedulerTask != null) {
            schedulerTask.cancel();
        }
    }
}

