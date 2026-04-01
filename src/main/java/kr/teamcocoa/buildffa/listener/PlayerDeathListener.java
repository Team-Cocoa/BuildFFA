package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    // 이건 수정 보류

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        e.setCancelled(true);

        BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);

        Player killer = buildFFAPlayer.getLastHitPlayer();

        if(killer != null) {
            BuildFFAPlayer killerBuildFFAPlayer = BuildFFAPlayerManager.getPlayer(killer);
            killerBuildFFAPlayer.kill(buildFFAPlayer);
        }

        buildFFAPlayer.reset();
        buildFFAPlayer.death(false);

    }
}
