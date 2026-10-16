package kr.teamcocoa.buildffa.task;

import com.google.inject.Inject;
import java.util.logging.Level;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.game.models.BuildFFAStats;
import kr.teamcocoa.buildffa.network.stats.StatsController;
import org.bukkit.Bukkit;

public class StatsAutoSaveTask implements Runnable {

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  private final StatsController statsController;

  @Inject
  public StatsAutoSaveTask(
      BuildFFAPlayerManager buildFFAPlayerManager, StatsController statsController) {
    this.buildFFAPlayerManager = buildFFAPlayerManager;
    this.statsController = statsController;
  }

  @Override
  public void run() {
    // An exception escaping run() would cancel the scheduleAtFixedRate task permanently
    for (BuildFFAPlayer buildFFAPlayer : buildFFAPlayerManager.getPlayers()) {
      try {
        BuildFFAStats stats = buildFFAPlayer.getBuildFFAStats();
        if (stats.isEdited() && statsController.upsertStats(stats)) {
          Bukkit.getLogger().info("Auto saved " + buildFFAPlayer.getPlayer().getName() + " stats.");
        }
      } catch (Throwable t) {
        Bukkit.getLogger()
            .log(
                Level.SEVERE,
                "[BuildFFA] Auto save failed for " + buildFFAPlayer.getPlayer().getName(),
                t);
      }
    }
  }
}
