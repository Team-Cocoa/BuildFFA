package kr.teamcocoa.buildffa.block;

import kr.teamcocoa.buildffa.main.Main;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class RemoveBlockAnimation extends BukkitRunnable {
    public static final List<DespawnBlock> blocks = new ArrayList<>();

    public void run(){
        for(Iterator<DespawnBlock> iterator = blocks.iterator(); iterator.hasNext();) {
            DespawnBlock despawnBlock = iterator.next();
            boolean result = despawnBlock.run();
            if(!result) {
                //Bukkit.getLogger().info("block removed!");
                iterator.remove();
                //Bukkit.getLogger().info(String.valueOf(blocks.contains(despawnBlock)));
            }
        }
        //Main.printMemory();
    }

}
