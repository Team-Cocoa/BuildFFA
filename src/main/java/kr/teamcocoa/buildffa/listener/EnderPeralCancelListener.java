package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.world.WorldManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import kr.teamcocoa.buildffa.kit.BffaPlayer;

public class EnderPeralCancelListener implements Listener {

    @EventHandler
    public void throwEnderPeral(PlayerInteractEvent e){
        final Player player = e.getPlayer();

        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if(player.getItemInHand().getType().equals(Material.ENDER_PEARL))
            {
                BffaPlayer bffaPlayer = Main.playerData.get(player);
                long now = System.currentTimeMillis();
                bffaPlayer.setThrewPearlTime(now);
                Main.playerData.put(player, bffaPlayer);
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
        final Player player = e.getPlayer();
        BffaPlayer bffaPlayer = Main.playerData.get(player);
        long threwTime = bffaPlayer.getThrewPearlTime();
        long latestDeadTime = bffaPlayer.getLatestDeadTime();
        long now = System.currentTimeMillis();
        if(latestDeadTime < threwTime && threwTime < now){
            Location spawnLocation = WorldManager.getInstance().getSpawnByName(WorldManager.getInstance().getCurrentMap());
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
