package kr.teamcocoa.buildffa.world.maps;

import lombok.Getter;
import org.bukkit.block.Block;

import java.util.LinkedList;
import java.util.List;

@Getter
public abstract class BuildFFAMap {

    private String name;

    private String slimeWorldName;

    private boolean pvpAble;

    private boolean placeAble;

    private int arenaHeight;

    private int deathHeight;

    private List<Block> blocks = new LinkedList<>();

    public void loadWorld() {

    }

    protected BuildFFAMap(String name, String slimeWorldName, boolean pvpAble, boolean placeAble, int arenaHeight, int deathHeight) {
        this.name = name;
        this.slimeWorldName = slimeWorldName;
        this.pvpAble = pvpAble;
        this.placeAble = placeAble;
        this.arenaHeight = arenaHeight;
        this.deathHeight = deathHeight;
    }

    public void addBlock(Block block) {
        blocks.add(block);
    }

    public void removeBlock(Block block) {
        blocks.remove(block);
    }

}
