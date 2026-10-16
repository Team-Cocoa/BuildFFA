package kr.teamcocoa.buildffa.listener;

import com.google.inject.Inject;
import kr.teamcocoa.buildffa.game.items.shop.ShopInventory;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.translation.InventoryEnum;
import kr.teamcocoa.buildffa.translation.LangUtils;
import kr.teamcocoa.buildffa.world.MapVoteInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryClickListener implements Listener {

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  private final ShopInventory shopInventory;

  private final MapVoteInventory mapVoteInventory;

  @Inject
  public InventoryClickListener(
      BuildFFAPlayerManager buildFFAPlayerManager,
      ShopInventory shopInventory,
      MapVoteInventory mapVoteInventory) {
    this.buildFFAPlayerManager = buildFFAPlayerManager;
    this.shopInventory = shopInventory;
    this.mapVoteInventory = mapVoteInventory;
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent e) {
    if (e.getClickedInventory() == null) {
      return;
    }

    if (e.getClickedInventory().getType() == InventoryType.CRAFTING) {
      e.setCancelled(true);
      return;
    }

    Player player = (Player) e.getWhoClicked();
    BuildFFAPlayer buildFFAPlayer = buildFFAPlayerManager.getPlayer(player);

    if (buildFFAPlayer.isBuild()) {
      return;
    }

    String title = e.getView().getTitle();

    if (title.equals(LangUtils.getMessage(player, InventoryEnum.VOTE))) {
      e.setCancelled(true);
      mapVoteInventory.onClickInventory(e);
    }

    if (title.equals(LangUtils.getMessage(player, InventoryEnum.EXTRA_ITEM))) {
      e.setCancelled(true);
      // TODO(economy): Re-enable buying once the coin system is implemented.
      // shopInventory.onClickShopInventory(e);
    }
  }
}
