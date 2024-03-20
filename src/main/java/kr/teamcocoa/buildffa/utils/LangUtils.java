package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.enums.*;
import kr.teamcocoa.language.enums.TypeEnum;
import kr.teamcocoa.language.languages.LanguageController;
import org.bukkit.entity.Player;

public class LangUtils {
    public static String getMessage(Player player, String node) {
        return LanguageController.getMessage(player.getUniqueId(), TypeEnum.BUILDFFA, node);
    }

    public static String getMessage(Player player, OtherEnum node) {
        return LanguageController.getMessage(player.getUniqueId(), TypeEnum.BUILDFFA, node.getName());
    }

    public static String getMessage(Player player, MessageEnum node) {
        return LanguageController.getMessage(player.getUniqueId(), TypeEnum.BUILDFFA, node.getName());
    }

    public static String getMessage(Player player, InventoryEnum node) {
        return LanguageController.getMessage(player.getUniqueId(), TypeEnum.BUILDFFA, node.getName());
    }

    public static String getMessage(Player player, ItemEnum node) {
        return LanguageController.getMessage(player.getUniqueId(), TypeEnum.BUILDFFA, node.getName());
    }
}
