package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.utils.Config;
import kr.teamcocoa.buildffa.utils.Locations;
import kr.teamcocoa.buildffa.utils.ScoreboardManager;
import kr.teamcocoa.buildffa.utils.Stats;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
  @EventHandler
  public static void onJoin(PlayerJoinEvent e) {
    final Player p = e.getPlayer();
    p.setLevel(0);
    ScoreboardManager.setScoreboard(p);
    String uuid = String.valueOf(p.getUniqueId());
    if (Config.config.getBoolean("stats")) {
      Stats.createPlayer(uuid);
    }
    Main.playerData.put(p, new BffaPlayer(p));
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
        if (!Main.playerData.get(p).isBuild()) {
          Main.playerData.get(p).setJoinInventory();
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
