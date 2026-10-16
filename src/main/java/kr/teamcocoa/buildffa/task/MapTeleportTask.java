package kr.teamcocoa.buildffa.task;

import java.util.Queue;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.world.maps.BuildFFAMap;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class MapTeleportTask extends BukkitRunnable {

  private static final int PLAYERS_PER_TICK = 3;

  private final Queue<BuildFFAPlayer> queue;

  private final BuildFFAMap newMap;

  private final Location location;

  private final MapChangeTask mapChangeTask;

  public MapTeleportTask(
      Queue<BuildFFAPlayer> queue, BuildFFAMap newMap, MapChangeTask mapChangeTask) {
    this.queue = queue;
    this.newMap = newMap;
    this.mapChangeTask = mapChangeTask;
    this.location = newMap.getSpawn();
  }

  @Override
  public void run() {
    for (int i = 0; i < PLAYERS_PER_TICK; i++) {
      if (queue.isEmpty()) {
        cancel();
        newMap.mapSessionStart();
        mapChangeTask.resetTimer();
        break;
      }

      // 혹시 큐 돌리다가 플레이어가 나가면 npe 같은 에러 뜰 수 있으니까 사전 방지

      try {
        BuildFFAPlayer buildFFAPlayer = queue.poll();
        buildFFAPlayer.reset();
        buildFFAPlayer.setLastHitPlayer(null);
        buildFFAPlayer.getPlayer().teleport(location);
        buildFFAPlayer.setJoinInventory();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
