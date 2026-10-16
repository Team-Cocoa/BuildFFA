package kr.teamcocoa.buildffa.listener;

import com.google.inject.Inject;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  @Inject
  public BlockBreakListener(BuildFFAPlayerManager buildFFAPlayerManager) {
    this.buildFFAPlayerManager = buildFFAPlayerManager;
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {
    Player player = e.getPlayer();

    BuildFFAPlayer buildFFAPlayer = buildFFAPlayerManager.getPlayer(player);

    e.setCancelled(!buildFFAPlayer.isBuild());
  }
}
