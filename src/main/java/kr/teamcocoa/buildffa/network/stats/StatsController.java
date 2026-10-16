package kr.teamcocoa.buildffa.network.stats;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import kr.teamcocoa.buildffa.game.models.BuildFFAStats;
import kr.teamcocoa.buildffa.network.ApiClient;
import org.bukkit.Bukkit;
import retrofit2.Response;

/** Blocking calls. Never call these from the main thread. */
@Singleton
public class StatsController {

  private final StatsService service;

  @Inject
  public StatsController(ApiClient apiClient) {
    this.service = apiClient.create(StatsService.class);
  }

  /**
   * @return true if the player had stats, false if new stats (all 0) were created
   * @throws IllegalStateException if the request failed
   */
  public boolean loadStats(BuildFFAStats stats) {
    try {
      Response<StatsDto> response = service.getStats(stats.getUuid().toString()).execute();

      if (response.code() == 404) {
        stats.init(0, 0, 0, 0);
        return false;
      }

      StatsDto body = response.body();
      if (!response.isSuccessful() || body == null) {
        throw new IllegalStateException(
            "Failed to load stats of " + stats.getUuid() + " (HTTP " + response.code() + ")");
      }

      stats.init(
          body.getKills(), body.getDeaths(), body.getKillStreaks(), body.getBestKillStreaks());
      return true;
    } catch (IOException e) {
      throw new IllegalStateException("Failed to load stats of " + stats.getUuid(), e);
    }
  }

  /** Marks the stats as saved on success, unless they changed while the request was in flight. */
  public boolean upsertStats(BuildFFAStats stats) {
    StatsDto snapshot;
    long revision;
    synchronized (stats) {
      snapshot = new StatsDto(stats);
      revision = stats.getRevision();
    }

    try {
      Response<Void> response = service.upsertStats(stats.getUuid().toString(), snapshot).execute();
      if (!response.isSuccessful()) {
        Bukkit.getLogger()
            .warning(
                "[BuildFFA] Failed to save stats of "
                    + stats.getUuid()
                    + " (HTTP "
                    + response.code()
                    + ")");
        return false;
      }
      stats.markSaved(revision);
      return true;
    } catch (IOException e) {
      Bukkit.getLogger()
          .warning(
              "[BuildFFA] Failed to save stats of " + stats.getUuid() + " : " + e.getMessage());
      return false;
    }
  }
}
