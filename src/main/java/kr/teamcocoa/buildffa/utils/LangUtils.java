package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.translate.*;
import kr.teamcocoa.language.enums.TypeEnum;
import kr.teamcocoa.language.languages.LanguageManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LangUtils {

    public static String getMessage(Player player, String node) {
        return StringUtils.color(LanguageManager.getMessage(player, TypeEnum.BUILDFFA, node));
    }

    public static String getMessage(Player player, OtherNode node) {
        return StringUtils.color(LanguageManager.getMessage(player, TypeEnum.BUILDFFA, node.getName()));
    }

    public static String getMessage(Player player, MessageNode node) {
        return StringUtils.color(LanguageManager.getMessage(player, TypeEnum.BUILDFFA, node.getName()));
    }

    public static String getMessage(Player player, InventortNode node) {
        return StringUtils.color(LanguageManager.getMessage(player, TypeEnum.BUILDFFA, node.getName()));
    }

    public static String getMessage(Player player, ItemNode node) {
        return StringUtils.color(LanguageManager.getMessage(player, TypeEnum.BUILDFFA, node.getName()));
    }
}
