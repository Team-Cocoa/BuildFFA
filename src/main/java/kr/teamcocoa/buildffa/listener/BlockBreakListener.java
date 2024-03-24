package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);

        e.setCancelled(!buildFFAPlayer.isBuild());
    }
}
