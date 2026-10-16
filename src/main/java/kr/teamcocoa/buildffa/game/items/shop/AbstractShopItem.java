package kr.teamcocoa.buildffa.game.items.shop;

import java.util.LinkedList;
import java.util.List;
import kr.teamcocoa.buildffa.game.economy.CurrencyService;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractShopItem {
  protected final CurrencyService currencyService;

  protected int price;

  protected AbstractShopItem(CurrencyService currencyService) {
    this.currencyService = currencyService;
  }

  public int getPrice() {
    return price;
  }

  protected List<String> getLore(String... strings) {
    List<String> list = new LinkedList<>();
    for (String string : strings) {
      list.add(StringUtils.color(string));
    }
    return list;
  }

  public abstract ItemStack getItemStack(Player player, int count);

  public abstract ItemStack getVoteItemStack(Player player);

  public abstract String getName(Player player);

  // TODO(economy): CurrencyService is a stub, so this always fails until the coin system exists.
  public boolean buyItem(BuildFFAPlayer player) {
    return currencyService.withdraw(player.getPlayer(), price);
  }
}
