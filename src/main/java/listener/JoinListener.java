package listener;

import commands.Build;
import main.Main;
import utils.Config;
import utils.Inventory;
import utils.Locations;
import utils.ScoreboardManager;
import utils.Stats;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class JoinListener implements Listener {
  @EventHandler
  public static void onJoin(PlayerJoinEvent e) {
    final Player p = e.getPlayer();
    p.setLevel(0);
    Main.playerData.latestDeadTime.put(p, 0L);
    if (Config.config.getBoolean("scoreboard"))
      ScoreboardManager.setScoreboard(p); 
//    if (Config.config.getBoolean("mapchange") && Config.config.getBoolean("scoreboard") && !ScoreboardManager.Mapchangeupdater.booleanValue())
//      ScoreboardManager.MapChangeUpdater();
    if (Config.player.getString("players." + p.getUniqueId() + ".killstreak") != null) {
      Config.player.set("players." + p.getUniqueId() + ".killstreak", null);
      try {
        Config.player.save(Config.playerFile);
      } catch (IOException e2) {
        e2.printStackTrace();
      } 
    } 
    String uuid = String.valueOf(p.getUniqueId());
    if (Config.config.getBoolean("stats")) {
      Stats.createPlayer(uuid);
    }
    Bukkit.getScheduler().runTaskLater((Plugin)Main.inst(), new Runnable() {
          public void run() {
            if (Build.buildmode.contains(p.getName())) {
              p.setGameMode(GameMode.CREATIVE);
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.info").replaceAll("&", "§"));
            } 
          }
        }, 3L);
    if (Config.config.getBoolean("join-quit-message")) {
      if (Config.config.getBoolean("displayname.joinmessage")) {
        e.setJoinMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("joinmessage").replaceAll("%PLAYER%", p.getDisplayName()).replaceAll("&", "§"));
      } else {
        e.setJoinMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("joinmessage").replaceAll("%PLAYER%", p.getName()).replaceAll("&", "§"));
      } 
    } else {
      e.setJoinMessage(null);
    } 
    if (!Config.config.getString("startmap").equals("none")) {
      if (Locations.getCurrentMap().equals("Keine Map"))
        Locations.setCurrentMap(Config.config.getString("startmap").replaceAll("&", "§")); 
    } else {
      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nostartmap.01").replaceAll("&", "§"));
      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nostartmap.02").replaceAll("&", "§"));
      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nostartmap.03").replaceAll("&", "§"));
      return;
    } 
    if (Config.locations.getString(String.valueOf(Locations.getCurrentMap()) + ".deathheight") != null) {
      if (Config.locations.getString(String.valueOf(Locations.getCurrentMap()) + ".arenaheight") != null) {
        Location mapspawn = Locations.getSpawnLocation(Locations.getCurrentMap());
        p.teleport(mapspawn);
        if (!Build.buildmode.contains(p.getName())) {
          Inventory.setJoinInventory(p);
          p.setHealth(1.0D);
          p.setFoodLevel(20);
        } 
      } else {
        p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("noarenaheight.01").replaceAll("&", "§"));
        p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("noarenaheight.02").replaceAll("&", "§"));
      } 
    } else {
      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nodeathheight.01").replaceAll("&", "§"));
      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nodeathheight.02").replaceAll("&", "§"));
    } 
  }
}
