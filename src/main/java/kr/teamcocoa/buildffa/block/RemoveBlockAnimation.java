package kr.teamcocoa.buildffa.block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class RemoveBlockAnimation extends BukkitRunnable {
    public static final List<DespawnBlock> blocks = new ArrayList<>();

    public void run(){
        for(Iterator<DespawnBlock> iterator = blocks.iterator(); iterator.hasNext();) {
            DespawnBlock despawnBlock = iterator.next();
            boolean result = despawnBlock.run();
            if(!result) {
                iterator.remove();
            }
        }
    }

}
