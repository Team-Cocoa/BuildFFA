package kr.teamcocoa.buildffa.game.items.shop;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.text.MessageFormat;
import kr.teamcocoa.buildffa.game.economy.CurrencyService;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.translation.LangUtils;
import kr.teamcocoa.buildffa.translation.OtherEnum;
import kr.teamcocoa.core.bukkit.utils.ItemUtils;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Singleton
public class SnowBall extends AbstractShopItem {

  @Inject
  public SnowBall(CurrencyService currencyService) {
    super(currencyService);
    super.price = 200;
  }

  @Override
  public ItemStack getItemStack(Player player, int count) {
    return new ItemStack(Material.SNOW_BALL, count);
  }

  @Override
  public ItemStack getVoteItemStack(Player player) {
    ItemStack itemStack = getItemStack(player, 1);
    ItemUtils.name(itemStack, getName(player));
    ItemUtils.lore(
        itemStack,
        getLore(MessageFormat.format(LangUtils.getMessage(player, OtherEnum.LORE_PRICE), price)));
    return itemStack;
  }

  @Override
  public String getName(Player player) {
    return StringUtils.color("&cSnowBall");
  }

  // TODO(economy): Re-enable buying once the coin system is implemented.
  public BuyStatus purchase(BuildFFAPlayer player) {
    // if (player.isSnowBallBought()) {
    //   return BuyStatus.ALREADY_BOUGHT;
    // }
    // if (super.buyItem(player)) {
    //   player.setSnowBallBought(true);
    //   return BuyStatus.SUCCESS;
    // } else {
    //   return BuyStatus.FAILED;
    // }
    return BuyStatus.FAILED;
  }
}
