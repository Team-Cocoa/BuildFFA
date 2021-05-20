package listener;

import main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EnderPeralCancelListener implements Listener {

    @EventHandler
    public void throwEnderPeral(PlayerInteractEvent e){
        final Player player = e.getPlayer();

        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if(player.getItemInHand().getType().equals(Material.ENDER_PEARL))
            {
                long now = System.currentTimeMillis();
                Main.playerData.throwPeralTime.put(player, now);
                return;
            }
            else {
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
        long throwedTime = Main.playerData.throwPeralTime.get(player);
        long latestDeadTime = Main.playerData.latestDeadTime.get(player);
        long now = System.currentTimeMillis();
        if(latestDeadTime < throwedTime && throwedTime < now){

        }
        else{
            e.setCancelled(true);
        }
        return;
    }
}
