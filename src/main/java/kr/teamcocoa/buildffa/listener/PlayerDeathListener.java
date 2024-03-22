package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();

        BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);

        e.setCancelled(true);

        Player killer = buildFFAPlayer.getLastHitPlayer();

        if(killer == null) {
            return;
        }
        else {
            BuildFFAPlayer killerBuildFFAPlayer = BuildFFAPlayerManager.getPlayer(killer);
            killerBuildFFAPlayer.kill(buildFFAPlayer);
        }

        buildFFAPlayer.death(false);
    }
}
