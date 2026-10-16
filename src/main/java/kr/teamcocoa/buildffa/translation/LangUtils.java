package kr.teamcocoa.buildffa.translation;

import kr.teamcocoa.language.enums.TypeEnum;
import kr.teamcocoa.language.languages.LanguageController;
import org.bukkit.entity.Player;

public class LangUtils {
  public static String getMessage(Player player, Translatable node) {
    return LanguageController.getMessage(player.getUniqueId(), TypeEnum.BUILDFFA, node.getName());
  }
}
