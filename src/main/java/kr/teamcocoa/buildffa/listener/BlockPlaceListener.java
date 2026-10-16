package kr.teamcocoa.buildffa.listener;

import com.google.inject.Inject;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.world.DeSpawnBlock;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.buildffa.world.maps.BuildFFAMap;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  private final WorldManager worldManager;

  private final DeSpawnBlock.Factory deSpawnBlockFactory;

  @Inject
  public BlockPlaceListener(
      BuildFFAPlayerManager buildFFAPlayerManager,
      WorldManager worldManager,
      DeSpawnBlock.Factory deSpawnBlockFactory) {
    this.buildFFAPlayerManager = buildFFAPlayerManager;
    this.worldManager = worldManager;
    this.deSpawnBlockFactory = deSpawnBlockFactory;
  }

  @EventHandler
  public void onBlockPlace(final BlockPlaceEvent e) {
    BuildFFAMap currentMap = worldManager.getCurrentMap();
    if (!currentMap.isPlaceAble()) {
      e.setCancelled(true);
      return;
    }

    Player player = e.getPlayer();
    BuildFFAPlayer buildFFAPlayer = buildFFAPlayerManager.getPlayer(player);

    if (buildFFAPlayer.isBuild()) {
      return;
    }

    double arenaStartHeight = currentMap.getArenaHeight();

    Block block = e.getBlock();

    if (e.getBlock().getY() < arenaStartHeight) {
      DeSpawnBlock deSpawnBlock = deSpawnBlockFactory.create(buildFFAPlayer, block, false);
      currentMap.addBlock(deSpawnBlock);
    } else {
      e.setCancelled(true);
    }
  }
}
