package uk.co.maboughey.mcnsanotes.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages {
    public static void sendMessage(CommandSender src, String s) {
        src.sendMessage(Colour.colour(s));
    }
    public static void sendToMods(String message) {
        for (Player player: Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("mcnsanotes.mod")) {
                player.sendMessage(Colour.colour(message));
            }
        }
    }
}
