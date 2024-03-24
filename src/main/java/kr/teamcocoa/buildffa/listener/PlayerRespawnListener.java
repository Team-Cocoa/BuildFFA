package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);
        buildFFAPlayer.setJoinInventory();
    }
}
