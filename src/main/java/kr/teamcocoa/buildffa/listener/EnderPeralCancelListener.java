package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.world.WorldManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;

public class EnderPeralCancelListener implements Listener {

    @EventHandler
    public void throwEnderPeral(PlayerInteractEvent e){
        final Player player = e.getPlayer();

        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(player.getItemInHand().getType().equals(Material.ENDER_PEARL)) {
                Location spawnLocation = WorldManager.getInstance().getCurrentMap().getSpawn();
                if(player.getLocation().getY() >= spawnLocation.getY()) {
                    e.setCancelled(true);
                    return;
                }
                BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);
                long now = System.currentTimeMillis();
                buildFFAPlayer.setThrewPearlTime(now);
                return;
            }
        }
        return;
    }

    @EventHandler
    public void teleportEnderPearl(PlayerTeleportEvent e){

        if(e.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL){
            return;
        }

        Player player = e.getPlayer();

        BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);

        long threwTime = buildFFAPlayer.getThrewPearlTime();
        long latestDeadTime = buildFFAPlayer.getLatestDeadTime();

        long now = System.currentTimeMillis();
        if(latestDeadTime < threwTime && threwTime < now){
            Location spawnLocation = WorldManager.getInstance().getCurrentMap().getSpawn();
            if(e.getTo().getY() >= spawnLocation.getY() - 5){
                e.setCancelled(true);
            }
        }
        else{
            e.setCancelled(true);
        }
        return;
    }
}
