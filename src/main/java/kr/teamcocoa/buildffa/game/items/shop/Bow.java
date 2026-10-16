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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Singleton
public class Bow extends AbstractShopItem {

  @Inject
  public Bow(CurrencyService currencyService) {
    super(currencyService);
    super.price = 1000;
  }

  @Override
  public ItemStack getItemStack(Player player, int count) {
    ItemStack itemStack = new ItemStack(Material.BOW, count);
    itemStack.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
    return itemStack;
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
    return StringUtils.color("&cBow");
  }

  // TODO(economy): Re-enable buying once the coin system is implemented.
  public BuyStatus purchase(BuildFFAPlayer player) {
    // if (player.isBowBought()) {
    //   return BuyStatus.ALREADY_BOUGHT;
    // }
    // if (super.buyItem(player)) {
    //   player.setBowBought(true);
    //   return BuyStatus.SUCCESS;
    // } else {
    //   return BuyStatus.FAILED;
    // }
    return BuyStatus.FAILED;
  }
}
