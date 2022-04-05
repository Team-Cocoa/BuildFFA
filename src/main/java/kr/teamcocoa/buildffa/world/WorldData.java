package kr.teamcocoa.buildffa.world;

import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorldData {

    private List<Block> blocks = new ArrayList<>();

    public void removeBlocks() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.inst(), () -> {
            Iterator<Block> it = blocks.iterator();
            while (it.hasNext()) {
                it.remove();
            }
        });
    }

    public void addBlock(Block block){
        blocks.add(block);
    }

    public void removeBlock(Block block){
        block.setType(Material.AIR);
        blocks.remove(block);
    }
}
