package cloud.cloudie.cloudsystem.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {
    private static final String GITHUB_API_URL = "https://api.github.com/repos/CloudieSMP/System/releases/latest";
    private final Plugin plugin;

    public UpdateChecker(Plugin plugin) {
        this.plugin = plugin;
    }

    public void checkForUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(GITHUB_API_URL).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                if (connection.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    String latestVersion = response.toString().split("\"tag_name\":\"")[1].split("\"")[0];
                    String currentVersion = plugin.getDescription().getVersion();

                    if (!latestVersion.equalsIgnoreCase(currentVersion)) {
                        Bukkit.getConsoleSender().sendMessage("§6[CloudieSMP] §eA new update is available: " + latestVersion + " (You have " + currentVersion + ")");
                    } else {
                        Bukkit.getConsoleSender().sendMessage("§a[CloudieSMP] You are running the latest version: " + currentVersion);
                    }
                }
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§c[CloudieSMP] Failed to check for updates.");
            }
        });
    }
}
