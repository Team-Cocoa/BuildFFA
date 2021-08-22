package kr.teamcocoa.buildffa.utils;

import org.bukkit.ChatColor;

public class StringUtils {
    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
