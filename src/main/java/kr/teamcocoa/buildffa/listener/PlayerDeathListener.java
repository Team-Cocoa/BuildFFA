package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.utils.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;

import kr.teamcocoa.buildffa.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        final Player p = e.getEntity();
        final BffaPlayer bffaPlayer = Main.playerData.get(p);
        final int deadPlayerKillStreak = Main.playerData.get(p).getPlayerKillStreak();
        bffaPlayer.setThrewPearlTime(System.currentTimeMillis());
        bffaPlayer.setPlayerKillStreak(0);
        bffaPlayer.addDeaths();
        if(bffaPlayer.isNicked()) {
            bffaPlayer.getNickedBffaPlayer().addDeaths();
        }
        e.setDroppedExp(0);
        e.getDrops().clear();


        Location spawn = WorldManager.getInstance().getSpawnByName(WorldManager.getInstance().getCurrentMap());
        for(PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }
        Bukkit.getScheduler().runTaskLater(Main.inst(), () -> {

            p.spigot().respawn();
            p.teleport(spawn);
            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            bffaPlayer.setInGame(false);
            bffaPlayer.setLatestDeadTime(System.currentTimeMillis());
        }, 1L);

        if (bffaPlayer.getLastHitPlayer() instanceof Player) {

            if(p.equals(bffaPlayer.getLastHitPlayer())) {
                return;
            }

            if(!Main.playerData.containsKey(bffaPlayer.getLastHitPlayer())) {
                return;
            }

            BffaPlayer killerBffaPlayer = Main.playerData.get(bffaPlayer.getLastHitPlayer());
            String killerName = bffaPlayer.getLastHitPlayer().getName();
            Player killer = bffaPlayer.getLastHitPlayer();
            killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1, 2);
            String KillerHealth = (new DecimalFormat("#0.0")).format(killer.getHealth() / 2.0D);

            killerBffaPlayer.addKills();
            if(killerBffaPlayer.isNicked()) {
                killerBffaPlayer.getNickedBffaPlayer().addKills();
            }


            p.sendMessage(LangUtils.getMessage(p, MessageEnum.PLAYER_KILL).replaceAll("%KILLER%", killerName).replaceAll("%KILLERHEALTH%", KillerHealth));

            int killerKillstreak = killerBffaPlayer.getPlayerKillStreak() + 1;
            killer.setHealth(20.0D);
            killer.setLevel(killerKillstreak);
            killerBffaPlayer.setPlayerKillStreak(killerKillstreak);
            if(killerKillstreak > killerBffaPlayer.getBestKillStreaks()) {
                killerBffaPlayer.setBestKillStreaks(killerKillstreak);
            }

            if(killerKillstreak % 3 == 0){
                try {
                    String kit = Main.inst().kitData.getKitByInt(Main.inst().kitData.getKit(killer));
                    int index = Arrays.asList(killerBffaPlayer.getInventory()).indexOf(new ItemStack(Material.ENDER_PEARL, 2));
                    if(killer.getInventory().getItem(index) == null) {
                        ItemStack blockItem = new ItemStack(Material.ENDER_PEARL,  1);
                        killer.getInventory().setItem(index, blockItem);
                    }
                    else if(killer.getInventory().getItem(index).getAmount() < 2) {
                        int amount = p.getKiller().getInventory().getItem(index).getAmount();
                        ItemStack blockItem = new ItemStack(Material.ENDER_PEARL, amount + 1);
                        killer.getInventory().setItem(index, blockItem);
                    }
                    if(kit.toLowerCase() == "archer") {
                        int index1 = Arrays.asList(Main.playerData.get(p.getKiller()).getInventory()).indexOf(new ItemStack(Material.ARROW, 16));
                        if(killer.getInventory().getItem(index) == null) {
                            ItemStack blockItem = new ItemStack(Material.ARROW,  5);
                            killer.getInventory().setItem(index1, blockItem);
                        }
                        else if(killer.getInventory().getItem(index1).getAmount() < 16) {
                            int amount1 = killer.getInventory().getItem(index1).getAmount();
                            ItemStack blockItem = new ItemStack(Material.ARROW, amount1 + (amount1 + 5 < 16 ? 5 : 5 - (amount1 + 5 - 16)));
                            killer.getInventory().setItem(index1, blockItem);
                        }
                    }
                    killer.playSound(p.getKiller().getLocation(), Sound.LEVEL_UP, 100.0F, 0.0F);
                }
                catch (NullPointerException e1) {

                }
            }
            bffaPlayer.setLastHitPlayer(null);


            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.inst(), () -> {
                // 코드 원작자 나가 뒤져라 씨발
                // 정리가 시급하다 나중에
                if (deadPlayerKillStreak >= 5) {
                    String killstreakPlayerString = String.valueOf(deadPlayerKillStreak);
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(LangUtils.getMessage(player, MessageEnum.KILL_STREAK_BROKEN).replaceAll("%KILLSTREAK%", killstreakPlayerString).replaceAll("%KILLER%", killerName).replaceAll("%PLAYER%", p.getName()));
                    }
                }
                if (killerKillstreak != 0 && (killerKillstreak % 5 == 0 || killerKillstreak > 15)) {
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(LangUtils.getMessage(player, MessageEnum.KILL_STREAK).replaceAll("%KILLSTREAK%", String.valueOf(killerKillstreak)).replaceAll("%PLAYER%", killerName));
                    }
                }
            }, 3L);
        }
    }
}
