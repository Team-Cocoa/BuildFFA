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

    public static String getMessage(Player player, Translatable translatable) {
        return LanguageManager.getMessage(player, TypeEnum.BUILDFFA, translatable.getName());
    }

}
