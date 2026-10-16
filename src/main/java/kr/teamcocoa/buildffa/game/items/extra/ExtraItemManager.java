package kr.teamcocoa.buildffa.game.items.extra;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.security.SecureRandom;
import java.text.MessageFormat;
import kr.teamcocoa.buildffa.translation.LangUtils;
import kr.teamcocoa.buildffa.translation.MessageEnum;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

@Singleton
public class ExtraItemManager {

  private final SecureRandom random = new SecureRandom();

  private final GoldenApple goldenApple;

  private final RescuePlatform rescuePlatform;

  @Inject
  public ExtraItemManager(GoldenApple goldenApple, RescuePlatform rescuePlatform) {
    this.goldenApple = goldenApple;
    this.rescuePlatform = rescuePlatform;
  }

  private int getRandomNumber() {
    return random.nextInt(4);
  }

  public void giveExtraItem(Player player) {
    int number = getRandomNumber();
    switch (number) {
      case 0:
        {
          player.getInventory().addItem(goldenApple.getItemStack(player, 1));
          player.sendMessage(
              MessageFormat.format(
                  LangUtils.getMessage(player, MessageEnum.GIVE_EXTRA_ITEM),
                  goldenApple.getName(player)));
          break;
        }
      case 1:
        {
          player.getInventory().addItem(rescuePlatform.getItemStack(player, 1));
          player.sendMessage(
              MessageFormat.format(
                  LangUtils.getMessage(player, MessageEnum.GIVE_EXTRA_ITEM),
                  rescuePlatform.getName(player)));
          break;
        }
      case 2:
        {
          player.addPotionEffect(PotionEffectType.SPEED.createEffect(300, 1));
          player.sendMessage(
              MessageFormat.format(
                  LangUtils.getMessage(player, MessageEnum.GIVE_EXTRA_ITEM),
                  StringUtils.color("&cSwiftness for 15 seconds")));
          break;
        }
      case 3:
        {
          player.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(100, 0));
          player.sendMessage(
              MessageFormat.format(
                  LangUtils.getMessage(player, MessageEnum.GIVE_EXTRA_ITEM),
                  StringUtils.color("&cStrength for 3 seconds")));
          break;
        }
    }
  }
}
