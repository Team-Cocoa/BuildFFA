package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.managers.PlayerManager;
import kr.teamcocoa.buildffa.model.BuildFFAPlayer;
import kr.teamcocoa.buildffa.model.DeSpawnBlock;
import kr.teamcocoa.buildffa.world.WorldManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!WorldManager.getInstance().isPlaceAble()) {
            e.setCancelled(true);
            return;
        }
        Player player = e.getPlayer();

        double height = 207;

        BuildFFAPlayer buildFFAPlayer = PlayerManager.getPlayer(player);

        if(buildFFAPlayer == null) {
            e.setCancelled(true);
            return;
        }

        if (player.getLocation().getY() <= height) {
            if (!buildFFAPlayer.isBuild()) {
                Block block = e.getBlock();
                DeSpawnBlock deSpawnBlock = new DeSpawnBlock(block);
                deSpawnBlock.initIndex();
                deSpawnBlock.start();
            }
        }
        else if (!buildFFAPlayer.isBuild()) {
            e.setCancelled(true);
        }
    }
}
