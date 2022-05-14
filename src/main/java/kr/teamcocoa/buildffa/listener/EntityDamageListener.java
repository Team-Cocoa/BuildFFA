package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.ScoreboardManager;
import kr.teamcocoa.buildffa.world.WorldManager;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class EntityDamageListener implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!WorldManager.getInstance().isPvpAble()) {
            e.setCancelled(true);
            return;
        }
        if (e.getEntity() instanceof Player) {
            if(e.getDamager() instanceof Projectile || e.getDamager() instanceof Player) {
                Player damagedPlayer = (Player) e.getEntity();
                Player damager = e.getDamager() instanceof Player
                        ? (Player) e.getDamager()
                        : (Player) ((Projectile) e.getDamager()).getShooter();
                if (!Main.playerData.get(damager).isInGame()) {
                    e.setCancelled(true);
                }
                else {
                    Main.playerData.get(damagedPlayer).setLastHitPlayer(damager);
                    Main.playerData.get(damagedPlayer).addDamage(damager, e.getFinalDamage());
                }
                synchronized (damagedPlayer) {
                    if (damagedPlayer.getHealth() - e.getFinalDamage() <= 0) {
                        Main.inst().getServer().getPluginManager().callEvent(CraftEventFactory.callPlayerDeathEvent(((CraftPlayer) damagedPlayer).getHandle(), null, "", true));
                        damagedPlayer.setHealth(20.0);
                        e.setCancelled(true);
                        Main.playerData.get(damagedPlayer).death(false); // TODO : death 함수 synchronized 화 하고, 위에 3줄 로직을 death 함수 안에 우겨넣기
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
