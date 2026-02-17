package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.core.bukkit.utils.PacketUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDamageListener implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
            return;
        }
        if (!WorldManager.getInstance().getCurrentMap().isPvpAble()) {
            e.setCancelled(true);
            return;
        }
        if (e.getEntity() instanceof Player) {
            if (e.getDamager() instanceof Projectile || e.getDamager() instanceof Player) {
                Player damagedPlayer = (Player) e.getEntity();
                Player damager = e.getDamager() instanceof Player
                        ? (Player) e.getDamager()
                        : (Player) ((Projectile) e.getDamager()).getShooter();

                BuildFFAPlayer damagedBFFAPlayer = BuildFFAPlayerManager.getPlayer(damagedPlayer);
                BuildFFAPlayer damagerBFFAPlayer = BuildFFAPlayerManager.getPlayer(damager);

                if (!damagerBFFAPlayer.isInGame() || !damagedBFFAPlayer.isInGame()) {
                    e.setCancelled(true);
                }
                else {
                    damagedBFFAPlayer.setLastHitPlayer(damager);
                    damagedBFFAPlayer.addDamage(damager, e.getFinalDamage());
                    ItemStack itemStack = damager.getInventory().getItemInMainHand();
                    if (itemStack != null && itemStack.getType() == Material.STICK) {
                        int durability = damagerBFFAPlayer.getKbStickDurability() - 1;
                        if (durability == 0) {
                            damager.setItemInHand(new ItemStack(Material.AIR));
                            damager.playSound(damager.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
                        }
                        damagerBFFAPlayer.setKbStickDurability(durability);
                        PacketUtils.sendTitle(damager, "", "&c(" + durability + "/15)", 0, 10, 0);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player) {
            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                e.setCancelled(true);
                return;
            }

            if (!WorldManager.getInstance().getCurrentMap().isPvpAble()) {
                e.setCancelled(true);
                return;
            }

            BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);

            if (!buildFFAPlayer.isInGame()) {
                e.setCancelled(true);
                return;
            }
        }
    }
}
