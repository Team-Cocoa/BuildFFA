package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
    if(!ScoreboardManager.pvpAble) {
      e.setCancelled(true);
      return;
    }
    if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
      Player damagedPlayer = (Player) e.getEntity();
      Player damager = (Player) e.getDamager();
      if (!Main.playerData.get(damager).isInGame()) {
        e.setCancelled(true);
      }
      Main.playerData.get(damagedPlayer).setLastHitPlayer(damager);
    }
  }
  
  @EventHandler
  public void onEntityDamage(EntityDamageEvent e) {
    if(!ScoreboardManager.pvpAble) {
      e.setCancelled(true);
      return;
    }
    if(e.getEntity() instanceof Player) {
      if(!Main.playerData.get((Player) e.getEntity()).isInGame()) {
        e.setCancelled(true);
        return;
      }
    }
    if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
      e.setCancelled(true);
      return;
    }
  }
}
