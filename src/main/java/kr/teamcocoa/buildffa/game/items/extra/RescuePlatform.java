package kr.teamcocoa.buildffa.game.items.extra;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.translation.ItemEnum;
import kr.teamcocoa.buildffa.translation.LangUtils;
import kr.teamcocoa.buildffa.translation.MessageEnum;
import kr.teamcocoa.buildffa.world.DeSpawnBlock;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.core.bukkit.utils.ItemUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@Singleton
public class RescuePlatform extends AbstractExtraItem implements UseAble {

  private final WorldManager worldManager;

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  private final DeSpawnBlock.Factory deSpawnBlockFactory;

  @Inject
  public RescuePlatform(
      WorldManager worldManager,
      BuildFFAPlayerManager buildFFAPlayerManager,
      DeSpawnBlock.Factory deSpawnBlockFactory) {
    this.worldManager = worldManager;
    this.buildFFAPlayerManager = buildFFAPlayerManager;
    this.deSpawnBlockFactory = deSpawnBlockFactory;
  }

  @Override
  public ItemStack getItemStack(Player player, int count) {
    ItemStack itemStack = new ItemStack(Material.BLAZE_ROD, count);
    ItemUtils.name(itemStack, LangUtils.getMessage(player, ItemEnum.RESCUE_PLATFORM));
    return itemStack;
  }

  @Override
  public String getName(Player player) {
    return LangUtils.getMessage(player, ItemEnum.RESCUE_PLATFORM);
  }

  @Override
  public void onClick(PlayerInteractEvent e) {

    Player player = e.getPlayer();

    if (worldManager.getCurrentMap().getDeathHeight() + 50 > player.getLocation().getY()) {
      player
          .getInventory()
          .setItem(
              player.getInventory().getHeldItemSlot(),
              player.getItemInHand().getAmount() - 1 == 0
                  ? null
                  : getItemStack(player, player.getItemInHand().getAmount() - 1));
      Location location = player.getLocation().clone().add(0, -5, 0);
      for (int i = -1; i < 2; i++) {
        for (int j = -1; j < 2; j++) {
          Location cloned = location.clone().add(i, 0, j);
          Block block = cloned.getBlock();
          block.setType(Material.SLIME_BLOCK);
          worldManager
              .getCurrentMap()
              .addBlock(
                  deSpawnBlockFactory.create(
                      buildFFAPlayerManager.getPlayer(player), block, false));
        }
      }
      player.sendMessage(LangUtils.getMessage(player, MessageEnum.USED_PLATFORM));
    } else {
      player.sendMessage(LangUtils.getMessage(player, MessageEnum.CANNOT_USE_PLATFORM));
    }
  }
}
