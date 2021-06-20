package kr.teamcocoa.buildffa.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupItemListener implements Listener {
    @EventHandler
    public void onArrowPickup(PlayerPickupItemEvent event){
        event.setCancelled(true);
    }
}
