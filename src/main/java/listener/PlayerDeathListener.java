package listener;

import main.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import utils.Config;
import utils.Locations;
import utils.Stats;
import java.io.IOException;
import java.text.DecimalFormat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

public class PlayerDeathListener implements Listener {
  public static String killstreakKiller;
  
  public static int level = 0;
  
  @EventHandler
  public static void onPlayerDeath(PlayerDeathEvent e) {
    final Player p = e.getEntity();
    String uuid = String.valueOf(p.getUniqueId());
    if (Config.config.getBoolean("stats")) {
      Stats.addDeaths(uuid, Integer.valueOf(1));
      Main.playerData.latestDeadTime.put(p, System.currentTimeMillis());
    }
    if (Locations.getCurrentMap() != null) {
      String Mapname = Locations.getCurrentMap();
      final Location spawnloc = Locations.getSpawnLocation(Mapname);
      Bukkit.getScheduler().runTaskLater((Plugin)Main.inst(), new Runnable() {
            public void run() {
              PlayerMoveListener.gotInventory.remove(p);
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
        Stats.addKills(uuidKiller, Integer.valueOf(1)); 
      if (Config.config.getBoolean("message.playerkill")) {
        if (Config.config.getBoolean("displayname.deaths")) {
          Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.killall").replaceAll("%PLAYER%", p.getDisplayName()).replaceAll("%KILLER%", p.getKiller().getDisplayName()).replaceAll("&", "§"));
        } else {
          Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.killall").replaceAll("%PLAYER%", p.getName()).replaceAll("%KILLER%", p.getKiller().getName()).replaceAll("&", "§"));
        } 
      } else {
        e.setDeathMessage(null);
      } 
      String KillerHealth = (new DecimalFormat("#0.0")).format(p.getKiller().getHealth() / 2.0D);
      if (Config.config.getBoolean("displayname.deaths")) {
        p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.kill").replaceAll("&", "§").replaceAll("%KILLER%", p.getKiller().getDisplayName()).replaceAll("%KILLERHEALTH%", KillerHealth));
      } else {
        p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.kill").replaceAll("&", "§").replaceAll("%KILLER%", p.getKiller().getName()).replaceAll("%KILLERHEALTH%", KillerHealth));
      } 
      p.getKiller().setHealth(20.0D);
      level = p.getKiller().getLevel();
      p.getKiller().setLevel(level + 1);
      if(p.getKiller().getLevel() % 3 == 0){
        ItemStack[] inventorySorting = Inventory.getSortInventoryHotbar(p.getKiller(), Config.player.getString("players." + p.getKiller().getUniqueId().toString() + ".kitselected"));
        int i = 0;
        Player killer = p.getKiller();
        for(ItemStack item : inventorySorting){
          if(item.isSimilar(new ItemStack(Material.ENDER_PEARL))){
            int enderPearlAmount = killer.getInventory().getItem(i).getAmount();
            if(enderPearlAmount < 2){
              killer.getInventory().setItem(enderPearlAmount + 1, new ItemStack(Material.ENDER_PEARL));
            }
            else{
              break;
            }
          }
        }
      }
      level = p.getKiller().getLevel();
      killstreakKiller = String.valueOf(level);
      Bukkit.getScheduler().runTaskLater((Plugin)Main.inst(), new Runnable() {
            public void run() {
              PlayerDeathListener.killstreakKiller = String.valueOf(PlayerDeathListener.level);
              int killstreakPlayer = p.getLevel();
              Config.player.set("players." + uuidKiller + ".killstreak", Integer.valueOf(PlayerDeathListener.level));
              try {
                Config.player.save(Config.playerFile);
              } catch (IOException e) {
                e.printStackTrace();
              } 
              if (Config.player.getString("players." + p.getUniqueId() + ".killstreak") != null)
                killstreakPlayer = Config.player.getInt("players." + p.getUniqueId() + ".killstreak"); 
              if (Config.config.getBoolean("message.killstreak")) {
                if (killstreakPlayer >= 5) {
                  String killstreakPlayerString = String.valueOf(killstreakPlayer);
                  if (Config.config.getBoolean("displayname.killstreak")) {
                    Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.killstreakbroken").replaceAll("%KILLSTREAK%", killstreakPlayerString).replaceAll("%KILLER%", displaynameKiller).replaceAll("%PLAYER%", p.getDisplayName()).replaceAll("&", "§"));
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
              if (Config.player.getString("players." + p.getUniqueId() + ".killstreak") != null) {
                Config.player.set("players." + p.getUniqueId() + ".killstreak", null);
                try {
                  Config.player.save(Config.playerFile);
                } catch (IOException e2) {
                  e2.printStackTrace();
                } 
              } 
            }
          }, 3L);
    } else if (Config.config.getBoolean("message.playerdeath")) {
      if (Config.config.getBoolean("displayname.deaths")) {
        Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.death").replaceAll("%PLAYER%", p.getDisplayName()).replaceAll("&", "§"));
      } else {
        Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("player.death").replaceAll("%PLAYER%", p.getName()).replaceAll("&", "§"));
      } 
    } else {
      e.setDeathMessage(null);
    } 
    if (Config.player.getString("players." + p.getUniqueId() + ".killstreak") != null) {
      Config.player.set("players." + p.getUniqueId() + ".killstreak", null);
      try {
        Config.player.save(Config.playerFile);
      } catch (IOException e2) {
        e2.printStackTrace();
      } 
    } 
    e.setDroppedExp(0);
    e.getDrops().clear();
  }
}
