package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.world.DeSpawnBlock;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.buildffa.world.maps.BuildFFAMap;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent e) {
        BuildFFAMap currentMap = WorldManager.getInstance().getCurrentMap();
        if (!currentMap.isPlaceAble()) {
            e.setCancelled(true);
            return;
        }

        Player player = e.getPlayer();
        BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);

        if(buildFFAPlayer.isBuild()) {
            return;
        }

        double arenaStartHeight = currentMap.getArenaHeight();

        Block block = e.getBlock();

        if(e.getBlock().getY() < arenaStartHeight) {
            DeSpawnBlock deSpawnBlock = new DeSpawnBlock(buildFFAPlayer, block, false);
            currentMap.addBlock(deSpawnBlock);
        }
        else {
            e.setCancelled(true);
        }
    }

}
