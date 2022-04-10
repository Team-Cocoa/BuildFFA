package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.ScoreboardManager;
import kr.teamcocoa.buildffa.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!WorldManager.getInstance().isPvpAble()) {
            e.setCancelled(true);
            return;
        }
        if (e.getEntity() instanceof Player) {
            if(e.getDamager() instanceof Arrow || e.getDamager() instanceof Player) {
                Player damagedPlayer = (Player) e.getEntity();
                Player damager = e.getDamager() instanceof Player
                        ? (Player) e.getDamager()
                        : (Player) ((Arrow) e.getDamager()).getShooter();
                if (!Main.playerData.get(damager).isInGame()) {
                    e.setCancelled(true);
                }
                else {
                    Main.playerData.get(damagedPlayer).setLastHitPlayer(damager);
                }
                synchronized (damagedPlayer) {
                    if (damagedPlayer.getHealth() - e.getFinalDamage() <= 0) {
                        damagedPlayer.setHealth(20.0);
                        e.setCancelled(true);
                        Main.playerData.get(damagedPlayer).death(false);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!WorldManager.getInstance().isPvpAble()) {
                e.setCancelled(true);
                return;
            }

            if (!Main.playerData.get((Player) e.getEntity()).isInGame()) {
                e.setCancelled(true);
                return;
            }

            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                e.setCancelled(true);
                return;
            }
        }
    }
}
