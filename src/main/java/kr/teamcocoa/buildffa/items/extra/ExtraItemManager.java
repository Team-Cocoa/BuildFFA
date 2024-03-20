package kr.teamcocoa.buildffa.items.extra;

import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Random;

public class ExtraItemManager {

    private SecureRandom random;

    private static ExtraItemManager instance;

    public static ExtraItemManager getInstance() {
        if (instance == null) {
            instance = new ExtraItemManager();
        }
        return instance;
    }

    private ExtraItemManager() {
        this.random = new SecureRandom();
    }

    private int getRandomNumber() {
        return random.nextInt(4);
    }

    public void giveExtraItem(Player player) {
        int number = getRandomNumber();
        switch (number) {
            case 0 -> {
                player.getInventory().addItem(GoldenApple.getInstance().getItemStack(player, 1));
                player.sendMessage(MessageFormat.format(LangUtils.getMessage(player, MessageEnum.GIVE_EXTRA_ITEM), GoldenApple.getInstance().getName(player)));
            }
            case 1 -> {
                player.getInventory().addItem(RescuePlatform.getInstance().getItemStack(player, 1));
                player.sendMessage(MessageFormat.format(LangUtils.getMessage(player, MessageEnum.GIVE_EXTRA_ITEM), RescuePlatform.getInstance().getName(player)));
            }
            case 2 -> {
                player.addPotionEffect(PotionEffectType.SPEED.createEffect(300, 1));
                player.sendMessage(MessageFormat.format(LangUtils.getMessage(player, MessageEnum.GIVE_EXTRA_ITEM), StringUtils.color("&cSwiftness for 15 seconds")));
            }
            case 3 -> {
                player.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(100, 0));
                player.sendMessage(MessageFormat.format(LangUtils.getMessage(player, MessageEnum.GIVE_EXTRA_ITEM), StringUtils.color("&cStrength for 3 seconds")));
            }
        }
    }
}
