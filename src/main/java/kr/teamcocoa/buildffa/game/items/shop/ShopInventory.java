package kr.teamcocoa.buildffa.game.items.shop;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.translation.InventoryEnum;
import kr.teamcocoa.buildffa.translation.LangUtils;
import kr.teamcocoa.core.bukkit.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Singleton
public class ShopInventory {

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  private final Bow bow;

  private final SnowBall snowBall;

  @Inject
  public ShopInventory(BuildFFAPlayerManager buildFFAPlayerManager, Bow bow, SnowBall snowBall) {
    this.buildFFAPlayerManager = buildFFAPlayerManager;
    this.bow = bow;
    this.snowBall = snowBall;
  }

  public void onClickShopInventory(InventoryClickEvent e) {
    Player player = (Player) e.getWhoClicked();

    if (e.getCurrentItem() == null) {
      return;
    }

    ItemStack clickedItem = e.getCurrentItem();

    if (!clickedItem.hasItemMeta()) {
      return;
    }

    e.setCancelled(true);

    // TODO(economy): Re-enable buying once the coin system is implemented.
    // BuyStatus status = null;
    //
    // ItemMeta clickedItemMeta = clickedItem.getItemMeta();
    //
    // BuildFFAPlayer buildFFAPlayer = buildFFAPlayerManager.getPlayer(player);
    //
    // if (clickedItemMeta.getDisplayName().equalsIgnoreCase(snowBall.getName(player))) {
    //   status = snowBall.purchase(buildFFAPlayer);
    // }
    // if (clickedItemMeta.getDisplayName().equalsIgnoreCase(bow.getName(player))) {
    //   status = bow.purchase(buildFFAPlayer);
    // }
    //
    // if (status != null) {
    //   switch (status) {
    //     case SUCCESS:
    //       {
    //         player.sendMessage(LangUtils.getMessage(player, MessageEnum.SHOP_BOUGHT));
    //         break;
    //       }
    //     case FAILED:
    //       {
    //         player.sendMessage(LangUtils.getMessage(player, MessageEnum.SHOP_CANNOT_BUY));
    //         break;
    //       }
    //     case ALREADY_BOUGHT:
    //       {
    //         player.sendMessage(LangUtils.getMessage(player, MessageEnum.SHOP_ALREADY_BOUGHT));
    //         break;
    //       }
    //   }
    // }
    //
  }

  public void openShopInventory(Player player) {
    Inventory inventory =
        Bukkit.createInventory(null, 1 * 9, LangUtils.getMessage(player, InventoryEnum.EXTRA_ITEM));
    for (int i = 0; i < 9; i++) {
      inventory.setItem(i, ItemUtils.getGUIBackGround());
    }
    inventory.setItem(2, snowBall.getVoteItemStack(player));
    inventory.setItem(6, bow.getVoteItemStack(player));
    player.openInventory(inventory);
  }
}
