package kr.teamcocoa.buildffa.world;

import kr.teamcocoa.buildffa.main.BuildFFA;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorldData {

    private List<Block> blocks = new ArrayList<>();

    public void removeBlocks() {
        Bukkit.getScheduler().runTaskAsynchronously(BuildFFA.getInstance(), () -> {
            Iterator<Block> it = blocks.iterator();
            while (it.hasNext()) {
                Block block = it.next();
                it.remove();
            }
        });
    }

    public void addBlock(Block block){
        blocks.add(block);
    }

    public void removeBlock(Block block){
        blocks.remove(block);
    }
}
