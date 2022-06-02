package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.kit.KitData;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.Title;
import kr.teamcocoa.buildffa.world.WorldManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
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
        if(e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
            return;
        }
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
                    ItemStack itemStack = damager.getItemInHand();
                    if(itemStack != null && itemStack.getType() == Material.STICK) {
                        BffaPlayer damagerBffaPlayer = Main.playerData.get(damager);
                        int durability = damagerBffaPlayer.getKbStickDurability() - 1;
                        if(durability == 0) {
                            damager.setItemInHand(new ItemStack(Material.AIR));
                            damager.playSound(damager.getLocation(), Sound.ITEM_BREAK, 100, 0);
                        }
                        damagerBffaPlayer.setKbStickDurability(durability);
                        Title.sendTitle(damager, "", "&c(" + durability + "/15)", 0, 10, 0);
                    }
                }
                synchronized (damagedPlayer) {
                    if (damagedPlayer.getHealth() - e.getFinalDamage() <= 0) {
                        e.setCancelled(true);
                        Main.getInstance().getServer().getPluginManager().callEvent(CraftEventFactory.callPlayerDeathEvent(((CraftPlayer) damagedPlayer).getHandle(), null, "", true));
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
