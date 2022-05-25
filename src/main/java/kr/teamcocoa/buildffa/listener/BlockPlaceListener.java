package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.block.DespawnBlock;
import kr.teamcocoa.buildffa.main.Main;


import kr.teamcocoa.buildffa.world.WorldManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;


public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent e) {
        if (!WorldManager.getInstance().isPlaceAble()) {
            e.setCancelled(true);
            return;
        }
        Player p = e.getPlayer();
        double height = 207;
        if (p.getLocation().getY() <= height) {
            if (!Main.playerData.get(p).isBuild()) {
                Block b = e.getBlock();
                Main.worldData.addBlock(b);
                new DespawnBlock(e, b).runTaskTimerAsynchronously(Main.getInstance(), 0L, 10L);
            }
        } else if (!Main.playerData.get(p).isBuild()) {
            e.setCancelled(true);
        }
    }
}
