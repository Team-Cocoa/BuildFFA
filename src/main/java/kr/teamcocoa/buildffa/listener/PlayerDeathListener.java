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
    bffaPlayer.setLastHitPlayer(null);
    e.setDroppedExp(0);
    e.getDrops().clear();


    Location spawn = WorldManager.getInstance().getSpawnByName(WorldManager.getInstance().getCurrentMap());

    Bukkit.getScheduler().runTaskLater(Main.inst(), () -> {
      for(PotionEffect effect : p.getActivePotionEffects()) {
        p.removePotionEffect(effect.getType());
      }
      p.spigot().respawn();
      p.teleport(spawn);
      p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
      bffaPlayer.setInGame(false);
      bffaPlayer.setLatestDeadTime(System.currentTimeMillis());
      }, 1L);

    if (p.getKiller() instanceof Player) {

      if(p.equals(p.getKiller())) {
        return;
      }

      final BffaPlayer killerBffaPlayer = Main.playerData.get(p.getKiller());
      final String nameKiller = p.getKiller().getName();
      killerBffaPlayer.addKills();
      if(killerBffaPlayer.isNicked()) {
        killerBffaPlayer.getNickedBffaPlayer().addKills();
      }

      String KillerHealth = (new DecimalFormat("#0.0")).format(p.getKiller().getHealth() / 2.0D);
      p.sendMessage(LangUtils.getMessage(p, MessageEnum.PLAYER_KILL).replaceAll("%KILLER%", p.getKiller().getName()).replaceAll("%KILLERHEALTH%", KillerHealth));

      int killerKillstreak = killerBffaPlayer.getPlayerKillStreak() + 1;
      p.getKiller().setHealth(20.0D);
      p.getKiller().setLevel(killerKillstreak);
      killerBffaPlayer.setPlayerKillStreak(killerKillstreak);

      /*
       * 여기서 부터 max kill streak 설정
       * */
      if(killerKillstreak > killerBffaPlayer.getBestKillStreaks()) {
        killerBffaPlayer.setBestKillStreaks(killerKillstreak);
      }
      /*
       * max kill streak 설정 끝
       * */

      if(killerKillstreak % 3 == 0){
          try {
            String kit = Main.inst().kitData.getKitByInt(Main.inst().kitData.getKit(p.getKiller()));
            int index = Arrays.asList(killerBffaPlayer.getInventory()).indexOf(new ItemStack(Material.ENDER_PEARL, 2));
            if(p.getKiller().getInventory().getItem(index) == null) {
              ItemStack blockItem = new ItemStack(Material.ENDER_PEARL,  1);
              p.getKiller().getInventory().setItem(index, blockItem);
            }
            else if(p.getKiller().getInventory().getItem(index).getAmount() < 2) {
              int amount = p.getKiller().getInventory().getItem(index).getAmount();
              ItemStack blockItem = new ItemStack(Material.ENDER_PEARL, amount + 1);
              p.getKiller().getInventory().setItem(index, blockItem);
            }
            if(kit.toLowerCase() == "archer") {
              int index1 = Arrays.asList(Main.playerData.get(p.getKiller()).getInventory()).indexOf(new ItemStack(Material.ARROW, 16));
              if(p.getKiller().getInventory().getItem(index) == null) {
                ItemStack blockItem = new ItemStack(Material.ARROW,  5);
                p.getKiller().getInventory().setItem(index1, blockItem);
              }
              else if(p.getKiller().getInventory().getItem(index1).getAmount() < 16) {
                int amount1 = p.getKiller().getInventory().getItem(index1).getAmount();
                ItemStack blockItem = new ItemStack(Material.ARROW, amount1 + (amount1 + 5 < 16 ? 5 : 5 - (amount1 + 5 - 16)));
                p.getKiller().getInventory().setItem(index1, blockItem);
              }
            }
            p.getKiller().playSound(p.getKiller().getLocation(), Sound.LEVEL_UP, 100.0F, 0.0F);
          }
          catch (NullPointerException e1) {

          }
          killerBffaPlayer.setLastHitPlayer(null);
      }


      Bukkit.getScheduler().runTaskLaterAsynchronously(Main.inst(), () -> {
        // 코드 원작자 나가 뒤져라 씨발
        // 정리가 시급하다 나중에
        if (deadPlayerKillStreak >= 5) {
          String killstreakPlayerString = String.valueOf(deadPlayerKillStreak);
          for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(LangUtils.getMessage(player, MessageEnum.KILL_STREAK_BROKEN).replaceAll("%KILLSTREAK%", killstreakPlayerString).replaceAll("%KILLER%", nameKiller).replaceAll("%PLAYER%", p.getName()));
          }
        }
        if (killerKillstreak != 0 && (killerKillstreak % 5 == 0 || killerKillstreak > 15)) {
          for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(LangUtils.getMessage(player, MessageEnum.KILL_STREAK).replaceAll("%KILLSTREAK%", String.valueOf(killerKillstreak)).replaceAll("%PLAYER%", nameKiller));
          }
        }
      }, 3L);
    }
  }
}
