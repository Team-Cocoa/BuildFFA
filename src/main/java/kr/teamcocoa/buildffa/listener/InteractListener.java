package kr.teamcocoa.buildffa.listener;

import com.google.inject.Inject;
import java.util.concurrent.TimeUnit;
import kr.teamcocoa.buildffa.game.items.extra.RescuePlatform;
import kr.teamcocoa.buildffa.game.items.shop.ShopInventory;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.translation.InventoryEnum;
import kr.teamcocoa.buildffa.translation.ItemEnum;
import kr.teamcocoa.buildffa.translation.LangUtils;
import kr.teamcocoa.buildffa.world.MapVoteInventory;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  private final ShopInventory shopInventory;

  private final MapVoteInventory mapVoteInventory;

  private final RescuePlatform rescuePlatform;

  @Inject
  public InteractListener(
      BuildFFAPlayerManager buildFFAPlayerManager,
      ShopInventory shopInventory,
      MapVoteInventory mapVoteInventory,
      RescuePlatform rescuePlatform) {
    this.buildFFAPlayerManager = buildFFAPlayerManager;
    this.shopInventory = shopInventory;
    this.mapVoteInventory = mapVoteInventory;
    this.rescuePlatform = rescuePlatform;
  }

  @EventHandler
  public void onInteract(PlayerInteractEvent e) {
    Player player = e.getPlayer();
    try {
      if (e.getClickedBlock().getType() == Material.STONE_PLATE) {
        return;
      }
    } catch (Exception e2) {

    }

    BuildFFAPlayer buildFFAPlayer = buildFFAPlayerManager.getPlayer(player);

    if (buildFFAPlayer == null) {
      return;
    }

    ItemStack itemInHand = player.getItemInHand();
    if (itemInHand != null
        && itemInHand.hasItemMeta()
        && itemInHand.getItemMeta().hasDisplayName()) {
      String displayName = itemInHand.getItemMeta().getDisplayName();
      if (displayName.equals(LangUtils.getMessage(player, ItemEnum.LEAVE_ITEM))) {
        if (System.currentTimeMillis() - buildFFAPlayer.getLatestDeadTime()
            > TimeUnit.SECONDS.toMillis(1)) {
          player.kickPlayer("");
        }
        return;
      }
      // TODO(economy): Re-enable opening the shop once the coin system is implemented.
      // if (displayName.equals(LangUtils.getMessage(player, ItemEnum.SHOP))) {
      //   shopInventory.openShopInventory(player);
      //   return;
      // }
      if (displayName.equals(LangUtils.getMessage(player, InventoryEnum.VOTE))) {
        player.openInventory(mapVoteInventory.getInventory(player));
        return;
      }
      if (displayName.equals(LangUtils.getMessage(player, ItemEnum.RESCUE_PLATFORM))) {
        rescuePlatform.onClick(e);
        return;
      }
      if (displayName.equals(StringUtils.color("&cKillEffects"))) {
        player.performCommand("killeffect");
        return;
      }
    }
  }
}
