package kr.teamcocoa.buildffa.world;

import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.world.maps.BuildFFAMap;
import kr.teamcocoa.buildffa.world.maps.Maps;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class WorldManager {

    private static WorldManager instance;

    private HashMap<Maps, BuildFFAMap> maps = new HashMap<>();


    public synchronized BuildFFAMap getCurrentMap() {
        return currentMap;
    }

    public synchronized void setCurrentMap(BuildFFAMap currentMap) {
        this.currentMap = currentMap;
    }

    private BuildFFAMap currentMap;

    public static WorldManager getInstance() {
        if(instance == null) {
            instance = new WorldManager();
            return instance;
        }
        return instance;
    }

    private WorldManager() {
        BuildFFAMap architecture = new BuildFFAMap(
                Maps.ARCHITECTURE,
                "architecture",
                false,
                false,
                0.5,
                200,
                0.5,
                173,
                100);
        BuildFFAMap flatland = new BuildFFAMap(
                Maps.FLATLAND,
                "flatland",
                false,
                false,
                0.5,
                200,
                0.5,
                185,
                100);
        BuildFFAMap spring = new BuildFFAMap(
                Maps.SPRING,
                "spring",
                false,
                false,
                0.5,
                200,
                0.5,
                185,
                30);
        BuildFFAMap CWBW = new BuildFFAMap(
                Maps.CWBW,
                "CWBW",
                false,
                false,
                0.5,
                200,
                0.5,
                190,
                50);

        maps.put(Maps.ARCHITECTURE, architecture);
        maps.put(Maps.FLATLAND, flatland);
        maps.put(Maps.SPRING, spring);
        maps.put(Maps.CWBW, CWBW);
    }

    public void mapChange(Maps newMaps) {
        if(currentMap != null) {
            currentMap.mapSessionStop();
        }

        BuildFFAMap newMap = maps.get(newMaps);
        setCurrentMap(newMap);

        Location location = newMap.getSpawn();

        Queue<BuildFFAPlayer> queue = new LinkedList<>(BuildFFAPlayerManager.getPlayerTable().values());

        (new BukkitRunnable() {
            @Override
            public void run() {
                for(int i = 0; i < 3; i++) {
                    if(queue.isEmpty()) {
                        cancel();
                        newMap.mapSessionStart();
                        MapChangeScheduler.getInstance().setSec(600);
                        break;
                    }

                    // 혹시 큐 돌리다가 플레이어가 나가면 npe 같은 에러 뜰 수 있으니까 사전 방지

                    try {
                        BuildFFAPlayer buildFFAPlayer = queue.poll();
                        buildFFAPlayer.reset();
                        buildFFAPlayer.getPlayer().teleport(location);
                        buildFFAPlayer.setJoinInventory();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).runTaskTimer(BuildFFA.getInstance(), 0L, 1L);

    }

}
