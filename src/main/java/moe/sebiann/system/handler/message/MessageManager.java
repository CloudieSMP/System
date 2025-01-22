package moe.sebiann.system.handler.message;

import moe.sebiann.system.System;
import moe.sebiann.system.enums.Severity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.plugin.Plugin;

public class MessageManager {

    private static final Plugin plugin = System.plugin;
    private static final String servername = "[Cloudie SMP - ";

    public static Component messageGenerator(Severity severity, String type, String message){
        if(Severity.severityCheck(severity)){
            return Component.text(servername + type + "]\n").color(TextColor.fromHexString(severity.getHexColor()))
                    .append(Component.text(message).color(TextColor.fromHexString(severity.getHexColor())));
        }else{
            plugin.getLogger().warning("Severity not found, canceling script!");
            return Component.text("404, Severity not found. Please report this bug.");
        }
    }
    public static Component messageGenerator(String hexCode, String type, String message){
        return Component.text(servername + type + "]\n").color(TextColor.fromHexString(hexCode))
                .append(Component.text(message).color(TextColor.fromHexString(hexCode)));
    }
    public static Component messageGenerator(Severity severity, String type, Component component){
        Plugin plugin = System.plugin;

        if(Severity.severityCheck(severity)){
            return Component.text(servername + type + "]\n").color(TextColor.fromHexString(severity.getHexColor()))
                    .append(component.color(TextColor.fromHexString(severity.getHexColor())));
        }else{
            plugin.getLogger().warning("Severity not found, canceling script!");
            return Component.text("404, Severity not found. Please report this bug.");
        }
    }
    public static Component messageGenerator(String hexCode, String type, Component component){
        return Component.text(servername + type + "]\n").color(TextColor.fromHexString(hexCode))
                .append(component.color(TextColor.fromHexString(hexCode)));
    }
}