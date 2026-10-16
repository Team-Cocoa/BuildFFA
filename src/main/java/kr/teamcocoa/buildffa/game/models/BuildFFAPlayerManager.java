package kr.teamcocoa.buildffa.game.models;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kr.teamcocoa.core.network.cache.Cache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Singleton
public class BuildFFAPlayerManager {

  // Entries must never expire while the player is online, so the TTL is effectively infinite
  private final Cache<Player, BuildFFAPlayer> cache = new Cache<>(Integer.MAX_VALUE, TimeUnit.DAYS);

  private final BuildFFAPlayer.Factory buildFFAPlayerFactory;

  @Inject
  public BuildFFAPlayerManager(BuildFFAPlayer.Factory buildFFAPlayerFactory) {
    this.buildFFAPlayerFactory = buildFFAPlayerFactory;
  }

  public boolean addPlayer(Player player, BuildFFAStats stats) {
    if (cache.readData(player) != null) {
      return false;
    }
    cache.createData(player, buildFFAPlayerFactory.create(player, stats));
    return true;
  }

  public BuildFFAPlayer getPlayer(Player player) {
    if (player == null) {
      return null;
    }
    return cache.readData(player);
  }

  public boolean removePlayer(Player player) {
    if (cache.readData(player) == null) {
      return false;
    }
    cache.deleteData(player);
    return true;
  }

  // Cache cannot be iterated, so walk the online player list instead
  public List<BuildFFAPlayer> getPlayers() {
    List<BuildFFAPlayer> players = new ArrayList<>();
    for (Player player : Bukkit.getOnlinePlayers()) {
      BuildFFAPlayer buildFFAPlayer = cache.readData(player);
      if (buildFFAPlayer != null) {
        players.add(buildFFAPlayer);
      }
    }
    return players;
  }
}
