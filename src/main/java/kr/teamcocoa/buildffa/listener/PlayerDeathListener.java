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
  public static String killstreakKiller;
  
  public static int level = 0;
  
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent e) {
    final Player p = e.getEntity();
    String uuid = String.valueOf(p.getUniqueId());
      Main.inst().stats.addDeaths(uuid, Integer.valueOf(1));
      BffaPlayer bffaPlayer = Main.playerData.get(p);
      bffaPlayer.setThrewPearlTime(System.currentTimeMillis());
      bffaPlayer.setPlayerKillStreak(0);
      Main.playerData.put(p, bffaPlayer);
    if (Locations.getCurrentMap() != null) {
      String Mapname = Locations.getCurrentMap();
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
      final String uuidKiller = String.valueOf(p.getKiller().getUniqueId());
      final String nameKiller = p.getKiller().getName();
      final String displaynameKiller = p.getKiller().getDisplayName();
      if (Config.config.getBoolean("stats"))
        Main.inst().stats.addKills(uuidKiller, Integer.valueOf(1));
      if (Config.config.getBoolean("message.playerkill")) {
        if (Config.config.getBoolean("displayname.deaths")) {
          Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.killall").replaceAll("%PLAYER%", p.getDisplayName()).replaceAll("%KILLER%", p.getKiller().getName()).replaceAll("&", "§"));
        } else {
          Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.killall").replaceAll("%PLAYER%", p.getName()).replaceAll("%KILLER%", p.getKiller().getName()).replaceAll("&", "§"));
        } 
      } else {
        e.setDeathMessage(null);
      } 
      String KillerHealth = (new DecimalFormat("#0.0")).format(p.getKiller().getHealth() / 2.0D);
      if (Config.config.getBoolean("displayname.deaths")) {
        p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.kill").replaceAll("&", "§").replaceAll("%KILLER%", p.getKiller().getName()).replaceAll("%KILLERHEALTH%", KillerHealth));
      } else {
        p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.kill").replaceAll("&", "§").replaceAll("%KILLER%", p.getKiller().getName()).replaceAll("%KILLERHEALTH%", KillerHealth));
      } 
      p.getKiller().setHealth(20.0D);
      level = Main.playerData.get(p.getKiller()).getPlayerKillStreak();
      p.getKiller().setLevel(level + 1);
      Main.playerData.get(p.getKiller()).setPlayerKillStreak(level + 1);
      if(Main.playerData.get(p.getKiller()).getPlayerKillStreak() % 3 == 0){
          try {
            String kit = Main.inst().kitData.getKitByInt(Main.inst().kitData.getKit(p.getKiller()));
            int index = Arrays.asList(Main.playerData.get(p.getKiller()).getInventory()).indexOf(new ItemStack(Material.ENDER_PEARL, 2));
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
      }
      level = Main.playerData.get(p.getKiller()).getPlayerKillStreak();
      killstreakKiller = String.valueOf(level);
      Bukkit.getScheduler().runTaskLater(Main.inst(), () -> {
        PlayerDeathListener.killstreakKiller = String.valueOf(PlayerDeathListener.level);
        int killstreakPlayer = p.getLevel();
//        Config.player.set("players." + uuidKiller + ".killstreak", Integer.valueOf(PlayerDeathListener.level));
//        try {
//          Config.player.save(Config.playerFile);
//        } catch (IOException e1) {
//          e1.printStackTrace();
//        }
//        if (Config.player.getString("players." + p.getUniqueId() + ".killstreak") != null)
//          killstreakPlayer = Config.player.getInt("players." + p.getUniqueId() + ".killstreak");
        killstreakPlayer = Main.playerData.get(p).getPlayerKillStreak();
        if (Config.config.getBoolean("message.killstreak")) {
          if (killstreakPlayer >= 5) {
            String killstreakPlayerString = String.valueOf(killstreakPlayer);
            if (Config.config.getBoolean("displayname.killstreak")) {
              Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.killstreakbroken").replaceAll("%KILLSTREAK%", killstreakPlayerString).replaceAll("%KILLER%", displaynameKiller).replaceAll("%PLAYER%", p.getName()).replaceAll("&", "§"));
            } else {
              Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.killstreakbroken").replaceAll("%KILLSTREAK%", killstreakPlayerString).replaceAll("%KILLER%", nameKiller).replaceAll("%PLAYER%", p.getName()).replaceAll("&", "§"));
            }
          }
          if ((((PlayerDeathListener.level == 5) ? 1 : 0) | ((PlayerDeathListener.level == 10) ? 1 : 0) | ((PlayerDeathListener.level >= 15) ? 1 : 0)) != 0)
            if (Config.config.getBoolean("displayname.killstreak")) {
              Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.killstreak").replaceAll("%KILLSTREAK%", PlayerDeathListener.killstreakKiller).replaceAll("%PLAYER%", displaynameKiller).replaceAll("&", "§"));
            } else {
              Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.killstreak").replaceAll("%KILLSTREAK%", PlayerDeathListener.killstreakKiller).replaceAll("%PLAYER%", nameKiller).replaceAll("&", "§"));
            }
        }
//        if (Config.player.getString("players." + p.getUniqueId() + ".killstreak") != null) {
//          Config.player.set("players." + p.getUniqueId() + ".killstreak", null);
//          try {
//            Config.player.save(Config.playerFile);
//          } catch (IOException e2) {
//            e2.printStackTrace();
//          }
//        }
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
  }
}
