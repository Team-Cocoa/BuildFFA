package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.BuildFFA;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!BuildFFA.playerData.get(p).isBuild()) {
            e.setCancelled(true);
        }
    }
}
