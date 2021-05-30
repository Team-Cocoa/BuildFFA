package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
  @EventHandler
  public static void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
    if (e.getEntity() instanceof Player) {
      Player damagedPlayer = (Player)e.getEntity();
      if (!Main.playerData.get(damagedPlayer).isInGame()) {
        e.setCancelled(true);
      }
    } 
  }
  
  @EventHandler
  public static void onEntityDamage(EntityDamageEvent e) {
    if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL))
      e.setCancelled(true); 
  }
}
