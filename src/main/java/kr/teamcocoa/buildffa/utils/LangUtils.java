package kr.teamcocoa.buildffa.utils;

import es.minetsii.languages.objects.Language;
import es.minetsii.languages.utils.SendManager;
import kr.teamcocoa.buildffa.enums.InventoryEnum;
import kr.teamcocoa.buildffa.enums.ItemEnum;
import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.enums.OtherEnum;
import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.entity.Player;

public class LangUtils {
    public static String getMessage(Player player, String node) {
        return StringUtils.color(SendManager.getMessage(node, player, Main.inst()));
    }

    public static String getMessage(Player player, OtherEnum node) {
        return StringUtils.color(SendManager.getMessage(node.getName(), player, Main.inst()));
    }

    public static String getMessage(Player player, MessageEnum node) {
        return StringUtils.color(SendManager.getMessage(node.getName(), player, Main.inst()));
    }

    public static String getMessage(Player player, InventoryEnum node) {
        return StringUtils.color(SendManager.getMessage(node.getName(), player, Main.inst()));
    }

    public static String getMessage(Player player, ItemEnum node) {
        return StringUtils.color(SendManager.getMessage(node.getName(), player, Main.inst()));
    }
}
