package kr.teamcocoa.buildffa.world;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.task.MapChangeTask;
import kr.teamcocoa.buildffa.task.MapTeleportTask;
import kr.teamcocoa.buildffa.world.maps.BuildFFAMap;
import kr.teamcocoa.buildffa.world.maps.Maps;
import org.bukkit.plugin.Plugin;

@Singleton
public class WorldManager {

  private final Plugin plugin;

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  // MapChangeTask depends on WorldManager, so a Provider breaks the cycle
  private final Provider<MapChangeTask> mapChangeTaskProvider;

  private HashMap<Maps, BuildFFAMap> maps = new HashMap<>();

  public synchronized BuildFFAMap getCurrentMap() {
    return currentMap;
  }

  public synchronized void setCurrentMap(BuildFFAMap currentMap) {
    this.currentMap = currentMap;
  }

  private BuildFFAMap currentMap;

  @Inject
  public WorldManager(
      Plugin plugin,
      BuildFFAPlayerManager buildFFAPlayerManager,
      Provider<MapChangeTask> mapChangeTaskProvider) {
    this.plugin = plugin;
    this.buildFFAPlayerManager = buildFFAPlayerManager;
    this.mapChangeTaskProvider = mapChangeTaskProvider;

    BuildFFAMap architecture =
        new BuildFFAMap(
            plugin, Maps.ARCHITECTURE, "architecture", false, false, 0.5, 200, 0.5, 173, 100);
    BuildFFAMap flatland =
        new BuildFFAMap(plugin, Maps.FLATLAND, "flatland", false, false, 0.5, 200, 0.5, 185, 100);
    BuildFFAMap spring =
        new BuildFFAMap(plugin, Maps.SPRING, "spring", false, false, 0.5, 200, 0.5, 185, 30);
    BuildFFAMap CWBW =
        new BuildFFAMap(plugin, Maps.CWBW, "CWBW", false, false, 0.5, 200, 0.5, 190, 50);

    maps.put(Maps.ARCHITECTURE, architecture);
    maps.put(Maps.FLATLAND, flatland);
    maps.put(Maps.SPRING, spring);
    maps.put(Maps.CWBW, CWBW);
  }

  public void mapChange(Maps newMaps) {
    if (currentMap != null) {
      currentMap.mapSessionStop();
    }

    BuildFFAMap newMap = maps.get(newMaps);
    setCurrentMap(newMap);

    Queue<BuildFFAPlayer> queue = new LinkedList<>(buildFFAPlayerManager.getPlayers());

    new MapTeleportTask(queue, newMap, mapChangeTaskProvider.get()).runTaskTimer(plugin, 0L, 1L);
  }
}
