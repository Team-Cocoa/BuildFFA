package kr.teamcocoa.buildffa.listener;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.network.stats.StatsController;
import kr.teamcocoa.buildffa.world.MapVote;
import kr.teamcocoa.buildffa.world.maps.Maps;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  private final MapVote mapVote;

  private final StatsController statsController;

  @Inject
  public QuitListener(
      BuildFFAPlayerManager buildFFAPlayerManager,
      MapVote mapVote,
      StatsController statsController) {
    this.buildFFAPlayerManager = buildFFAPlayerManager;
    this.mapVote = mapVote;
    this.statsController = statsController;
  }

  // Unbounded queue so a burst of quits never rejects a stats save
  private final ExecutorService executors =
      Executors.newFixedThreadPool(
          4, new ThreadFactoryBuilder().setNameFormat("bffa-datasave-pool-%d").build());

  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    Player player = e.getPlayer();
    e.setQuitMessage("");

    BuildFFAPlayer buildFFAPlayer = buildFFAPlayerManager.getPlayer(player);

    if (buildFFAPlayer == null) {
      return;
    }

    String quitMessage =
        StringUtils.color("&a[&dBuildFFA&a] &e" + player.getName() + " left the game!");
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      onlinePlayer.sendMessage(quitMessage);
    }

    Maps votedMap = mapVote.getWhatPlayerVoted(player);

    if (votedMap != null) {
      mapVote.removeVote(player, votedMap);
    }

    if (buildFFAPlayer.isInGame()) {
      buildFFAPlayer.death(true);
    }

    buildFFAPlayerManager.removePlayer(player);

    executors.execute(() -> statsController.upsertStats(buildFFAPlayer.getBuildFFAStats()));
  }
}
