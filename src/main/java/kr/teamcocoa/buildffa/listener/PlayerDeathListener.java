package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.utils.Config;
import kr.teamcocoa.buildffa.utils.Locations;
import kr.teamcocoa.buildffa.utils.MYSQL;
import kr.teamcocoa.buildffa.utils.Stats;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;

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

public class PlayerDeathListener implements Listener {
  
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent e) {
    final Player p = e.getEntity();
    final BffaPlayer bffaPlayer = Main.playerData.get(p);
    final int deadPlayerKillStreak = Main.playerData.get(p).getPlayerKillStreak();
    //Main.inst().stats.addDeaths(uuid, Integer.valueOf(1));
    bffaPlayer.setThrewPearlTime(System.currentTimeMillis());
    bffaPlayer.setPlayerKillStreak(0);
    bffaPlayer.addDeaths();

    if (Locations.CurrentMapname != null) {
      String Mapname = Locations.CurrentMapname;
      final Location spawnloc = Locations.getSpawnLocation(Mapname);
      Bukkit.getScheduler().runTaskLater((Plugin)Main.inst(), new Runnable() {
            public void run() {
              Main.playerData.get(p).setInGame(false);
              Main.playerData.get(p).setLatestDeadTime(System.currentTimeMillis());
              p.spigot().respawn();
              p.teleport(spawnloc);
              p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
          }, 1L);
    }

    if (p.getKiller() instanceof Player) {

      if(p.equals(p.getKiller())) {
        e.setDeathMessage("");
        return;
      }
      final BffaPlayer killerBffaPlayer = Main.playerData.get(p.getKiller());
      final String uuidKiller = String.valueOf(p.getKiller().getUniqueId());
      final String nameKiller = p.getKiller().getName();
      //Main.inst().stats.addKills(uuidKiller, Integer.valueOf(1));
      killerBffaPlayer.addKills();

      String KillerHealth = (new DecimalFormat("#0.0")).format(p.getKiller().getHealth() / 2.0D);
      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.kill").replaceAll("&", "§").replaceAll("%KILLER%", p.getKiller().getName()).replaceAll("%KILLERHEALTH%", KillerHealth));


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
            e1.printStackTrace();
          }
          Main.playerData.put(p.getKiller(), killerBffaPlayer);
      }


      Bukkit.getScheduler().runTaskLater(Main.inst(), () -> {
        // 코드 원작자 나가 뒤져라 씨발
        // 정리가 시급하다 나중에
        if (deadPlayerKillStreak >= 5) {
          String killstreakPlayerString = String.valueOf(deadPlayerKillStreak);
          for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.killstreakbroken").replaceAll("%KILLSTREAK%", killstreakPlayerString).replaceAll("%KILLER%", nameKiller).replaceAll("%PLAYER%", p.getName()).replaceAll("&", "§"));
          }
        }
        if (killerKillstreak != 0 && (killerKillstreak % 5 == 0 || killerKillstreak > 15)) {
          for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Main.getPrefix() + Config.messages.getString("player.killstreak").replaceAll("%KILLSTREAK%", String.valueOf(killerKillstreak)).replaceAll("%PLAYER%", nameKiller).replaceAll("&", "§"));
          }
        }
      }, 3L);
    }

    else if (Config.config.getBoolean("message.playerdeath")) {
      if (Config.config.getBoolean("displayname.deaths")) {
        Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.death").replaceAll("%PLAYER%", p.getName()).replaceAll("&", "§"));
      } else {
        Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.death").replaceAll("%PLAYER%", p.getName()).replaceAll("&", "§"));
      } 
    } else {
      e.setDeathMessage(null);
    } 
//    if (Config.player.getString("players." + p.getUniqueId() + ".killstreak") != null) {
//      Config.player.set("players." + p.getUniqueId() + ".killstreak", null);
//      try {
//        Config.player.save(Config.playerFile);
//      } catch (IOException e2) {
//        e2.printStackTrace();
//      }
//    }
    e.setDroppedExp(0);
    e.getDrops().clear();
    e.setDeathMessage("");
    Main.playerData.put(p, bffaPlayer);
  }
}
