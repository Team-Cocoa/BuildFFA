package kr.teamcocoa.buildffa.world.maps;

import kr.teamcocoa.buildffa.BuildFFABootstrap;
import kr.teamcocoa.buildffa.world.DeSpawnBlock;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.LinkedBlockingQueue;

@Getter
public class BuildFFAMap {

    private Maps maps;

    private String slimeWorldName;

    private boolean pvpAble;

    private boolean placeAble;

    private double spawnX;

    private double spawnY;

    private double spawnZ;

    private int arenaHeight;

    private int deathHeight;

    private LinkedBlockingQueue<DeSpawnBlock> blocks = new LinkedBlockingQueue<>();

    private BukkitTask blockScheduler;

    public BuildFFAMap(Maps maps, String slimeWorldName, boolean pvpAble, boolean placeAble, double spawnX, double spawnY, double spawnZ, int arenaHeight, int deathHeight) {
        this.maps = maps;
        this.slimeWorldName = slimeWorldName;
        this.pvpAble = pvpAble;
        this.placeAble = placeAble;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnZ = spawnZ;
        this.arenaHeight = arenaHeight;
        this.deathHeight = deathHeight;
    }

    public void removeAllBlocks() {
        for (DeSpawnBlock deSpawnBlock : blocks) {
            deSpawnBlock.makeAir();
        }
        blocks.clear();
    }

    public void addBlock(DeSpawnBlock block) {
        blocks.add(block);
    }

    public void removeBlock(DeSpawnBlock block) {
        blocks.remove(block);
    }

    public void mapSessionStart() {
        this.pvpAble = true;
        this.placeAble = true;
        this.blockScheduler =
                Bukkit.getScheduler().runTaskTimerAsynchronously(
                        BuildFFABootstrap.getInstance(),
                        () -> {
                            for (DeSpawnBlock deSpawnBlock : blocks) {
                                deSpawnBlock.tick();
                                if (deSpawnBlock.isExpire()) {
                                    removeBlock(deSpawnBlock);
                                }
                            }
                        },
                        1L, 10L);
    }

    public void mapSessionStop() {
        this.pvpAble = false;
        this.placeAble = false;
        blockScheduler.cancel();
    }

    public Location getSpawn() {
        return new Location(
                Bukkit.getWorld(slimeWorldName),
                spawnX,
                spawnY,
                spawnZ);
    }

}
