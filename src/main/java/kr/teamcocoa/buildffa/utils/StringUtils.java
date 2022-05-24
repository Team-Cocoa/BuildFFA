package kr.teamcocoa.buildffa.utils;

import org.bukkit.ChatColor;

public class StringUtils {
    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String getListByString(String string) {
        string = string.replace("[", "").replace("]", "").trim();
        StringBuilder sb = new StringBuilder();
        for(String s : string.split(",")) {
            sb.append(StringUtils.color(s) + "\n");
        }
        return sb.toString();
    }
}
