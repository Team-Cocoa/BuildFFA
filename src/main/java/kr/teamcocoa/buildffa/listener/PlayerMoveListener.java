package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.buildffa.world.maps.BuildFFAMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        Location location = player.getLocation();

        BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);

        if(buildFFAPlayer == null) {
            Bukkit.getLogger().info("bffaplayer is null in playermoveevent | " + player.getName());
            return;
        }

        BuildFFAMap currentMap = WorldManager.getInstance().getCurrentMap();

        if(location.getY() < 0 || (!buildFFAPlayer.isBuild() &&
                buildFFAPlayer.isInGame() &&
                currentMap.getDeathHeight() > location.getY())) {
            Player killer = buildFFAPlayer.getLastHitPlayer();

            if(killer != null) {
                BuildFFAPlayer killerBuildFFAPlayer = BuildFFAPlayerManager.getPlayer(killer);
                killerBuildFFAPlayer.kill(buildFFAPlayer);
            }

            buildFFAPlayer.reset();
            buildFFAPlayer.death(false);
            return;
            // ^ 이거 없애기 금지 이거 없애면 이 파트 실행되고 밑에 if 문도 실행됨
        }

        if(!buildFFAPlayer.isBuild() &&
                !buildFFAPlayer.isInGame() &&
                currentMap.getArenaHeight() > location.getY()) {
            buildFFAPlayer.arenaJoin();
        }

    }
}
