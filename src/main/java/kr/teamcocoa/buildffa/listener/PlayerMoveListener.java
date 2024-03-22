package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.buildffa.world.maps.BuildFFAMap;
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

        BuildFFAMap currentMap = WorldManager.getInstance().getCurrentMap();

        if(!buildFFAPlayer.isBuild() &&
                buildFFAPlayer.isInGame() &&
                currentMap.getDeathHeight() < location.getY()) {
            buildFFAPlayer.death(false);
        }

        if(!buildFFAPlayer.isBuild() &&
                !buildFFAPlayer.isInGame() &&
                currentMap.getArenaHeight() < location.getY()) {
            buildFFAPlayer.arenaJoin();
        }

    }
}
